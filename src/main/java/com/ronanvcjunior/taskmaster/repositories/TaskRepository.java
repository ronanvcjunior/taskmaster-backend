package com.ronanvcjunior.taskmaster.repositories;

import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.ronanvcjunior.taskmaster.constants.QueryConstants.*;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    @Query(value =  CREATE_TASK, nativeQuery = true)
    Optional<Integer> findMaxOrderByUserId(@Param("userId") Long userId);

    @Query(value =  SELECT_TASK, nativeQuery = true)
    Optional<TaskEntity> findTaskEntityByTaskIdAndUserId(@Param("taskId") String taskId, @Param("userId") Long userId);

    @Query(value =  SELECT_ALL_TASKS, nativeQuery = true)
    Optional<List<TaskEntity>> findTasksEntitiesByUserId(Long userId);

    @Query(value =  SELECT_TASK_BY_ORDER, nativeQuery = true)
    Optional<TaskEntity> findTaskEntityByOrderAndUserId(@Param("order") Integer order, @Param("userId") Long userId);


    @Query(value =  SELECT_TASKS_BY_ORDER_GREATER, nativeQuery = true)
    Optional<List<TaskEntity>> findTaskEntitiesByOrderGreaterThanAndUserId(@Param("order") Integer order, @Param("userId") Long userId);
}
