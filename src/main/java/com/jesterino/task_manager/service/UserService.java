package com.jesterino.task_manager.service;

import com.jesterino.task_manager.dto.userDto.UserCreateDto;
import com.jesterino.task_manager.dto.userDto.UserResponseDto;
import com.jesterino.task_manager.dto.userDto.UserUpdateDto;
import com.jesterino.task_manager.entity.User;
import com.jesterino.task_manager.exception.AlreadyExistsException;
import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.mapper.UserMapper;
import com.jesterino.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto findById(Long id) {

        log.debug("Searching task with id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));

        return userMapper.toDto(user);
    }

    public List<UserResponseDto> findAll() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserResponseDto createUser(UserCreateDto dto) {

        if (userRepository.existsByName(dto.name())) {
            throw new AlreadyExistsException(
                    "User '" + dto.name() + "' already exists");
        }

        User user = userMapper.toEntity(dto);

        log.info("Creating user {}", dto.name());
        return userMapper.toDto(
                userRepository.save(user)
        );
    }

    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with id " + id + " not found"));

        if (!user.getName().equals(dto.name())
                && userRepository.existsByName(dto.name())) {

            throw new AlreadyExistsException(
                    "User '" + dto.name() + "' already exists");
        }

        userMapper.updateEntity(user, dto);

        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }

        userRepository.deleteById(id);
    }
}