package com.tasksync.controller;

import com.tasksync.common.Result;
import com.tasksync.entity.Feedback;
import com.tasksync.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping
    public Result<List<Feedback>> getFeedbacks(@PathVariable Long taskId) {
        return Result.success(feedbackService.getFeedbacksByTaskId(taskId));
    }

    @PostMapping
    public Result<Feedback> createFeedback(
            @PathVariable Long taskId,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody Feedback feedback) {
        feedback.setTaskId(taskId);
        feedback.setUserId(userId);
        return Result.success(feedbackService.createFeedback(feedback));
    }

    @DeleteMapping("/{feedbackId}")
    public Result<Void> deleteFeedback(
            @PathVariable Long taskId,
            @PathVariable Long feedbackId,
            @RequestHeader("X-User-Id") Long userId) {
        feedbackService.deleteFeedback(feedbackId, userId);
        return Result.success();
    }
}
