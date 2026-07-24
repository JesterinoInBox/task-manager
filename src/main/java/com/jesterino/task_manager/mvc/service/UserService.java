package com.jesterino.task_manager.mvc.service;

import com.jesterino.task_manager.mvc.dto.user.UserCreateDto;
import com.jesterino.task_manager.mvc.dto.user.UserResponseDto;
import com.jesterino.task_manager.mvc.dto.user.UserUpdateDto;
import com.jesterino.task_manager.mvc.entity.User;
import com.jesterino.task_manager.exception.AlreadyExistsException;
import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.mvc.mapper.UserMapper;
import com.jesterino.task_manager.mvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Cacheable(value = "users", key = "#id")
    public UserResponseDto findById(Long id) {

        log.info("Loading user {} from database", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"));

        return userMapper.toDto(user);
    }

    @Cacheable("usersList")
    public List<UserResponseDto> findAll() {

        log.info("Loading all users");

        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @CacheEvict(value = "usersList", allEntries = true)
    public UserResponseDto createUser(UserCreateDto dto) {

        log.info("Creating user '{}'", dto.name());

        if (userRepository.existsByName(dto.name())) {
            throw new AlreadyExistsException(
                    "User '" + dto.name() + "' already exists");
        }

        User user = userMapper.toEntity(dto);

        User saved = userRepository.save(user);

        log.info("User {} created", saved.getId());

        return userMapper.toDto(saved);
    }

    @Caching(
            put = {
                    @CachePut(value = "users", key = "#id")
            },
            evict = {
                    @CacheEvict(value = "usersList", allEntries = true)
            }
    )
    public UserResponseDto updateUser(Long id, UserUpdateDto dto) {

        log.info("Updating user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + id + " not found"));

        if (!user.getName().equals(dto.name())
                && userRepository.existsByName(dto.name())) {

            throw new AlreadyExistsException(
                    "User '" + dto.name() + "' already exists");
        }

        userMapper.updateEntity(user, dto);

        User updated = userRepository.save(user);

        log.info("User {} updated", id);

        return userMapper.toDto(updated);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "users", key = "#id"),
                    @CacheEvict(value = "usersList", allEntries = true)
            }
    )
    public void deleteUser(Long id) {

        log.info("Deleting user {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "User with id " + id + " not found");
        }

        userRepository.deleteById(id);

        log.info("User {} deleted", id);
    }
}