package com.ronanvcjunior.taskmaster.contollers;

import com.ronanvcjunior.taskmaster.dtos.Response;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.dtos.requests.TaskRequest;
import com.ronanvcjunior.taskmaster.dtos.requests.UserRequest;
import com.ronanvcjunior.taskmaster.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.ronanvcjunior.taskmaster.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = { "/task" } )
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('task:create') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> saveTask(@AuthenticationPrincipal User user, @RequestBody @Valid TaskRequest task, HttpServletRequest request) {
        taskService.createTask(task.name(), task.cost(), task.paymentDeadline(), user);

        return ResponseEntity.created(URI.create("")).body(getResponse(request, emptyMap(), "Atividade criada", CREATED));
    }
}
