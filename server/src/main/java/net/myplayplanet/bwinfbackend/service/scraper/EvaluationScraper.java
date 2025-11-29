package net.myplayplanet.bwinfbackend.service.scraper;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class EvaluationScraper {

    public record ParseResult(Map<Corrector, List<Evaluation>> evaluations,
                              int unstarted, int firstDone, int done, int inProgres) {
    }

    public ParseResult parseTable(Document doc,
                                  CorrectionContext context) {

        log.info("Starting parseTable");

        log.info("Document {}", doc);

        Map<String, Corrector> correctorMap = new HashMap<>();
        Map<Corrector, List<Evaluation>> evaluationsByCorrector = new HashMap<>();

        Elements rows = doc.select("tr");
        log.info("Found {} table rows", rows.size());

        int unstarted = 0, firstDone = 0, done = 0, inProgres = 0;

        for (Element row : rows) {
            Elements tds = row.select("td");
            if (tds.size() < 2) {
                log.info("Skipping row without enough TDs");
                continue;
            }

            Element td = tds.get(1);
            Element small = td.selectFirst("small.text-muted");
            String name = null;
            if (small != null) {
                name = small.text().trim();
            }

            String text = td.ownText().trim();

            // log.info("Processing corrector='{}' | points-text='{}'", name, text);
            int points = 0;
            try {
                points = Integer.parseInt(text);
                // log.info("Parsed points={}", points);

            } catch (NumberFormatException e) {
                log.info("Could not parse points '{}'", text);
            }
            String submissionId = "";
            Element submissionLink = tds.get(0).selectFirst("a");
            if (submissionLink != null) {
                String href = submissionLink.attr("href");
                // log.info("Found submission link: {}", href);

                String[] parts = href.split("/");
                submissionId = parts[parts.length - 1].isEmpty()
                        ? parts[parts.length - 2]
                        : parts[parts.length - 1];

                // log.info("Parsed submission ID={}", submissionId);
            }

            // Determine evaluation type
            EvaluationType evaluationType = null;
            Element form = row.selectFirst("form");

            if (form != null) {
                String formText = form.text().toLowerCase();
                log.info("Form text={}", formText);

                if (formText.contains("claim zweitbewertung")) {
                    evaluationType = EvaluationType.FIRST;
                    firstDone++;
                    log.info("evaluationType=FIRST");
                } else if (formText.contains("claim erstbewertung")) {
                    unstarted++;
                    log.info("evaluationType=EMPTY (unstarted)");
                } else {
                    evaluationType = EvaluationType.SECOND;
                    done++;
                    log.info("evaluationType=SECOND");
                }
            } else {
                inProgres++;
                evaluationType = EvaluationType.IN_PROGRESS;
                log.info("No form found → evaluationType=EMPTY");
            }

            Evaluation eval = new Evaluation();

            // Get or create corrector
            if (name != null) {
                Corrector corrector = correctorMap.computeIfAbsent(name, n -> {
                    log.info("Creating new Corrector '{}'", n);
                    Corrector c = new Corrector();
                    c.setShortName(n);
                    return c;
                });
                eval.setCorrector(corrector);

                evaluationsByCorrector
                        .computeIfAbsent(corrector, k -> new ArrayList<>())
                        .add(eval);
            }


            // Build Evaluation
            eval.setSubmissionId(submissionId);
            eval.setCorrectionContext(context);
            eval.setEvaluationType(evaluationType);
            eval.setCreatedAt(LocalDateTime.now());

            if (context.getRound() == Round.FIRST) {
                eval.setPointsDeducted(5 - points);
            } else {
                eval.setPointsDeducted(20 - points);
            }

            log.info("Created evaluation: submission={}, corrector={}, type={}, pointsDeducted={}",
                    submissionId, name, evaluationType, eval.getPointsDeducted());


        }

        log.info("Parse finished: unstarted={}, firstDone={}, done={}",
                unstarted, firstDone, done);

        return new ParseResult(evaluationsByCorrector, unstarted, firstDone, done, inProgres);
    }
}
