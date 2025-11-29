package net.myplayplanet.bwinfbackend.controller;

import net.myplayplanet.bwinfbackend.service.scraper.DjangoScraperService;
import net.myplayplanet.bwinfbackend.service.scraper.EvaluationScraper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ScrapeController {

    private final DjangoScraperService scraper;

    public ScrapeController(DjangoScraperService scraper, EvaluationScraper evaluationScraper) {
        this.scraper = scraper;
    }

    // Endpoint to trigger login and store session
    @PostMapping("/scrape/login")
    public Mono<String> login() {
        return scraper.loginAndStoreSession();
    }

}
