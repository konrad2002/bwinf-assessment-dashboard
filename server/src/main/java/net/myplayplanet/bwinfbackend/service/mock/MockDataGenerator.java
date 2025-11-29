package net.myplayplanet.bwinfbackend.service.mock;

import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.controller.IntegrationController;
import net.myplayplanet.bwinfbackend.dto.CorrectorDto;
import net.myplayplanet.bwinfbackend.dto.EvaluationDto;
import net.myplayplanet.bwinfbackend.model.EvaluationType;
import net.myplayplanet.bwinfbackend.model.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MockDataGenerator {

    private final IntegrationController controller;
    private final RandomizerService randomizerService;

    private final static Collection<String> correctors = List.of("paulf", "konradw");
    private final static Collection<Integer> taskNumbers = List.of(1, 2, 3, 4, 5);
    private final static Collection<Integer> deductedPoints = List.of(1, 2, 3, 4, 5);
    private final static Collection<String> submissionIds = List.of("0001", "0002", "0003");


    //@Scheduled(fixedRate = 5000) // 5000 ms = 5 seconds
    public void generateEvaluationData() {
        EvaluationDto evaluationDto = EvaluationDto.builder()
                .taskNumber(this.randomizerService.getRandomElement(taskNumbers))
                .evaluationType(EvaluationType.FIRST)
                .pointsDeducted(this.randomizerService.getRandomElement(deductedPoints))
                .corrector(new CorrectorDto(null, this.randomizerService.getRandomElement(correctors)))
                .taskType(TaskType.JWINF)
                .submissionId(this.randomizerService.getRandomElement(submissionIds))
                .correctionContextId(1L)
                .build();

        System.out.println(evaluationDto);

        this.controller.publishNewEvaluation(
                evaluationDto
        );
    }


}
