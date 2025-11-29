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

    private final static Collection<String> correctors = List.of("paulf", "konradw");
    private final static Collection<Integer> taskNumbers = List.of(1, 2, 3, 4, 5);
    private final static Collection<Integer> deductedPoints = List.of(1, 2, 3, 4, 5);
    private final static Collection<String> submissionIds = List.of("0001", "0002", "0003");


    @Scheduled(fixedRate = 5000) // 5000 ms = 5 seconds
    public void generateEvaluationData() {
        EvaluationDto evaluationDto = EvaluationDto.builder()
                .taskNumber(getRandomElement(taskNumbers))
                .evaluationType(EvaluationType.FIRST)
                .pointsDeducted(getRandomElement(deductedPoints))
                .corrector(new CorrectorDto(null, getRandomElement(correctors)))
                .taskType(TaskType.JWINF)
                .submissionId(getRandomElement(submissionIds))
                .correctionContextId(1L)
                .build();

        System.out.println(evaluationDto);

        this.controller.publishNewEvaluation(
                evaluationDto
        );
    }

    private static <T> T getRandomElement(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }

        // If it's a List, we can access by index directly
        if (collection instanceof List<T> list) {
            int randomIndex = ThreadLocalRandom.current().nextInt(list.size());
            return list.get(randomIndex);
        }

        // For generic Collection, convert to List
        List<T> list = new ArrayList<>(collection);
        int randomIndex = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(randomIndex);
    }

}
