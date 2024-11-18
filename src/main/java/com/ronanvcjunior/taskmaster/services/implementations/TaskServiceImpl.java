package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.dtos.response.TaskResponse;
import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.repositories.TaskRepository;
import com.ronanvcjunior.taskmaster.repositories.UserRepository;
import com.ronanvcjunior.taskmaster.services.TaskService;
import com.ronanvcjunior.taskmaster.services.UserService;
import com.ronanvcjunior.taskmaster.utils.TaskUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static com.ronanvcjunior.taskmaster.utils.TaskUtils.createTaskEntity;
import static com.ronanvcjunior.taskmaster.utils.TaskUtils.fromTaskEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public void createTask(String name, BigDecimal cost, ZonedDateTime paymentDeadline) {
        Long userId = RequestContext.getUserId();

        Integer order = this.taskRepository.findMaxOrderByUserId(userId).orElse(0) + 1;

        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User não encontrado"));

        TaskEntity task = createTaskEntity(name, cost, paymentDeadline, order, userEntity);

        this.taskRepository.save(task);
    }

    @Override
    public TaskResponse getTask(String taskId) {
        Long userId = RequestContext.getUserId();

        TaskEntity task = this.taskRepository.findTaskEntityByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        return fromTaskEntity(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        Long userId = RequestContext.getUserId();

        List<TaskEntity> tasksEntities = this.taskRepository.findTasksEntitiesByUserId(userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));



        return tasksEntities.stream()
                .map(TaskUtils::fromTaskEntity)
                .toList();
    }
}
