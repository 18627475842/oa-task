package com.tasksync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tasksync.dto.UserDTO;
import com.tasksync.entity.User;
import com.tasksync.mapper.UserMapper;
import com.tasksync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long id) {
        User user = userMapper.selectById(id);
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userMapper.selectList(null);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByLeaderId(Long leaderId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("leader_id", leaderId);
        List<User> users = userMapper.selectList(queryWrapper);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getSubordinates(Long leaderId) {
        return getUsersByLeaderId(leaderId);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (userDTO.getRealName() != null) {
            user.setRealName(userDTO.getRealName());
        }
        if (userDTO.getAvatar() != null) {
            user.setAvatar(userDTO.getAvatar());
        }
        if (userDTO.getLeaderId() != null) {
            user.setLeaderId(userDTO.getLeaderId());
        }

        userMapper.updateById(user);
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRealName(user.getRealName());
        dto.setRole(user.getRole());
        dto.setLeaderId(user.getLeaderId());
        dto.setAvatar(user.getAvatar());
        dto.setCreatedAt(user.getCreatedAt());

        if (user.getLeaderId() != null) {
            User leader = userMapper.selectById(user.getLeaderId());
            if (leader != null) {
                dto.setLeaderName(leader.getRealName());
            }
        }

        return dto;
    }
}
