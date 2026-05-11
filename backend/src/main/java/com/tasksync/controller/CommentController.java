package com.tasksync.controller;

import com.tasksync.common.Result;
import com.tasksync.dto.CommentDTO;
import com.tasksync.security.UserPrincipal;
import com.tasksync.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public Result<List<CommentDTO>> getComments(@PathVariable Long taskId) {
        return Result.success(commentService.getCommentsByTaskId(taskId));
    }

    @PostMapping
    public Result<CommentDTO> createComment(
            @PathVariable Long taskId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody Map<String, String> body) {
        return Result.success(commentService.createComment(taskId, userPrincipal.getUserId(), body.get("content")));
    }

    @DeleteMapping("/{commentId}")
    public Result<Void> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        commentService.deleteComment(commentId, userPrincipal.getUserId());
        return Result.success();
    }
}
