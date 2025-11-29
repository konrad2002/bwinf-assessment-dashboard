package net.myplayplanet.bwinfbackend.dto;

import lombok.Value;
import net.myplayplanet.bwinfbackend.model.TaskType;

import java.io.Serializable;

@Value
public class NewEvaluationEventDto implements Serializable {

    TaskType taskType;
    Integer taskNumber;
    CorrectorDto correctorDto;
    CombinedProgressDataPointDto combinedProgressDataPointDto;

}
