package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Dto.UserDto;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserDto userDto, String rawPassword){
        User user=User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .address(userDto.getAdress())
                .phone(userDto.getPhone())
                .role(userDto.getRole()!=null ?userDtp.getRole():"USER")
                .build();

        User saved=userRepository.save(user);
        return mapToDto(saved);

    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findbyId(id)
                .map(this::mapToDo)
                .orElseThrow(()->new RuntimeException("User Not Found"))
    }

    @Override
    public List<UserDto> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user=userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        user.setName(userDto.getName());
        user.setAddress(userDto.getAddress());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        return mapToDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto mapToDto(User user){
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }

}
