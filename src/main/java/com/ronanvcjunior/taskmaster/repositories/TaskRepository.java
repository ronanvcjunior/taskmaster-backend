package com.ronanvcjunior.taskmaster.repositories;

import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ronanvcjunior.taskmaster.constants.QueryConstants.CREATE_TASK;
import static com.ronanvcjunior.taskmaster.constants.QueryConstants.SELECT_TASK;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query(value =  CREATE_TASK, nativeQuery = true)
    Optional<Integer> findMaxOrderByUserId(@Param("userId") Long userId);

    @Query(value =  SELECT_TASK, nativeQuery = true)
    Optional<TaskEntity> findTaskEntityByTaskIdAndUserId(@Param("taskId") String taskId, @Param("userId") Long userId);
}
