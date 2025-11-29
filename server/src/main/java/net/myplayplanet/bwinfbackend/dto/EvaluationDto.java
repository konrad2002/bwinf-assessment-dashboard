package net.myplayplanet.bwinfbackend.dto;

import lombok.Builder;
import lombok.Value;
import net.myplayplanet.bwinfbackend.model.EvaluationType;
import net.myplayplanet.bwinfbackend.model.TaskType;

import java.io.Serializable;

/**
 * DTO for {@link net.myplayplanet.bwinfbackend.model.Evaluation}
 */
@Builder
@Value
public class EvaluationDto implements Serializable {
    CorrectorDto corrector;
    Long correctionContextId;
    EvaluationType evaluationType;
    TaskType taskType;
    Integer taskNumber;
    Integer pointsDeducted;
}