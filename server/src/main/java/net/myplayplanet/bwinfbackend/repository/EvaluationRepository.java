package net.myplayplanet.bwinfbackend.repository;

import net.myplayplanet.bwinfbackend.model.Evaluation;
import net.myplayplanet.bwinfbackend.model.EvaluationType;
import net.myplayplanet.bwinfbackend.model.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {


    @Query("select e from Evaluation e where e.taskType = ?1 and e.taskNumber = ?2 and e.correctionContext.id = ?3")
    Set<Evaluation> findByTaskTypeAndTaskNumberAndCorrectionContext_Id(@NonNull TaskType taskType, @NonNull Integer taskNumber, @NonNull Long id);

    @Query("select e from Evaluation e where e.correctionContext.id = ?1")
    Set<Evaluation> findByCorrectionContext_Id(@NonNull Long id);



    boolean existsByCorrector_IdAndEvaluationTypeAndCorrectionContext_IdAndTaskTypeAndTaskNumberAndSubmissionId(@NonNull Long correctorId,
                                                                                                                @NonNull EvaluationType evaluationType,
                                                                                                                @NonNull Long contextId,
                                                                                                                @NonNull TaskType taskType,
                                                                                                                @NonNull Integer taskNumber,
                                                                                                                @NonNull String submissionId);

    @Query("""
        SELECT e
        FROM Evaluation e
        WHERE e.correctionContext.id = :contextId
          AND e.createdAt = (
              SELECT MAX(e2.createdAt)
              FROM Evaluation e2
              WHERE e2.submissionId = e.submissionId
                AND e2.taskNumber = e.taskNumber
                AND e2.taskType = e.taskType
                AND e2.correctionContext.id = :contextId
          )
    """)
    Set<Evaluation> findLatestEvaluationsByCorrectionContext(@Param("contextId") Long contextId);

    @Query("""
        SELECT COUNT(DISTINCT e.submissionId)
        FROM Evaluation e
        WHERE e.correctionContext.id = :contextId
          AND e.taskNumber = :taskNumber
          AND e.taskType = :taskType
    """)
    long countDistinctSubmissionIds(
            @Param("contextId") Long contextId,
            @Param("taskNumber") Integer taskNumber,
            @Param("taskType") TaskType taskType
    );
}

