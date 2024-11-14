package com.ronanvcjunior.taskmaster.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@JsonInclude(NON_DEFAULT)
public class TaskEntity extends  Auditable {

    @Column(updatable = false, unique = true, nullable = false)
    private String taskId;

    @Column(nullable = false)
    private String name;

    private BigDecimal cost;

    @Column(name = "payment_deadline")
    private ZonedDateTime paymentDeadline;

    @Column(name = "task_order", unique = true, nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_tasks_owner", foreignKeyDefinition = "foreign key /* FK */ (user_id) references users", value = ConstraintMode.CONSTRAINT)
    )
    private UserEntity owner;


}
