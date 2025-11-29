package net.myplayplanet.bwinfbackend.service.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.myplayplanet.bwinfbackend.dto.*;
import net.myplayplanet.bwinfbackend.model.Evaluation;
import net.myplayplanet.bwinfbackend.model.EvaluationType;
import net.myplayplanet.bwinfbackend.model.TaskProgressDataPoint;
import net.myplayplanet.bwinfbackend.model.TaskType;
import net.myplayplanet.bwinfbackend.repository.CorrectionContextRepository;
import net.myplayplanet.bwinfbackend.repository.EvaluationRepository;
import net.myplayplanet.bwinfbackend.repository.TaskProgressDataPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssessmentService {

    private final EvaluationRepository evaluationRepository;
    private final TaskProgressDataPointRepository taskProgressDataPointRepository;
    private final CorrectionContextRepository correctionContextRepository;

    public CombinedProgressDataPointDto calculateCombinedProgressDataPointDto(Long correctionContextId) {

        List<TaskProgressDataPointDto> list = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            list.add(calculateTaskProgressDataPointDto(correctionContextId, TaskType.JWINF, i));
        }
        for (int i = 1; i <= 5; i++) {
            list.add(calculateTaskProgressDataPointDto(correctionContextId, TaskType.BWINF, i));
        }
        return new CombinedProgressDataPointDto(
                calculateGlobalTaskProgressDataPointDto(correctionContextId),
                list
        );
    }

    public GlobalProgressDataPointDto calculateGlobalTaskProgressDataPointDto(Long correctionContextId) {
        int open = 0, done = 0, onlyFirstDone = 0, inProgress = 0, totalSubmissions = 0;
        LocalDateTime lastUpdated = LocalDateTime.MIN;

        for (int i = 1; i <= 2; i++) {
            TaskProgressDataPointDto progress = this.calculateTaskProgressDataPointDto(correctionContextId, TaskType.JWINF, i);
            ProgressDataPointDto progressDataPointDto = progress.getProgressDataPointDto();
            open += progressDataPointDto.getOpen();
            done += progressDataPointDto.getDone();
            onlyFirstDone += progressDataPointDto.getOnlyFirstDone();
            inProgress += progressDataPointDto.getInProgress();
            totalSubmissions += progressDataPointDto.getTotalSubmissions();

            if (progress.getCreatedAt().isAfter(lastUpdated)) {
                lastUpdated = progress.getCreatedAt();
            }
        }

        for (int i = 1; i <= 5; i++) {
            TaskProgressDataPointDto progress = this.calculateTaskProgressDataPointDto(correctionContextId, TaskType.BWINF, i);
            ProgressDataPointDto progressDataPointDto = progress.getProgressDataPointDto();
            open += progressDataPointDto.getOpen();
            done += progressDataPointDto.getDone();
            onlyFirstDone += progressDataPointDto.getOnlyFirstDone();
            inProgress += progressDataPointDto.getInProgress();
            totalSubmissions += progressDataPointDto.getTotalSubmissions();

            if (progress.getCreatedAt().isAfter(lastUpdated)) {
                lastUpdated = progress.getCreatedAt();
            }
        }
        ProgressDataPointDto progressDataPointDto = new ProgressDataPointDto(open, done, onlyFirstDone, inProgress, totalSubmissions);
        return new GlobalProgressDataPointDto(progressDataPointDto,
                lastUpdated);
    }

    public TaskProgressDataPointDto calculateTaskProgressDataPointDto(Long correctionContextId, TaskType taskType, Integer taskNumber) {
        Optional<TaskProgressDataPoint> optional = this.taskProgressDataPointRepository.findFirstByCorrectionContext_IdAndTaskTypeAndTaskNumberOrderByCreatedAtDesc(correctionContextId, taskType, taskNumber);

        if (optional.isEmpty()) {
            return new TaskProgressDataPointDto(taskType, taskNumber, new ProgressDataPointDto(
                    0, 0, 0, 0, 0
            ), LocalDateTime.MIN);
        }

        TaskProgressDataPoint taskProgressDataPoint = optional.get();
        ProgressDataPointDto taskProgressDataPointDto = new ProgressDataPointDto(taskProgressDataPoint.getOpen(),
                taskProgressDataPoint.getDone(),
                taskProgressDataPoint.getOnlyFirstDone(),
                taskProgressDataPoint.getInProgress(),
                taskProgressDataPoint.getOpen() + taskProgressDataPoint.getDone() + taskProgressDataPoint.getOnlyFirstDone() + taskProgressDataPoint.getInProgress()
        );

        log.info("ProgressData for task {} {} {}", taskType, taskNumber, taskProgressDataPointDto);
        return new TaskProgressDataPointDto(taskType,
                taskNumber,
                taskProgressDataPointDto,
                taskProgressDataPoint.getCreatedAt()
        );
    }

    private int countTotalSubmissions(Long contextId) {

        int counter = 0;

        for (int i = 1; i <= 2; i++) {
            counter += (int) this.evaluationRepository.countDistinctSubmissionIds(contextId, i, TaskType.JWINF);
        }

        for (int i = 1; i <= 5; i++) {
            counter += (int) this.evaluationRepository.countDistinctSubmissionIds(contextId, i, TaskType.BWINF);
        }

        return counter;

    }

    public void saveNewProgressDataPoint(Long correctionContextId) {
        Set<Evaluation> evaluations = this.evaluationRepository.findLatestEvaluationsByCorrectionContext(correctionContextId);

        // Build per-task progress
        List<TaskProgressDataPoint> byTask = evaluations.stream()
                .collect(Collectors.groupingBy(e -> e.getTaskType() + "-" + e.getTaskNumber()))
                .entrySet()
                .stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("-");
                    TaskType type = TaskType.valueOf(parts[0]);
                    Integer number = Integer.valueOf(parts[1]);
                    Set<Evaluation> taskEvals = new HashSet<>(entry.getValue());
                    return calculateTaskProgressPoint(correctionContextId, type, number, taskEvals);
                })
                .toList();

        this.taskProgressDataPointRepository.saveAll(byTask);
    }

    private TaskProgressDataPoint calculateTaskProgressPoint(Long contextId, TaskType taskType, Integer taskNumber, Set<Evaluation> evaluations) {
        Set<Evaluation> relevantEvaluations = evaluations.stream().filter(evaluation -> evaluation.getTaskType().equals(taskType) && Objects.equals(evaluation.getTaskNumber(), taskNumber)).collect(Collectors.toSet());

        int firstAssessmentsDone = (int) relevantEvaluations.stream().filter(e -> e.getEvaluationType() == EvaluationType.FIRST).count();
        int secondAssessmentsDone = (int) relevantEvaluations.stream().filter(e -> e.getEvaluationType() == EvaluationType.SECOND).count();
        int inProgress = (int) relevantEvaluations.stream().filter(e -> e.getEvaluationType() == EvaluationType.IN_PROGRESS).count();

        int totalSubmissions = (int) this.evaluationRepository.countDistinctSubmissionIds(contextId, taskNumber, taskType);

        LocalDateTime lastUpdated = LocalDateTime.MIN;
        for (Evaluation evaluation : relevantEvaluations) {
            if (evaluation.getCreatedAt().isAfter(lastUpdated)) {
                lastUpdated = evaluation.getCreatedAt();
            }
        }

        int open = totalSubmissions - firstAssessmentsDone - secondAssessmentsDone - inProgress;

        return new TaskProgressDataPoint(null, this.correctionContextRepository.getReferenceById(contextId),
                taskType,
                taskNumber,
                open,
                secondAssessmentsDone,
                firstAssessmentsDone,
                inProgress,
                lastUpdated
        );
    }


}
