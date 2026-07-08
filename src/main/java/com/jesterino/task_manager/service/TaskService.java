package com.jesterino.task_manager.service;

import com.jesterino.task_manager.dto.taskDto.TaskCreateDto;
import com.jesterino.task_manager.dto.taskDto.TaskResponseDto;
import com.jesterino.task_manager.dto.taskDto.TaskUpdateDto;
import com.jesterino.task_manager.entity.Category;
import com.jesterino.task_manager.entity.Task;
import com.jesterino.task_manager.entity.TaskStatus;
import com.jesterino.task_manager.entity.User;
import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.mapper.TaskMapper;
import com.jesterino.task_manager.repository.CategoryRepository;
import com.jesterino.task_manager.repository.TaskRepository;
import com.jesterino.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;

    public TaskResponseDto findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task with id " + id + " not found"));

        return taskMapper.toDto(task);
    }

    public List<TaskResponseDto> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskResponseDto createTask(TaskCreateDto dto) {

        log.info("Creating task '{}'", dto.title());

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with id " + dto.userId() + " not found"));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category with id " + dto.categoryId() + " not found"));

        Task task = taskMapper.toEntity(dto, category, user);

        Task savedTask = taskRepository.save(task);
        log.info("Task {} created successfully", savedTask.getId());

        return taskMapper.toDto(savedTask);
    }

    public TaskResponseDto updateTask(Long id, TaskUpdateDto dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task with id " + id + " not found"));

        taskMapper.updateEntity(task, dto);

        Task updatedTask = taskRepository.save(task);

        log.info("Task {} updated", id);

        return taskMapper.toDto(updatedTask);
    }

    public void deleteTask(Long id) {

        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Task with id " + id + " not found");
        }

        taskRepository.deleteById(id);
        log.info("Task {} deleted", id);
    }

    public List<TaskResponseDto> findByCategory(Long categoryId) {

        return taskRepository.findByCategoryId(categoryId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public List<TaskResponseDto> findByStatus(TaskStatus status) {

        return taskRepository.findByTaskStatus(status)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}