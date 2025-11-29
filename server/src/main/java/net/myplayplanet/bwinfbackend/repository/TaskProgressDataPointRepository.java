package net.myplayplanet.bwinfbackend.repository;

import net.myplayplanet.bwinfbackend.model.TaskProgressDataPoint;
import net.myplayplanet.bwinfbackend.model.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface TaskProgressDataPointRepository extends JpaRepository<TaskProgressDataPoint, Long> {

    Optional<TaskProgressDataPoint> findFirstByCorrectionContext_IdAndTaskTypeAndTaskNumberOrderByCreatedAtDesc(
            @NonNull Long id,
            @NonNull TaskType taskType,
            @NonNull Integer taskNumber
    );

}
