package net.myplayplanet.bwinfbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "evaluation")
public class Evaluation {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "corrector_id")
  private Corrector corrector;

  @ManyToOne
  @JoinColumn(name = "correction_context_id")
  private CorrectionContext correctionContext;

  @Enumerated(EnumType.STRING)
  @Column(name = "evaluation_type")
  private EvaluationType evaluationType;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

}