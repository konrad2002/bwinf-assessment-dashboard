package net.myplayplanet.bwinfbackend.service.scraper;

import lombok.Getter;
import net.myplayplanet.bwinfbackend.model.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluationScraper {

    public record ParseResult(Map<Corrector, List<Evaluation>> evaluations,
                              int unstarted, int firstDone, int done) {
    }

    /**
     * Parses a Jsoup Document and returns a map of Corrector -> list of Evaluations
     */
    public ParseResult parseTable(Document doc,
                                  CorrectionContext context) {
        Map<String, Corrector> correctorMap = new HashMap<>();
        Map<Corrector, List<Evaluation>> evaluationsByCorrector = new HashMap<>();

        Elements rows = doc.select("tr");

        int unstarted = 0, firstDone = 0, done = 0;

        for (Element row : rows) {
            Elements tds = row.select("td");
            if (tds.size() < 2) continue;

            // td containing points and corrector name
            Element td = tds.get(1);
            Element small = td.selectFirst("small.text-muted");
            if (small == null) continue;

            String name = small.text().trim();
            String text = td.ownText().trim(); // points before the <small>

            try {
                int points = Integer.parseInt(text);

                // Optional: extract submissionId from first td
                String submissionId = "";
                Element submissionLink = tds.get(0).selectFirst("a");
                if (submissionLink != null) {
                    String href = submissionLink.attr("href"); // e.g. "/submission/JW/3287/"
                    String[] parts = href.split("/");
                    submissionId = parts[parts.length - 1].isEmpty() ? parts[parts.length - 2] : parts[parts.length - 1];
                }

                // Determine EvaluationType
                EvaluationType evaluationType = null;
                Element form = row.selectFirst("form");
                if (form != null) {
                    String formText = form.text().toLowerCase();
                    if (formText.contains("claim zweitbewertung")) {
                        evaluationType = EvaluationType.FIRST;
                        firstDone++;
                    } else if (formText.contains("claim erstbewertung")) {
                        unstarted++;
                    } else {
                        evaluationType = EvaluationType.SECOND;
                        done++;
                    }

                }
                // If no form/button, evaluationType remains null (EMPTY)

                // Get or create Corrector
                Corrector corrector = correctorMap.computeIfAbsent(name, n -> {
                    Corrector c = new Corrector();
                    c.setShortName(n);
                    return c;
                });

                // Create Evaluation object
                Evaluation eval = new Evaluation();
                eval.setCorrector(corrector);
                eval.setSubmissionId(submissionId);
                eval.setCorrectionContext(context);
                eval.setEvaluationType(evaluationType);
                eval.setCreatedAt(LocalDateTime.now());

                if (context.getRound() == Round.FIRST) {
                    eval.setPointsDeducted(5 - points);
                } else {
                    eval.setPointsDeducted(20 - points);
                }

                evaluationsByCorrector.computeIfAbsent(corrector, k -> new ArrayList<>()).add(eval);

            } catch (NumberFormatException e) {
                // ignore invalid numbers
            }
        }

        return new ParseResult(evaluationsByCorrector, unstarted, firstDone, done);
    }
}
