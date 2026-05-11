package com.tasksync.controller;

import com.tasksync.common.Result;
import com.tasksync.dto.CreateTaskRequest;
import com.tasksync.dto.TaskDTO;
import com.tasksync.dto.UpdateTaskRequest;
import com.tasksync.security.UserPrincipal;
import com.tasksync.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public Result<TaskDTO> getTaskById(@PathVariable Long id) {
        return Result.success(taskService.getTaskById(id));
    }

    @GetMapping
    public Result<List<TaskDTO>> getTasks(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Result.success(taskService.getTasksByUserId(userPrincipal.getUserId()));
    }

    @GetMapping("/created")
    public Result<List<TaskDTO>> getCreatedTasks(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Result.success(taskService.getTasksByLeaderId(userPrincipal.getUserId()));
    }

    @GetMapping("/assigned")
    public Result<List<TaskDTO>> getAssignedTasks(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return Result.success(taskService.getTasksByAssigneeId(userPrincipal.getUserId()));
    }

    @PostMapping
    public Result<TaskDTO> createTask(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateTaskRequest request) {
        return Result.success(taskService.createTask(userPrincipal.getUserId(), request));
    }

    @PutMapping("/{id}")
    public Result<TaskDTO> updateTask(@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return Result.success(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return Result.success();
    }
}
