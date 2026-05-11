package com.tasksync.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("task_feedbacks")
public class Feedback {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long taskId;

    private Long userId;

    private String content;

    private String tag;

    private String mentionedUsers;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String userAvatar;
}
