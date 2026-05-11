package com.tasksync.service;

import com.tasksync.dto.CommentDTO;
import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommentsByTaskId(Long taskId);

    CommentDTO createComment(Long taskId, Long userId, String content);

    void deleteComment(Long commentId, Long userId);
}
