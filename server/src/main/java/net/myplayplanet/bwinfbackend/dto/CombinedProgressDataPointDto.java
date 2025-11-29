package net.myplayplanet.bwinfbackend.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class CombinedProgressDataPointDto implements Serializable {
    GlobalProgressDataPointDto globalProgressDataPoint;
    List<TaskProgressDataPointDto> taskProgressDataPointDtos;
}
