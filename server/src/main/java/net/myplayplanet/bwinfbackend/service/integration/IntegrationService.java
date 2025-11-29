package net.myplayplanet.bwinfbackend.service.integration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.dto.EvaluationDto;
import net.myplayplanet.bwinfbackend.model.Evaluation;
import net.myplayplanet.bwinfbackend.repository.CorrectionContextRepository;
import net.myplayplanet.bwinfbackend.repository.CorrectorRepository;
import net.myplayplanet.bwinfbackend.repository.EvaluationRepository;
import net.myplayplanet.bwinfbackend.service.CorrectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IntegrationService {

    private final EvaluationRepository evaluationRepository;
    private final CorrectorService correctorService;
    private final CorrectionContextRepository correctionContextRepository;


    public void insertEvaluation(EvaluationDto evaluationDto) {

        Evaluation evaluation = new Evaluation();

        evaluation.setEvaluationType(evaluationDto.getEvaluationType());
        evaluation.setCorrector(this.correctorService.getOrCreate(evaluationDto.getCorrector().getShortName()));
        evaluation.setTaskNumber(evaluationDto.getTaskNumber());
        evaluation.setCreatedAt(LocalDateTime.now());
        evaluation.setCorrectionContext(this.correctionContextRepository.getReferenceById(evaluationDto.getCorrectionContextId()));
        evaluation.setPointsDeducted(evaluationDto.getPointsDeducted());
        evaluation.setTaskType(evaluationDto.getTaskType());
        evaluation.setSubmissionId(evaluationDto.getSubmissionId());
        System.out.println(evaluation);
        this.evaluationRepository.save(evaluation);
    }

}
