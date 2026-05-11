package com.tasksync.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数校验失败"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),

    USERNAME_EXIST(1001, "用户名已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    TOKEN_EXPIRED(1004, "Token已过期"),
    TOKEN_INVALID(1005, "Token无效"),

    TASK_NOT_FOUND(2001, "任务不存在"),
    TASK_ASSIGNEE_ERROR(2002, "任务分配失败"),
    TASK_STATUS_ERROR(2003, "任务状态异常"),

    COMMENT_NOT_FOUND(3001, "评论不存在");

    private final Integer code;
    private final String message;
}
