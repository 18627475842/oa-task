package com.tasksync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tasksync.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
