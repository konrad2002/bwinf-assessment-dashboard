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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year")
    private LocalDateTime year;

    @Enumerated(EnumType.STRING)
    @Column(name = "round", nullable = false)
    private Round round;


    @Override
    public String toString() {
        return "CorrectionContext{" +
                "id=" + id +
                ", year=" + year +
                ", round=" + round +
                '}';
    }
}