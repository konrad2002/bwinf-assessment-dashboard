package net.myplayplanet.bwinfbackend.repository;

import net.myplayplanet.bwinfbackend.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
