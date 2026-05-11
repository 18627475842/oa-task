package com.tasksync.service;

import com.tasksync.dto.UserDTO;
import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByLeaderId(Long leaderId);

    List<UserDTO> getSubordinates(Long leaderId);

    UserDTO updateUser(Long id, UserDTO userDTO);
}
