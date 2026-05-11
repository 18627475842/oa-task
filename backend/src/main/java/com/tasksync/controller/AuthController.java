package com.tasksync.controller;

import com.tasksync.common.Result;
import com.tasksync.dto.LoginRequest;
import com.tasksync.dto.LoginResponse;
import com.tasksync.dto.RegisterRequest;
import com.tasksync.security.UserPrincipal;
import com.tasksync.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @PostMapping("/register")
    public Result<LoginResponse> register(@RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    @GetMapping("/current")
    public Result<LoginResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Result.success(authService.getCurrentUser(userPrincipal.getUserId()));
    }
}
