package com.ronanvcjunior.taskmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.AlternativeJdkIdGenerator;

import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public abstract class Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_key_seq")
    @SequenceGenerator(name = "primary_key_seq", sequenceName = "primary_key_seq", allocationSize = 1)
    private Long id;

    private String referenceId = new AlternativeJdkIdGenerator().generateId().toString();

    @NotNull
    private Long createdBy;

    @NotNull
    private Long updatedBy;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    public void beforePersist() {
        Long userId = RequestContext.getUserId();
        if (userId == null) {
            throw new ApiException("Não é possível persistir a entidade sem o ID do usuário no contexto da requisição para este thread");
        }

        setCreatedAt(ZonedDateTime.now());
        setCreatedBy(userId);

        setUpdatedAt(ZonedDateTime.now());
        setUpdatedBy(userId);
    }

    @PreUpdate
    public void beforeUpdate() {
        Long userId = RequestContext.getUserId();
        if (userId == null) {
            throw new ApiException("Não é possível atualizar a entidade sem o ID do usuário no contexto da requisição para este thread");
        }

        setUpdatedAt(ZonedDateTime.now());
        setUpdatedBy(userId);
    }
}
