package com.ronanvcjunior.taskmaster.services.implementations;

import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.entities.TaskEntity;
import com.ronanvcjunior.taskmaster.entities.UserEntity;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import com.ronanvcjunior.taskmaster.repositories.TaskRepository;
import com.ronanvcjunior.taskmaster.repositories.UserRepository;
import com.ronanvcjunior.taskmaster.services.TaskService;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static com.ronanvcjunior.taskmaster.utils.TaskUtils.createTaskEntity;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public void createTask(String name, BigDecimal cost, ZonedDateTime paymentDeadline, User user) {
        Integer order = this.taskRepository.findMaxOrderByUserId(user.id()).orElse(0) + 1;

        UserEntity userEntity = this.userRepository.findById(user.id()).orElseThrow(
                () -> new ApiException("User não encontrado")
        );

        TaskEntity task = createTaskEntity(name, cost, paymentDeadline, order, userEntity);

        this.taskRepository.save(task);
    }
}
