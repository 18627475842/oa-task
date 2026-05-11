package com.tasksync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Long leaderId;
    private String leaderName;
    private Long assigneeId;
    private String assigneeName;
    private String assigneeAvatar;
    private String status;
    private String priority;
    private LocalDateTime deadline;
    private Integer progress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private LocalDateTime acceptedAt;
}
