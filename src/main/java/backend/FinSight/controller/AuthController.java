package backend.FinSight.controller;

import backend.FinSight.dto.LoginRequest;
import backend.FinSight.model.User;
import backend.FinSight.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import backend.FinSight.dto.RegisterRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(
            @RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public User login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}