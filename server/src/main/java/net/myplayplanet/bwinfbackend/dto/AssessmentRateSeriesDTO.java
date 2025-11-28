package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** Time-series rate information either overall or per task. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRateSeriesDTO {
    private String taskId; // null for overall
    private String taskName;
    private String bucketSize; // minute | hour | day
    private List<RatePointDTO> points;
    private RangeDTO range;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RangeDTO {
        private String from;
        private String to;
    }
}
