package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Aggregate progress across all tasks. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverallProgressDTO {
    private int totalTasks;
    private int totalSubmissions;
    private int firstAssessmentsDone;
    private int secondAssessmentsDone;
    private int totalAssessmentsDone;
    private int firstMissing;
    private int secondMissing;
    private int totalMissing;
    private TaskProgressDTO.Percentages percentages;
    private List<TaskProgressDTO> byTask;
    private String updatedAt; // ISO 8601 UTC
}
