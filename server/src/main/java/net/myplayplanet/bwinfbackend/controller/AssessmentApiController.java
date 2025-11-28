package net.myplayplanet.bwinfbackend.controller;

import net.myplayplanet.bwinfbackend.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * API controller implementing the endpoints defined in api.md.
 * NOTE: All responses are stubbed placeholders; replace with real service layer.
 */
@RestController
@RequestMapping("/api")
public class AssessmentApiController {

    @GetMapping("/progress/overall")
    public OverallProgressDTO getOverallProgress() {
        TaskProgressDTO.Percentages pct = TaskProgressDTO.Percentages.builder().first(0).second(0).total(0).build();
        return OverallProgressDTO.builder()
                .totalTasks(0)
                .totalSubmissions(0)
                .firstAssessmentsDone(0)
                .secondAssessmentsDone(0)
                .totalAssessmentsDone(0)
                .firstMissing(0)
                .secondMissing(0)
                .totalMissing(0)
                .percentages(pct)
                .byTask(Collections.emptyList())
                .updatedAt(Instant.now().toString())
                .build();
    }

    @GetMapping("/progress/tasks/{taskId}")
    public TaskProgressDTO getTaskProgress(@PathVariable String taskId) {
        return TaskProgressDTO.builder()
                .taskId(taskId)
                .taskName("Task " + taskId)
                .totalSubmissions(0)
                .firstAssessmentsDone(0)
                .secondAssessmentsDone(0)
                .totalAssessmentsDone(0)
                .firstMissing(0)
                .secondMissing(0)
                .totalMissing(0)
                .percentages(TaskProgressDTO.Percentages.builder().first(0).second(0).total(0).build())
                .updatedAt(Instant.now().toString())
                .build();
    }

    @GetMapping("/rates")
    public AssessmentRateResponseDTO getRates(@RequestParam String from,
                                              @RequestParam String to,
                                              @RequestParam(required = false, defaultValue = "hour") String bucket,
                                              @RequestParam(required = false) String tasks) {
        AssessmentRateSeriesDTO.RangeDTO range = AssessmentRateSeriesDTO.RangeDTO.builder().from(from).to(to).build();
        AssessmentRateSeriesDTO overall = AssessmentRateSeriesDTO.builder()
                .bucketSize(bucket)
                .points(Collections.emptyList())
                .range(range)
                .build();
        return AssessmentRateResponseDTO.builder()
                .overall(overall)
                .byTask(Collections.emptyList())
                .generatedAt(Instant.now().toString())
                .build();
    }

    @GetMapping("/rates/tasks/{taskId}")
    public AssessmentRateSeriesDTO getTaskRates(@PathVariable String taskId,
                                                @RequestParam String from,
                                                @RequestParam String to,
                                                @RequestParam(required = false, defaultValue = "hour") String bucket) {
        AssessmentRateSeriesDTO.RangeDTO range = AssessmentRateSeriesDTO.RangeDTO.builder().from(from).to(to).build();
        return AssessmentRateSeriesDTO.builder()
                .taskId(taskId)
                .taskName("Task " + taskId)
                .bucketSize(bucket)
                .points(Collections.emptyList())
                .range(range)
                .build();
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        // Send initial heartbeat
        LiveEventEnvelopeDTO<Object> heartbeat = LiveEventEnvelopeDTO.builder()
                .type("heartbeat")
                .eventId(UUID.randomUUID().toString())
                .emittedAt(Instant.now().toString())
                .payload(Collections.singletonMap("timestamp", Instant.now().toString()))
                .build();
        try {
            emitter.send(SseEmitter.event().id(heartbeat.getEventId()).name(heartbeat.getType()).data(heartbeat));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @GetMapping("/assessments")
    public AssessmentEventPageDTO listAssessments(@RequestParam(required = false) String from,
                                                  @RequestParam(required = false) String to,
                                                  @RequestParam(required = false) String taskId,
                                                  @RequestParam(required = false) String assessorId,
                                                  @RequestParam(required = false, defaultValue = "1") int page,
                                                  @RequestParam(required = false, defaultValue = "20") int pageSize) {
        // Stub single example event if parameters provided, else empty.
        List<AssessmentEventDTO> items;
        if (taskId != null) {
            items = List.of(AssessmentEventDTO.builder()
                    .id(UUID.randomUUID().toString())
                    .submissionId("submission-1")
                    .taskId(taskId)
                    .assessorId(assessorId != null ? assessorId : "assessor-1")
                    .assessmentType(AssessmentEventDTO.AssessmentType.FIRST)
                    .points(0)
                    .timestamp(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString())
                    .build());
        } else {
            items = Collections.emptyList();
        }
        return AssessmentEventPageDTO.builder()
                .items(items)
                .page(page)
                .pageSize(pageSize)
                .totalItems(items.size())
                .build();
    }
}
