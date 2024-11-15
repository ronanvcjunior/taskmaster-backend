package com.ronanvcjunior.taskmaster.contollers;

import com.ronanvcjunior.taskmaster.dtos.Response;
import com.ronanvcjunior.taskmaster.dtos.requests.UserRequest;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(path = { "/user" } )
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.firstName(), user.lastName(), user.email(), user.password());

        return ResponseEntity.created(URI.create("")).body(getResponse(request, emptyMap(), "Conta criada. Verifique seu email para ativar sua conta", CREATED));
    }

}
