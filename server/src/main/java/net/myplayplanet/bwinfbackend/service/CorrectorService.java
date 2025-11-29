package net.myplayplanet.bwinfbackend.service;


import lombok.RequiredArgsConstructor;
import net.myplayplanet.bwinfbackend.model.Corrector;
import net.myplayplanet.bwinfbackend.repository.CorrectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CorrectorService {

    private final CorrectorRepository correctorRepository;

    public Corrector getOrCreate(String shortName) {
        return this.correctorRepository.findByShortName(shortName)
                .orElseGet(() -> this.correctorRepository.save(new Corrector(null, shortName)));
    }
}
