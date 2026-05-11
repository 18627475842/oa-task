package com.tasksync.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tasks")
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Long leaderId;

    private Long assigneeId;

    private String status;

    private String priority;

    private LocalDateTime deadline;

    private Integer progress;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    private LocalDateTime acceptedAt;
}
