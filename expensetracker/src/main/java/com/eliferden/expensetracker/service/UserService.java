package com.eliferden.expensetracker.service;

import com.eliferden.expensetracker.dto.CreateUserDTO;
import com.eliferden.expensetracker.dto.UserDTO;
import com.eliferden.expensetracker.dto.UserUpdateDTO;
import com.eliferden.expensetracker.model.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(CreateUserDTO createUserDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long id);

}
