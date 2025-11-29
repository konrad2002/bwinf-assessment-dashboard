package net.myplayplanet.bwinfbackend.repository;

import net.myplayplanet.bwinfbackend.model.TaskProgressDataPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskProgressDataPointRepository extends JpaRepository<TaskProgressDataPoint, Long> {
}
