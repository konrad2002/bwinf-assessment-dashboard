package net.myplayplanet.bwinfbackend.controller;

import net.myplayplanet.bwinfbackend.model.*;
import net.myplayplanet.bwinfbackend.service.scraper.DjangoScraperService;
import net.myplayplanet.bwinfbackend.service.scraper.EvaluationScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class ScrapeController {

    private final DjangoScraperService scraper;
    private final EvaluationScraper evaluationScraper;

    public ScrapeController(DjangoScraperService scraper, EvaluationScraper evaluationScraper) {
        this.scraper = scraper;
        this.evaluationScraper = evaluationScraper;
    }

    // Endpoint to trigger login and store session
    @PostMapping("/scrape/login")
    public Mono<String> login() {
        return scraper.loginAndStoreSession();
    }

    // Endpoint to scrape using stored session
    @PostMapping("/scrape")
    public Mono<String> scrape() {
        return scraper.scrapeWithStoredSession();
    }

    // New endpoint to trigger evaluation scraping from Django page
    @PostMapping("/scrape/evaluate")
    public Mono<String> evaluate() {
        // Use default values if not provided
        CorrectionContext correctionContext = new CorrectionContext();
        correctionContext.setYear(LocalDateTime.of(2025, 11, 29, 10, 0));
        correctionContext.setRound(Round.FIRST);

        EvaluationType evaluationType = EvaluationType.FIRST;

        return scraper.scrapeWithStoredSession()
                .flatMap(html -> {
                    try {
                        Document doc = Jsoup.parse(html);
                        // EvaluationScraper expects a File, so we refactor it to accept Document or HTML string
                        // For now, let's assume we add a new method to EvaluationScraper: parseTable(Document, ...)
                        Map<Corrector, List<Evaluation>> result = evaluationScraper.parseTable(doc, correctionContext, evaluationType);
                        // Return a summary string
                        StringBuilder sb = new StringBuilder();
                        result.forEach((corrector, evals) -> {
                            sb.append(corrector.getShortName())
                              .append(": ")
                              .append(evals.size())
                              .append(" evaluations\n");
                        });
                        return Mono.just(sb.toString());
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
}
