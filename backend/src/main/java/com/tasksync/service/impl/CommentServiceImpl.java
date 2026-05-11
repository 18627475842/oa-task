package com.tasksync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tasksync.dto.CommentDTO;
import com.tasksync.entity.Comment;
import com.tasksync.entity.User;
import com.tasksync.mapper.CommentMapper;
import com.tasksync.mapper.UserMapper;
import com.tasksync.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Override
    public List<CommentDTO> getCommentsByTaskId(Long taskId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId).orderByAsc("created_at");
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return comments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO createComment(Long taskId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setTaskId(taskId);
        comment.setUserId(userId);
        comment.setContent(content);

        commentMapper.insert(comment);
        return convertToDTO(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除此评论");
        }
        commentMapper.deleteById(commentId);
    }

    private CommentDTO convertToDTO(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setTaskId(comment.getTaskId());
        dto.setUserId(comment.getUserId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        if (comment.getUserId() != null) {
            User user = userMapper.selectById(comment.getUserId());
            if (user != null) {
                dto.setUserName(user.getRealName());
                dto.setUserAvatar(user.getAvatar());
            }
        }

        return dto;
    }
}
