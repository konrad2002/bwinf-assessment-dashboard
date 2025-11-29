package net.myplayplanet.bwinfbackend.service.scraper;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.myplayplanet.bwinfbackend.model.*;
import net.myplayplanet.bwinfbackend.repository.CorrectionContextRepository;
import net.myplayplanet.bwinfbackend.repository.EvaluationRepository;
import net.myplayplanet.bwinfbackend.repository.TaskProgressDataPointRepository;
import net.myplayplanet.bwinfbackend.service.CorrectorService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebScraper {

    private final DjangoScraperService scraper;
    private final EvaluationScraper evaluationScraper;
    private final EvaluationRepository evaluationRepository;
    private final CorrectionContextRepository correctionContextRepository;
    private final TaskProgressDataPointRepository taskProgressDataPointRepository;
    private final CorrectorService correctorService;


    @Scheduled(fixedRate = 60000) // 1x per minute
    public void runScraper() {

        long correctionContext = 1L;

        for (int i = 1; i <= 2; i++) {
            scrape(correctionContext, TaskType.JWINF, i);
        }

        for (int i = 1; i <= 5; i++) {
            scrape(correctionContext, TaskType.BWINF, i);
        }
    }

    public void scrape(Long correctionContext, TaskType taskType, Integer taskNumber) {
        // Use default values if not provided

        CorrectionContext referenceById = this.correctionContextRepository.getReferenceById(correctionContext);

        this.scraper.loginAndStoreSession().block();

        EvaluationScraper.ParseResult scrapedData = scraper.scrapeWithStoredSession("192.168.146.184", taskType, taskNumber)
                .flatMap(html -> {
                    try {
                        Document doc = Jsoup.parse(html);
                        // EvaluationScraper expects a File, so we refactor it to accept Document or HTML string
                        // For now, let's assume we add a new method to EvaluationScraper: parseTable(Document, ...)
                        return Mono.just(evaluationScraper.parseTable(doc, referenceById));
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                }).block();
        if (scrapedData == null) {
            log.error("Could not scrape data");
            return;
        }

        scrapedData.evaluations().keySet().stream().map(corrector ->
                this.correctorService.getOrCreate(corrector.getShortName()));

        Set<Evaluation> collect = scrapedData.evaluations().values().stream().flatMap(Collection::stream).collect(Collectors.toSet())
                .stream()
                .peek(evaluation -> {
                    evaluation.setTaskType(taskType);
                    evaluation.setTaskNumber(taskNumber);
                    evaluation.setCorrector(this.correctorService.getOrCreate(evaluation.getCorrector().getShortName()));
                })
                .filter(this::isDiff)
                .collect(Collectors.toSet());

        TaskProgressDataPoint taskProgressDataPoint = new TaskProgressDataPoint();

        taskProgressDataPoint.setOpen(scrapedData.unstarted());
        taskProgressDataPoint.setOnlyFirstDone(scrapedData.firstDone());
        taskProgressDataPoint.setInProgress(scrapedData.inProgres());
        taskProgressDataPoint.setDone(scrapedData.done());
        taskProgressDataPoint.setCorrectionContext(referenceById);
        taskProgressDataPoint.setCreatedAt(LocalDateTime.now());
        taskProgressDataPoint.setTaskType(taskType);
        taskProgressDataPoint.setTaskNumber(taskNumber);

        this.taskProgressDataPointRepository.save(taskProgressDataPoint);
        this.evaluationRepository.saveAll(collect);
    }

    private boolean isDiff(Evaluation evaluation) {
        return !this.evaluationRepository.existsByCorrector_IdAndEvaluationTypeAndCorrectionContext_IdAndTaskTypeAndTaskNumberAndSubmissionId(
                evaluation.getCorrector().getId(),
                evaluation.getEvaluationType(),
                evaluation.getCorrectionContext().getId(),
                evaluation.getTaskType(),
                evaluation.getTaskNumber(),
                evaluation.getSubmissionId()
        );
    }

}
