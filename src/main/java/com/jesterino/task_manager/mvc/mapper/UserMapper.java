package com.jesterino.task_manager.mvc.mapper;

import com.jesterino.task_manager.mvc.dto.user.UserCreateDto;
import com.jesterino.task_manager.mvc.dto.user.UserResponseDto;
import com.jesterino.task_manager.mvc.dto.user.UserUpdateDto;
import com.jesterino.task_manager.mvc.entity.User;
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