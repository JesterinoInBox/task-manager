package com.jesterino.task_manager.mapper;

import com.jesterino.task_manager.dto.userDto.UserCreateDto;
import com.jesterino.task_manager.dto.userDto.UserResponseDto;
import com.jesterino.task_manager.dto.userDto.UserUpdateDto;
import com.jesterino.task_manager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateDto dto) {

        User user = new User();
        user.setName(dto.name());

        return user;
    }

    public UserResponseDto toDto(User user) {

        return new UserResponseDto(
                user.getId(),
                user.getName()
        );
    }

    public void updateEntity(User user, UserUpdateDto dto) {

        if (dto.name() != null) {
            user.setName(dto.name());
        }
    }
}