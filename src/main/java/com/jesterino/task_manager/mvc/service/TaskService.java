package com.jesterino.task_manager.mvc.service;

import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.kafka.event.TaskCreatedEvent;
import com.jesterino.task_manager.kafka.outbox.OutboxEvent;
import com.jesterino.task_manager.kafka.outbox.OutboxRepository;
import com.jesterino.task_manager.mvc.dto.task.TaskCreateDto;
import com.jesterino.task_manager.mvc.dto.task.TaskResponseDto;
import com.jesterino.task_manager.mvc.dto.task.TaskUpdateDto;
import com.jesterino.task_manager.mvc.entity.*;
import com.jesterino.task_manager.mvc.mapper.TaskMapper;
import com.jesterino.task_manager.mvc.repository.CategoryRepository;
import com.jesterino.task_manager.mvc.repository.TaskRepository;
import com.jesterino.task_manager.mvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskMapper taskMapper;
    private final OutboxRepository outboxRepository;



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
    public Page<TaskResponseDto> findAll(
            int page,
            int size,
            String sortBy,
            Sort.Direction direction
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );

        return taskRepository.findAll(pageable)
                .map(taskMapper::toDto);
    }

    @Transactional
    @CacheEvict(value = "tasksList", allEntries = true)
    public TaskResponseDto createTask(TaskCreateDto dto, NotificationType notificationType) {

        log.info("Creating task '{}'", dto.title());
        log.info("With notification type '{}'", notificationType);


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


        log.info(
                "Task {} created successfully",
                savedTask.getId()
        );


        var event = new TaskCreatedEvent(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getUser().getName(),
                savedTask.getCategory().getCategoryName(),
                savedTask.getTaskStatus().name(),
                notificationType
        );


        var outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .aggregateType("TASK")
                .aggregateId(savedTask.getId())
                .eventType("TASK_CREATED")
                .payload(event)
                .createdAt(Instant.now())
                .build();



        outboxRepository.save(outboxEvent);



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

    public List<TaskResponseDto> findAllByUser(Long userId) {

        return taskRepository.findAllByUser(userId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}