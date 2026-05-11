package com.tasksync.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String realName;
    private String role;
    private Long leaderId;
    private String leaderName;
    private String avatar;
    private LocalDateTime createdAt;
}
