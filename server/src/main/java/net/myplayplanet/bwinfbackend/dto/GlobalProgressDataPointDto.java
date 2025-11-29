package net.myplayplanet.bwinfbackend.dto;

import lombok.Value;
import net.myplayplanet.bwinfbackend.model.TaskType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.myplayplanet.bwinfbackend.model.TaskProgressDataPoint}
 */
@Value
public class GlobalProgressDataPointDto implements Serializable {
    ProgressDataPointDto progressDataPointDto;
    LocalDateTime createdAt;
}