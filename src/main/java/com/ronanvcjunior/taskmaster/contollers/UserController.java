package com.ronanvcjunior.taskmaster.contollers;

import com.ronanvcjunior.taskmaster.dtos.Response;
import com.ronanvcjunior.taskmaster.dtos.requests.UserRequest;
import com.ronanvcjunior.taskmaster.handlers.ApiLogoutHandler;
import com.ronanvcjunior.taskmaster.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.ronanvcjunior.taskmaster.utils.RequestUtils.getResponse;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = { "/user" } )
public class UserController {
    private final UserService userService;

    private final ApiLogoutHandler logoutHandler;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest user, HttpServletRequest request) {
        userService.createUser(user.firstName(), user.lastName(), user.email(), user.password());

        return ResponseEntity.created(URI.create("")).body(getResponse(request, emptyMap(), "Conta criada. Verifique seu email para ativar sua conta", CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyUser(@RequestParam("key") @Valid String key, HttpServletRequest request) {
        userService.verifyAccountKey(key);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Conta Verificada", OK));
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logoutHandler.logout(request, response, authentication);

        return ResponseEntity.ok().body(getResponse(request, emptyMap(), "Você efetuou logout com sucesso", OK));
    }

}
