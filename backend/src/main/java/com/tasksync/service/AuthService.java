package com.tasksync.service;

import com.tasksync.dto.LoginRequest;
import com.tasksync.dto.LoginResponse;
import com.tasksync.dto.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);

    LoginResponse getCurrentUser(Long userId);
}
