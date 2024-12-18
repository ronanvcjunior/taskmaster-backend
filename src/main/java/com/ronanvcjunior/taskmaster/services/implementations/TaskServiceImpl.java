package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.domains.RequestContext;
import com.ronanvcjunior.taskmaster.domains.TaskSpecification;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static com.ronanvcjunior.taskmaster.utils.TaskUtils.createTaskEntity;
import static com.ronanvcjunior.taskmaster.utils.TaskUtils.fromTaskEntity;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createTask(String name, BigDecimal cost, ZonedDateTime paymentDeadline) {
        Long userId = RequestContext.getUserId();

        Integer order = this.taskRepository.findMaxOrderByUserId(userId).orElse(0) + 1;

        UserEntity userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User não encontrado"));

        TaskEntity task = createTaskEntity(name, cost, paymentDeadline, order, userEntity);

        this.taskRepository.save(task);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TaskResponse getTask(String taskId) {
        Long userId = RequestContext.getUserId();

        TaskEntity task = this.taskRepository.findTaskEntityByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        return fromTaskEntity(task);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<TaskResponse> getAllTasks() {
        Long userId = RequestContext.getUserId();

        List<TaskEntity> tasksEntities = this.taskRepository.findTasksEntitiesByUserId(userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));



        return tasksEntities.stream()
                .map(TaskUtils::fromTaskEntity)
                .toList();
    }

    @Override
    public Page<TaskResponse> getTasks(int page, int size, String sortField, String sortOrder, String filters) {
        Long userId = RequestContext.getUserId();

        Sort sort = sortOrder != null && sortOrder.equalsIgnoreCase("DESC")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<TaskEntity> spec = TaskSpecification.byFilters(filters, userId);

        Page<TaskEntity> tasks = this.taskRepository.findAll(spec, pageable);

        if (tasks.isEmpty()) {
            return Page.empty(pageable);
        }


        return tasks.map(TaskUtils::fromTaskEntity);
    }


    @Override
    public void moveTaskOrder(String taskId, Boolean moveUp) {
        Long userId = RequestContext.getUserId();

        TaskEntity taskMoved = this.taskRepository.findTaskEntityByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        Integer taskOrder = taskMoved.getOrder();

        TaskEntity taskAdjacent;
        if (moveUp) {
            taskAdjacent = this.taskRepository.findTaskEntityByOrderAndUserId(taskOrder + 1, userId)
                    .orElseThrow(() -> new ApiException("Task não encontrada"));

            taskAdjacent.setOrder(-taskAdjacent.getOrder());
            this.taskRepository.save(taskAdjacent);

            taskMoved.setOrder(taskMoved.getOrder() + 1);
            this.taskRepository.save(taskMoved);
        }
        else {
            taskAdjacent = this.taskRepository.findTaskEntityByOrderAndUserId(taskOrder - 1, userId)
                    .orElseThrow(() -> new ApiException("Task não encontrada"));

            taskAdjacent.setOrder(-taskAdjacent.getOrder());
            this.taskRepository.save(taskAdjacent);

            taskMoved.setOrder(taskMoved.getOrder() - 1);
            this.taskRepository.save(taskMoved);
        }

        taskAdjacent.setOrder(taskOrder);
        this.taskRepository.save(taskAdjacent);
    }

    @Override
    public void setTaskOrder(String taskId, Integer order) {
        Long userId = RequestContext.getUserId();

        TaskEntity taskMoved = this.taskRepository.findTaskEntityByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        Integer taskOrder = taskMoved.getOrder();

        TaskEntity taskAdjacent = this.taskRepository.findTaskEntityByOrderAndUserId(order, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        taskAdjacent.setOrder(-taskAdjacent.getOrder());
        this.taskRepository.save(taskAdjacent);

        taskMoved.setOrder(order);
        this.taskRepository.save(taskMoved);

        taskAdjacent.setOrder(taskOrder);
        this.taskRepository.save(taskAdjacent);
    }

    @Override
    public void updateTask(String taskId, String name, BigDecimal cost, ZonedDateTime paymentDeadline) {
        Long userId = RequestContext.getUserId();

        TaskEntity task = this.taskRepository.findTaskEntityByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        task.setName(name);
        task.setCost(cost);
        task.setPaymentDeadline(paymentDeadline);

        this.taskRepository.save(task);
    }

    @Override
    public void deleteTask(String taskId) {
        Long userId = RequestContext.getUserId();

        TaskEntity task = this.taskRepository.findTaskEntityByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ApiException("Task não encontrada"));

        Integer orderTaskDelete = task.getOrder();

        this.taskRepository.delete(task);

        List<TaskEntity> tasks = this.taskRepository.findTaskEntitiesByOrderGreaterThanAndUserId(orderTaskDelete, userId)
                .orElse(List.of());

        for (TaskEntity taskEntity : tasks) {
            taskEntity.setOrder(taskEntity.getOrder() - 1);
        }

        this.taskRepository.saveAll(tasks);
    }
}
