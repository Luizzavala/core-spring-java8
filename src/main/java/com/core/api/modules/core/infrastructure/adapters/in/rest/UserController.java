package com.core.api.modules.core.infrastructure.adapters.in.rest;

import com.core.api.config.security.JWTAuthorizedFilter;
import com.core.api.domain.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.core.api.domain.models.Response;
import com.core.api.modules.core.domain.models.User;
import com.core.api.modules.core.domain.ports.gateway.UserRest;
import com.core.api.modules.core.application.services.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserRest {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTAuthorizedFilter jwtAuthorizedFilter;

    @Override
    @GetMapping
    public Response<?> getAll(
            @RequestParam(name = "idUser", required = false) Integer idUser,
            @RequestParam(name = "userName", required = false, defaultValue = "") String userName,
            @RequestParam(name = "email", required = false, defaultValue = "") String email,
            @RequestParam(name = "status", required = false) Integer status,
            @RequestParam(name = "onlyGraph", required = false) Boolean onlyGraph
    ) {
        try {
            onlyGraph = onlyGraph != null && onlyGraph;
            Set<User> users = userService.getAll(idUser, userName, email, status, onlyGraph);
            return new Response<>(HttpStatus.OK, "Se obtuvieron los registros correctamente.", users);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @GetMapping("/{idUser}")
    public Response<?> getById(@PathVariable Integer idUser) {
        try {
            Set<User> users = new HashSet<>();
            users.add(userService.getById(idUser));
            return new Response<>(HttpStatus.OK, "Se obtuvieron los registros correctamente.", users);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping()
    public Response<?> save(@RequestBody User user) {
        try {
            Set<User> users = new HashSet<>();
            users.add(userService.save(user));
            return new Response<>(HttpStatus.OK, "Se guardó el registro correctamente.", users);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/enable")
    public Response<?> enable(@RequestBody User user) {
        try {
            Set<User> users = new HashSet<>();
            users.add(userService.enable(user));
            return new Response<>(HttpStatus.OK, "Se Habilitó el registro correctamente.", users);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/disable")
    public Response<?> disable(@RequestBody User user) {
        try {
            Set<User> users = new HashSet<>();
            users.add(userService.disable(user));
            return new Response<>(HttpStatus.OK, "Se Dehabilitó el registro correctamente.", users);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/delete")
    public Response<?> delete(@RequestBody User user) {
        try {
            Set<User> users = new HashSet<>();
            users.add(userService.delete(user));
            return new Response<>(HttpStatus.OK, "Se Elimino el registro correctamente.", users);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @Override
    @PostMapping("/login")
    public Response<?> login(
            @RequestBody User user
    ) {
        try {
            User userLogin = userService.login(user.getUserName(), user.getPassword());
            List<User> userList = new ArrayList<>();

            if (userLogin != null && userLogin.getIdUser() != null) {
                Token tokenMap = jwtAuthorizedFilter.getJWTToken(userLogin.getUserName());
                userLogin.setToken(tokenMap.getTokenString());
                userLogin.setExpiredAt(tokenMap.getExpiredAt());
                userList.add(userLogin);
            }
            return new Response<>(HttpStatus.OK, "Inicio de sesión correcto", userList);
        } catch (Exception e) {
            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

}