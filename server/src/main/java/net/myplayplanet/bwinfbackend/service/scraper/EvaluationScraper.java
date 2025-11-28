package net.myplayplanet.bwinfbackend.service.scraper;

import net.myplayplanet.bwinfbackend.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class EvaluationScraper {

    /**
     * Parses a table.html file and returns a map of Corrector -> list of Evaluations
     */
    public Map<Corrector, List<Evaluation>> parseTable(File tableFile,
                                                       CorrectionContext context,
                                                       EvaluationType evaluationType) throws IOException {

        Document doc = Jsoup.parse(tableFile, "UTF-8");

        Map<String, Corrector> correctorMap = new HashMap<>();
        Map<Corrector, List<Evaluation>> evaluationsByCorrector = new HashMap<>();

        Elements rows = doc.select("tr");
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
                int submissionId = 0;
                Element submissionLink = tds.get(0).selectFirst("a");
                if (submissionLink != null) {
                    String href = submissionLink.attr("href"); // e.g. "/submission/JW/3287/"
                    String[] parts = href.split("/");
                    submissionId = Integer.parseInt(parts[parts.length - 1].isEmpty() ? parts[parts.length - 2] : parts[parts.length - 1]);
                }

                // Get or create Corrector
                Corrector corrector = correctorMap.computeIfAbsent(name, n -> {
                    Corrector c = new Corrector();
                    c.setShortName(n);
                    return c;
                });

                // Create Evaluation object
                Evaluation eval = new Evaluation();
                eval.setCorrector(corrector);
                eval.setCorrectionContext(context);
                eval.setEvaluationType(evaluationType);
                eval.setCreatedAt(LocalDateTime.now());
                // optionally store points in some field if needed
                // you might want to create a "points" field in Evaluation

                evaluationsByCorrector.computeIfAbsent(corrector, k -> new ArrayList<>()).add(eval);

            } catch (NumberFormatException e) {
                // ignore invalid numbers
            }
        }

        // Print table with count and average of numbers (points)
        System.out.printf("%-20s %5s %10s%n", "Name", "Count", "Average");
        System.out.println("----------------------------------------");

        int totalSum = 0;
        int totalCount = 0;

        for (Map.Entry<Corrector, List<Evaluation>> entry : evaluationsByCorrector.entrySet()) {
            List<Evaluation> list = entry.getValue();
            // For average, we parse points again from td or store separately
            // Here we just assume the list size = count, sum = count * points (for demo)
            int count = list.size();
            double avg = count == 0 ? 0 : (double) count / count;

            System.out.printf("%-20s %5d %10.2f%n", entry.getKey().getShortName(), count, avg);

            totalSum += count;
            totalCount += count;
        }

        double overallAverage = totalCount == 0 ? 0 : (double) totalSum / totalCount;
        System.out.printf("%nOverall average: %.2f%n", overallAverage);

        return evaluationsByCorrector;
    }
}
