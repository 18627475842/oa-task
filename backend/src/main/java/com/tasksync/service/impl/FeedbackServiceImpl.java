package com.tasksync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tasksync.entity.Feedback;
import com.tasksync.entity.User;
import com.tasksync.mapper.FeedbackMapper;
import com.tasksync.mapper.UserMapper;
import com.tasksync.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper feedbackMapper;
    private final UserMapper userMapper;

    @Override
    public List<Feedback> getFeedbacksByTaskId(Long taskId) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId).orderByDesc("created_at");
        List<Feedback> feedbacks = feedbackMapper.selectList(queryWrapper);

        for (Feedback feedback : feedbacks) {
            User user = userMapper.selectById(feedback.getUserId());
            if (user != null) {
                feedback.setUserName(user.getRealName());
                feedback.setUserAvatar(user.getAvatar());
            }
        }
        return feedbacks;
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        feedbackMapper.insert(feedback);
        return feedback;
    }

    @Override
    public void deleteFeedback(Long feedbackId, Long userId) {
        Feedback feedback = feedbackMapper.selectById(feedbackId);
        if (feedback == null) {
            throw new RuntimeException("Feedback not found");
        }
        if (!feedback.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this feedback");
        }
        feedbackMapper.deleteById(feedbackId);
    }
}
