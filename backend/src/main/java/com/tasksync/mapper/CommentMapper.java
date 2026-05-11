package com.tasksync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tasksync.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
