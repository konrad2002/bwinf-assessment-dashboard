package net.myplayplanet.bwinfbackend.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class ProgressDataPointDto implements Serializable {
    Integer open;           // number of tasks not started
    Integer done;           // number of tasks fully evaluated
    Integer onlyFirstDone;  // number of tasks with only first evaluation done
    Integer inProgress;     // number of tasks currently claimed
    Integer totalSubmissions; // total submissions per task
    Integer totalRequiredEvaluations; // derived
    Integer firstMissing;           // derived
    Integer secondMissing;          // derived

    public ProgressDataPointDto(Integer open,
                                Integer done,
                                Integer onlyFirstDone,
                                Integer inProgress,
                                Integer totalSubmissions) {
        this.open = open;
        this.done = done;
        this.onlyFirstDone = onlyFirstDone;
        this.inProgress = inProgress;
        this.totalSubmissions = totalSubmissions;

        this.totalRequiredEvaluations = totalSubmissions * 2;
        this.firstMissing = totalSubmissions - done - onlyFirstDone;
        this.secondMissing = totalSubmissions - done;
    }


}
