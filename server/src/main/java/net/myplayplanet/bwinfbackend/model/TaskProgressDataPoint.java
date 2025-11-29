package net.myplayplanet.bwinfbackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "task_progress_data_point")
public class TaskProgressDataPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "correction_context_id", nullable = false)
    private CorrectionContext correctionContext;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;

    @Column(name = "task_number", nullable = false)
    private Integer taskNumber;

    @Column(name = "open", nullable = false)
    private Integer open;

    @Column(name = "done", nullable = false)
    private Integer done;

    @Column(name = "only_first_done", nullable = false)
    private Integer onlyFirstDone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}