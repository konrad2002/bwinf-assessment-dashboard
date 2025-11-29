package net.myplayplanet.bwinfbackend.service.mock;

import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.dto.*;
import net.myplayplanet.bwinfbackend.model.TaskType;
import net.myplayplanet.bwinfbackend.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MockEventGenerator {


    private final EventService eventService;
    private final RandomizerService random;

    private static final Collection<String> corrector = List.of("paulf", "konradw");
    private final static Collection<TaskType> taskType = List.of(TaskType.BWINF, TaskType.JWINF);
    private final static Collection<Integer> taskNumbers = List.of(1, 2, 3, 4, 5);
    private final static Collection<Long> correctorId = List.of(1L, 2L, 3L);

    @Scheduled(fixedRate = 5000) // 5000 ms = 5 seconds
    public void generateEvents() {
        NewEvaluationEventDto event = new NewEvaluationEventDto(
                this.random.getRandomElement(taskType),
                this.random.getRandomElement(taskNumbers),
                new CorrectorDto(
                        this.random.getRandomElement(correctorId),
                        this.random.getRandomElement(corrector)
                ),
                new CombinedProgressDataPointDto(
                        new GlobalProgressDataPointDto(
                                new ProgressDataPointDto(10, 2, 6, 1, 19),
                                LocalDateTime.now()
                        ),
                        List.of()
                )
        );
        eventService.emitEvent(event);
    }
}
