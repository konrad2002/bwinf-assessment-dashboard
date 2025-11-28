package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Wrapper for overall and per-task rate series. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRateResponseDTO {
    private AssessmentRateSeriesDTO overall;
    private List<AssessmentRateSeriesDTO> byTask;
    private String generatedAt; // ISO 8601 UTC timestamp
}
