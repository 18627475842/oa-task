package com.tasksync.service;

import com.tasksync.entity.Feedback;
import java.util.List;

public interface FeedbackService {

    List<Feedback> getFeedbacksByTaskId(Long taskId);

    Feedback createFeedback(Feedback feedback);

    void deleteFeedback(Long feedbackId, Long userId);
}
