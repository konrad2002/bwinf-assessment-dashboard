package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A single bucket of assessment completion counts. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatePointDTO {
    private String timestamp; // ISO 8601 bucket start/end marker
    private int count;
    private int cumulativeCount;
    private Double ratePerHour; // optional
}
