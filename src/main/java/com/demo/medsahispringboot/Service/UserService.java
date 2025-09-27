package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto registerUser(UserDto userDto,String rawPassword);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id,UserDto userDto);
    void deleteUser(Long id);
}
