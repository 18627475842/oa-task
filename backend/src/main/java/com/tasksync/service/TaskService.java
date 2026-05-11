package com.tasksync.service;

import com.tasksync.dto.CreateTaskRequest;
import com.tasksync.dto.TaskDTO;
import com.tasksync.dto.UpdateTaskRequest;
import java.util.List;

public interface TaskService {

    TaskDTO getTaskById(Long id);

    List<TaskDTO> getTasksByLeaderId(Long leaderId);

    List<TaskDTO> getTasksByAssigneeId(Long assigneeId);

    List<TaskDTO> getTasksByUserId(Long userId);

    TaskDTO createTask(Long leaderId, CreateTaskRequest request);

    TaskDTO updateTask(Long id, UpdateTaskRequest request);

    void deleteTask(Long id);
}
