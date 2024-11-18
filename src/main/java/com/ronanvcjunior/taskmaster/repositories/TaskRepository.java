package com.ronanvcjunior.taskmaster.repositories;

import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.ronanvcjunior.taskmaster.constants.QueryConstants.CREATE_TASK;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    @Query(value =  CREATE_TASK, nativeQuery = true)
    Optional<Integer> findMaxOrderByUserId(@Param("userId") Long userId);
}
