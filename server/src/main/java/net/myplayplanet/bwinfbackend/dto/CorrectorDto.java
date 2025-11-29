package net.myplayplanet.bwinfbackend.dto;

import lombok.Value;
import net.myplayplanet.bwinfbackend.model.Corrector;

import java.io.Serializable;

/**
 * DTO for {@link Corrector}
 */
@Value
public class CorrectorDto implements Serializable {
    Long id;
    String shortName;
}