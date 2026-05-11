package com.tasksync.controller;

import com.tasksync.common.Result;
import com.tasksync.dto.UserDTO;
import com.tasksync.security.UserPrincipal;
import com.tasksync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @GetMapping
    public Result<List<UserDTO>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @GetMapping("/subordinates")
    public Result<List<UserDTO>> getSubordinates(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Result.success(userService.getSubordinates(userPrincipal.getUserId()));
    }

    @GetMapping("/team")
    public Result<List<UserDTO>> getTeamUsers(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<UserDTO> subordinates = userService.getSubordinates(userPrincipal.getUserId());
        UserDTO currentUser = userService.getUserById(userPrincipal.getUserId());
        subordinates.add(0, currentUser);
        return Result.success(subordinates);
    }

    @PutMapping("/{id}")
    public Result<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return Result.success(userService.updateUser(id, userDTO));
    }
}
