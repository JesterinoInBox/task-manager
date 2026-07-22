package com.jesterino.task_manager.service;

import com.jesterino.task_manager.dto.task.TaskCreateDto;
import com.jesterino.task_manager.dto.task.TaskResponseDto;
import com.jesterino.task_manager.dto.task.TaskUpdateDto;
import com.jesterino.task_manager.entity.Category;
import com.jesterino.task_manager.entity.Task;
import com.jesterino.task_manager.entity.TaskStatus;
import com.jesterino.task_manager.entity.User;
import com.jesterino.task_manager.event.TaskCreatedEvent;
import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.kafka.TaskEventProducer;
import com.jesterino.task_manager.mapper.TaskMapper;
import com.jesterino.task_manager.repository.CategoryRepository;
import com.jesterino.task_manager.repository.TaskRepository;
import com.jesterino.task_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    private final TaskEventProducer producer;

    @Cacheable(value = "tasks", key = "#id")
    public TaskResponseDto findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task with id " + id + " not found"));

        return taskMapper.toDto(task);
    }

    @Caching(
            put = {
                    @CachePut(value = "tasks")
            },
            evict = {
                    @CacheEvict(value = "tasksList", allEntries = true)
            }
    )
    public List<TaskResponseDto> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @CacheEvict(value = "tasksList", allEntries = true)
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

        producer.sendTaskCreated(
                new TaskCreatedEvent(
                        savedTask.getId(),
                        savedTask.getTitle(),
                        savedTask.getUser().getId(),
                        savedTask.getCategory().getId(),
                        savedTask.getTaskStatus().name()
                )
        );


        return taskMapper.toDto(savedTask);
    }

    @CachePut(value = "tasks", key = "#id")
    public TaskResponseDto updateTask(Long id, TaskUpdateDto dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task with id " + id + " not found"));

        taskMapper.updateEntity(task, dto);

        Task updatedTask = taskRepository.save(task);

        log.info("Task {} updated", id);

        return taskMapper.toDto(updatedTask);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "tasks", key = "#id"),
                    @CacheEvict(value = "tasksList", allEntries = true)
            }
    )
    public void deleteTask(Long id) {

        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Task with id " + id + " not found");
        }

        taskRepository.deleteById(id);
        log.info("Task {} deleted", id);
    }

    @Cacheable(value = "tasksByCategory", key = "#categoryId")
    public List<TaskResponseDto> findByCategory(Long categoryId) {

        return taskRepository.findByCategoryId(categoryId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Cacheable(value = "tasksByStatus", key = "#status")
    public List<TaskResponseDto> findByStatus(TaskStatus status) {

        return taskRepository.findByTaskStatus(status)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}