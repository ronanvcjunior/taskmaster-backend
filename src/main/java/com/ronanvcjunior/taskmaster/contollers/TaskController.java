package com.ronanvcjunior.taskmaster.contollers;

import com.ronanvcjunior.taskmaster.dtos.Response;
import com.ronanvcjunior.taskmaster.dtos.User;
import com.ronanvcjunior.taskmaster.dtos.requests.TaskRequest;
import com.ronanvcjunior.taskmaster.dtos.requests.UserRequest;
import com.ronanvcjunior.taskmaster.dtos.response.TaskResponse;
import com.ronanvcjunior.taskmaster.services.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.ronanvcjunior.taskmaster.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = { "/task" } )
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('task:create') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> saveTask(@RequestBody @Valid TaskRequest task, HttpServletRequest request) {
        this.taskService.createTask(task.name(), task.cost(), task.paymentDeadline());

        return ResponseEntity.created(URI.create("")).body(getResponse(request, emptyMap(), "Atividade criada", CREATED));
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('task:read') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> getTask(@RequestParam("task") @Valid String taskId, HttpServletRequest request) {
        TaskResponse task = this.taskService.getTask(taskId);

        return ResponseEntity.ok().body(getResponse(request, of("task", task), "Atividade do usuário recuperado", OK));
    }

//    @GetMapping("/all")
//    @PreAuthorize("hasAnyAuthority('task:read') or hasAnyRole('USER', 'ADMIN')")
//    public ResponseEntity<Response> getAllTasks(HttpServletRequest request) {
//        List<TaskResponse> tasks = this.taskService.getAllTasks();
//
//        return ResponseEntity.ok().body(getResponse(request, of("tasks", tasks), "Atividade do usuário recuperado", OK));
//    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('task:read') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "order") String sortField,
            @RequestParam(required = false, defaultValue = "DESC") String sortOrder,
            @RequestParam(required = false) String filters,
            HttpServletRequest request) {

        Page<TaskResponse> tasks = this.taskService.getTasks(page, size, sortField, sortOrder, filters);

        return ResponseEntity.ok().body(getResponse(
                request,
                of("tasks", tasks.getContent(), "total", tasks.getTotalElements()),
                "Atividades recuperadas com sucesso",
                OK
        ));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('task:update') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> updateTask(@RequestBody @Valid TaskRequest task, HttpServletRequest request) {
        this.taskService.updateTask(task.taskId(), task.name(), task.cost(), task.paymentDeadline());

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Atividade alterada", OK));
    }

    @PutMapping("/move")
    @PreAuthorize("hasAnyAuthority('task:update') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> moveTaskOrder(@RequestParam("task") @Valid String taskId, @RequestParam("moveUp") @Valid Boolean moveUp, HttpServletRequest request) {
        this.taskService.moveTaskOrder(taskId, moveUp);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Atividade movida", OK));
    }

    @PutMapping("/setmove")
    @PreAuthorize("hasAnyAuthority('task:update') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> setTaskOrder(@RequestParam("task") @Valid String taskId, @RequestParam("order") @Valid Integer order, HttpServletRequest request) {
        this.taskService.setTaskOrder(taskId, order);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Atividade movida", OK));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('task:delete') or hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response> deleteTask(@RequestParam("task") @Valid String taskId, HttpServletRequest request) {
        this.taskService.deleteTask(taskId);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Atividade do usuário deletada", OK));
    }
}
