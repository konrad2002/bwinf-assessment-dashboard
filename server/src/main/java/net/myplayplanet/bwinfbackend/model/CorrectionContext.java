package net.myplayplanet.bwinfbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "correction_context")
public class CorrectionContext {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year")
    private LocalDateTime year;

    @Enumerated(EnumType.STRING)
    @Column(name = "round", nullable = false)
    private Round round;

}