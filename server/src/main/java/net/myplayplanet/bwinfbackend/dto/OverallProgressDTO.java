package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Aggregate progress across all tasks.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverallProgressDTO {
    private GlobalProgressDataPointDto globalProgressDataPointDto;
    private TaskProgressDTO.Percentages percentages;
    private List<TaskProgressDataPointDto> byTask;
    private String updatedAt; // ISO 8601 UTC
}
