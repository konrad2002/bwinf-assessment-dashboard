package net.myplayplanet.bwinfbackend.repository;


import net.myplayplanet.bwinfbackend.model.Corrector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface CorrectorRepository extends JpaRepository<Corrector, Long> {
    @Query("select c from Corrector c where c.shortName = ?1")
    Optional<Corrector> findByShortName(@NonNull String shortName);
}
