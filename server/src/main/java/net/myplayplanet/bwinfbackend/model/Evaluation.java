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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @Column(name = "task_number", nullable = false)
    private Integer taskNumber;

    @Column(name = "points_deducted", nullable = false)
    private Integer pointsDeducted;

    @Override
    public String toString() {
        return "Evaluation{" +
                "id=" + id +
                ", corrector=" + corrector +
                ", correctionContext=" + correctionContext.getId() +
                ", evaluationType=" + evaluationType +
                ", createdAt=" + createdAt +
                ", taskType=" + taskType +
                ", taskNumber=" + taskNumber +
                ", pointsDeducted=" + pointsDeducted +
                '}';
    }
}