package com.tasksync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private Long assigneeId;
    private String status;
    private String priority;
    private LocalDateTime deadline;
    private Integer progress;
}
