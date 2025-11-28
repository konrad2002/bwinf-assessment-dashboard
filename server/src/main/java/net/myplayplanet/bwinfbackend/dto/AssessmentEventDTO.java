package net.myplayplanet.bwinfbackend.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors client AssessmentEventDTO (see Angular model).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentEventDTO {
    private String id;
    private String submissionId;
    private String taskId;
    private String assessorId;
    private AssessmentType assessmentType;
    private int points;
    /** ISO 8601 UTC timestamp */
    private String timestamp;

    public enum AssessmentType {
        FIRST("first"),
        SECOND("second");

        private final String json;

        AssessmentType(String json) {
            this.json = json;
        }

        @JsonValue
        public String getJson() {
            return json;
        }
    }
}
