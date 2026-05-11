package com.tasksync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tasksync.dto.CreateTaskRequest;
import com.tasksync.dto.TaskDTO;
import com.tasksync.dto.UpdateTaskRequest;
import com.tasksync.entity.Task;
import com.tasksync.entity.User;
import com.tasksync.mapper.TaskMapper;
import com.tasksync.mapper.UserMapper;
import com.tasksync.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskMapper.selectById(id);
        return convertToDTO(task);
    }

    @Override
    public List<TaskDTO> getTasksByLeaderId(Long leaderId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("leader_id", leaderId).orderByDesc("created_at");
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByAssigneeId(Long assigneeId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("assignee_id", assigneeId).orderByDesc("created_at");
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(w -> w.eq("leader_id", userId).or().eq("assignee_id", userId))
                   .orderByDesc("created_at");
        List<Task> tasks = taskMapper.selectList(queryWrapper);
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTask(Long leaderId, CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setLeaderId(leaderId);
        task.setAssigneeId(request.getAssigneeId());
        task.setStatus("PENDING");
        task.setPriority(request.getPriority() != null ? request.getPriority() : "MEDIUM");
        task.setDeadline(request.getDeadline());
        task.setProgress(0);

        taskMapper.insert(task);
        return convertToDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskRequest request) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getAssigneeId() != null) {
            task.setAssigneeId(request.getAssigneeId());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
            if ("COMPLETED".equals(request.getStatus())) {
                task.setCompletedAt(LocalDateTime.now());
            } else if ("ACCEPTED".equals(request.getStatus())) {
                task.setAcceptedAt(LocalDateTime.now());
            }
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getDeadline() != null) {
            task.setDeadline(request.getDeadline());
        }
        if (request.getProgress() != null) {
            task.setProgress(request.getProgress());
        }

        taskMapper.updateById(task);
        return convertToDTO(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskMapper.deleteById(id);
    }

    private TaskDTO convertToDTO(Task task) {
        if (task == null) {
            return null;
        }

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setLeaderId(task.getLeaderId());
        dto.setAssigneeId(task.getAssigneeId());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDeadline(task.getDeadline());
        dto.setProgress(task.getProgress());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        dto.setCompletedAt(task.getCompletedAt());
        dto.setAcceptedAt(task.getAcceptedAt());

        if (task.getLeaderId() != null) {
            User leader = userMapper.selectById(task.getLeaderId());
            if (leader != null) {
                dto.setLeaderName(leader.getRealName());
            }
        }

        if (task.getAssigneeId() != null) {
            User assignee = userMapper.selectById(task.getAssigneeId());
            if (assignee != null) {
                dto.setAssigneeName(assignee.getRealName());
                dto.setAssigneeAvatar(assignee.getAvatar());
            }
        }

        return dto;
    }
}
