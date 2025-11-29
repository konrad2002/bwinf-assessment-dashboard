package net.myplayplanet.bwinfbackend.service.statistic;

import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.dto.OverallProgressDTO;
import net.myplayplanet.bwinfbackend.dto.TaskProgressDTO;
import net.myplayplanet.bwinfbackend.model.Evaluation;
import net.myplayplanet.bwinfbackend.model.EvaluationType;
import net.myplayplanet.bwinfbackend.model.TaskType;
import net.myplayplanet.bwinfbackend.repository.EvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssessmentService {

    private final EvaluationRepository evaluationRepository;

    public TaskProgressDTO calculateTaskProgressDto(Long correctionContextId, TaskType taskType, Integer taskId) {
        Set<Evaluation> evaluations = this.evaluationRepository
                .findByTaskTypeAndTaskNumberAndCorrectionContext_Id(taskType, taskId, correctionContextId);

        return getTaskProgressDTO(evaluations, taskType, taskId);
    }

    public OverallProgressDTO calculateOverallProgress(Long correctionContextId) {
        Set<Evaluation> evaluations = this.evaluationRepository.findByCorrectionContext_Id(correctionContextId);

        int totalTasks = (int) evaluations.stream()
                .map(e -> e.getTaskType() + "-" + e.getTaskNumber())
                .distinct()
                .count();

        Result result = getResult(evaluations);

        String updatedAt = evaluations.stream()
                .map(Evaluation::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .map(LocalDateTime::toString)
                .orElse(Instant.now().toString());

        // Build per-task progress
        List<TaskProgressDTO> byTask = evaluations.stream()
                .collect(Collectors.groupingBy(e -> e.getTaskType() + "-" + e.getTaskNumber()))
                .entrySet()
                .stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("-");
                    TaskType type = TaskType.valueOf(parts[0]);
                    Integer number = Integer.valueOf(parts[1]);
                    Set<Evaluation> taskEvals = new HashSet<>(entry.getValue());
                    return calculateTaskProgressDto(taskEvals, type, number);
                })
                .collect(Collectors.toList());

        TaskProgressDTO.Percentages pct = new TaskProgressDTO.Percentages(result.firstPercentage(), result.secondPercentage(), result.totalPercentage());

        return OverallProgressDTO.builder()
                .totalTasks(totalTasks)
                .totalSubmissions(result.totalSubmissions())
                .firstAssessmentsDone(result.firstAssessmentsDone())
                .secondAssessmentsDone(result.secondAssessmentsDone())
                .totalAssessmentsDone(result.totalAssessmentsDone())
                .firstMissing(result.firstMissing())
                .secondMissing(result.secondMissing())
                .totalMissing(result.totalMissing())
                .percentages(pct)
                .byTask(byTask)
                .updatedAt(updatedAt)
                .build();
    }

    private static Result getResult(Set<Evaluation> evaluations) {
        int totalSubmissions = evaluations.size();
        int firstAssessmentsDone = (int) evaluations.stream().filter(e -> e.getEvaluationType() == EvaluationType.FIRST).count();
        int secondAssessmentsDone = (int) evaluations.stream().filter(e -> e.getEvaluationType() == EvaluationType.SECOND).count();
        int totalAssessmentsDone = firstAssessmentsDone + secondAssessmentsDone;

        int firstMissing = totalSubmissions - firstAssessmentsDone;
        int secondMissing = totalSubmissions - secondAssessmentsDone;
        int totalMissing = totalSubmissions * 2 - totalAssessmentsDone;

        double firstPercentage = totalSubmissions == 0 ? 0.0 : ((double) firstAssessmentsDone / totalSubmissions) * 100;
        double secondPercentage = totalSubmissions == 0 ? 0.0 : ((double) secondAssessmentsDone / totalSubmissions) * 100;
        double totalPercentage = totalSubmissions == 0 ? 0.0 : ((double) totalAssessmentsDone / (totalSubmissions * 2)) * 100;
        return new Result(totalSubmissions, firstAssessmentsDone, secondAssessmentsDone, totalAssessmentsDone, firstMissing, secondMissing, totalMissing, firstPercentage, secondPercentage, totalPercentage);
    }

    private record Result(int totalSubmissions,
                          int firstAssessmentsDone,
                          int secondAssessmentsDone,
                          int totalAssessmentsDone,
                          int firstMissing,
                          int secondMissing,
                          int totalMissing,
                          double firstPercentage,
                          double secondPercentage,
                          double totalPercentage) {
    }

    // Helper method to calculate TaskProgressDTO from a Set<Evaluation>
    private TaskProgressDTO calculateTaskProgressDto(Set<Evaluation> evaluations, TaskType taskType, Integer taskId) {
        return getTaskProgressDTO(evaluations, taskType, taskId);
    }

    private TaskProgressDTO getTaskProgressDTO(Set<Evaluation> evaluations, TaskType taskType, Integer taskId) {
        Result result = getResult(evaluations);

        String updatedAt = evaluations.stream()
                .map(Evaluation::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .map(LocalDateTime::toString)
                .orElse(null);

        return TaskProgressDTO.builder()
                .taskId(taskId.toString())
                .taskName(taskType.name())
                .totalSubmissions(result.totalSubmissions())
                .firstAssessmentsDone(result.firstAssessmentsDone())
                .secondAssessmentsDone(result.secondAssessmentsDone())
                .totalAssessmentsDone(result.totalAssessmentsDone())
                .firstMissing(result.firstMissing())
                .secondMissing(result.secondMissing())
                .totalMissing(result.totalMissing())
                .percentages(new TaskProgressDTO.Percentages(result.firstPercentage(), result.secondPercentage(), result.totalPercentage()))
                .updatedAt(updatedAt)
                .build();
    }


}
