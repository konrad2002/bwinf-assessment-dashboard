package net.myplayplanet.bwinfbackend.controller;

import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.model.TaskType;
import net.myplayplanet.bwinfbackend.service.scraper.DjangoScraperService;
import net.myplayplanet.bwinfbackend.service.scraper.WebScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/scrape")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ScrapeController {

    private final DjangoScraperService scraper;
    private final WebScraper webScraper;

    // Endpoint to trigger login and store session
    @PostMapping("/login")
    public Mono<String> login() {
        return scraper.loginAndStoreSession();
    }

    @PostMapping("/{ctxId}/type/{type}/task/{taskId}")
    public String scrape(@PathVariable Long ctxId, @PathVariable String type, @PathVariable Integer taskId) {
        webScraper.scrape(ctxId, TaskType.valueOf(type), taskId);
        return "worked!";
    }

    @GetMapping("test")
    public String test() {
        return "test";
    }

}
