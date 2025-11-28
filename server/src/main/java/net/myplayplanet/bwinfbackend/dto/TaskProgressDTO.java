package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Per-task progress stats. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskProgressDTO {
    private String taskId;
    private String taskName;
    private int totalSubmissions;
    private int firstAssessmentsDone;
    private int secondAssessmentsDone;
    private int totalAssessmentsDone;
    private int firstMissing;
    private int secondMissing;
    private int totalMissing;
    private Percentages percentages;
    private String updatedAt; // ISO 8601 UTC

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Percentages {
        private double first;
        private double second;
        private double total;
    }
}
