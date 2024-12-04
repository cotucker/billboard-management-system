package com.example.billboard.mapper;

import com.example.billboard.dto.UserDto;
import com.example.billboard.model.User;

public interface UserMapper {
    static UserDto toUserDto(User user) {
        return UserDto.builder()
                .password(user.getPassword())
                .role(user.getRole())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }
    static User toUser(UserDto user) {
        return User.builder()
                .password(user.getPassword())
                .role(user.getRole())
                .username(user.getUsername())
                .id(user.getId())
                .build();
    }
}
