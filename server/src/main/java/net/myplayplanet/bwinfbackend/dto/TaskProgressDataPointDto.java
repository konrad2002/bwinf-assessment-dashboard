package net.myplayplanet.bwinfbackend.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import net.myplayplanet.bwinfbackend.model.TaskType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.myplayplanet.bwinfbackend.model.TaskProgressDataPoint}
 */
@Value
public class TaskProgressDataPointDto implements Serializable {
    TaskType taskType;
    Integer taskNumber;
    ProgressDataPointDto progressDataPointDto;
    LocalDateTime createdAt;
}