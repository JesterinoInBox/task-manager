package com.jesterino.task_manager.mvc.service;

import com.jesterino.task_manager.exception.ResourceNotFoundException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private OutboxRepository outboxRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    NotificationType notificationType = NotificationType.SMS;


    @Test
    void shouldReturnTaskById() {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");

        TaskResponseDto dto = new TaskResponseDto(1L,
                "TestTitle", "TestCategory", "TestName" ,TaskStatus.DONE);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskMapper.toDto(task))
                .thenReturn(dto);

        TaskResponseDto result = taskService.findById(1L);

        assertEquals(dto, result);

        verify(taskRepository).findById(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    void shouldThrowWhenTaskNotFound() {

        when(taskRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.findById(1L)
        );

        verify(taskMapper, never()).toDto(any());
    }

    @Test
    void shouldDeleteTask() {

        when(taskRepository.existsById(1L))
                .thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingMissingTask() {

        when(taskRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.deleteTask(1L)
        );

        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void shouldUpdateTask() {

        Task task = new Task();
        task.setId(1L);

        TaskUpdateDto dto =
                new TaskUpdateDto("New title", TaskStatus.DONE);

        TaskResponseDto response =
                new TaskResponseDto(
                        1L,
                        "New title",
                        "Email",
                        "John",
                        TaskStatus.DONE
                );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        when(taskMapper.toDto(task))
                .thenReturn(response);

        TaskResponseDto result =
                taskService.updateTask(1L, dto);

        assertEquals(response, result);

        verify(taskMapper).updateEntity(task, dto);
        verify(taskRepository).save(task);
    }

    @Test
    void shouldThrowWhenUpdatingMissingTask() {

        when(taskRepository.findById(1L))
                .thenReturn(Optional.empty());

        TaskUpdateDto dto =
                new TaskUpdateDto("Title", TaskStatus.TODO);

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.updateTask(1L, dto)
        );
    }

    @Test
    void shouldReturnTasksByStatus() {

        Task task = new Task();

        TaskResponseDto dto =
                new TaskResponseDto(
                        1L,
                        "Task",
                        "Email",
                        "John",
                        TaskStatus.TODO
                );

        when(taskRepository.findByTaskStatus(TaskStatus.TODO))
                .thenReturn(List.of(task));

        when(taskMapper.toDto(task))
                .thenReturn(dto);

        List<TaskResponseDto> result =
                taskService.findByStatus(TaskStatus.TODO);

        assertEquals(1, result.size());

        verify(taskMapper).toDto(task);
    }

    @Test
    void shouldCreateTask(){

        TaskCreateDto dto = new TaskCreateDto(
                "New task",
                1L,
                1L
        );

        User user = new User();
        user.setId(1L);
        user.setName("John");

        Category category = new Category();
        category.setId(1L);
        category.setCategoryName("EMAIL");

        Task task = new Task();
        task.setTitle("New task");
        task.setUser(user);
        task.setCategory(category);

        Task savedTask = new Task();
        savedTask.setId(10L);
        savedTask.setTitle("New task");
        savedTask.setUser(user);
        savedTask.setCategory(category);
        savedTask.setTaskStatus(TaskStatus.TODO);

        TaskResponseDto response = new TaskResponseDto(
                10L,
                "New task",
                "EMAIL",
                "John",
                TaskStatus.TODO
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(taskMapper.toEntity(dto, category, user))
                .thenReturn(task);

        when(taskRepository.save(task))
                .thenReturn(savedTask);

        when(taskMapper.toDto(savedTask))
                .thenReturn(response);

        TaskResponseDto result = taskService.createTask(dto, notificationType);

        assertEquals(response, result);

        verify(userRepository).findById(1L);
        verify(categoryRepository).findById(1L);
        verify(taskRepository).save(task);
        verify(outboxRepository).save(any(OutboxEvent.class));
    }

    @Test
    void shouldThrowWhenUserNotFound() {

        TaskCreateDto dto = new TaskCreateDto(
                "Task",
                1L,
                1L
        );

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.createTask(dto, notificationType)
        );

        verify(taskRepository, never()).save(any());
        verify(outboxRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {

        TaskCreateDto dto = new TaskCreateDto(
                "Task",
                1L,
                1L
        );

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.createTask(dto, notificationType)
        );

        verify(taskRepository, never()).save(any());
        verify(outboxRepository, never()).save(any());
    }

    @Test
    void shouldReturnTasksByCategory() {

        Task task = new Task();

        TaskResponseDto dto = new TaskResponseDto(
                1L,
                "Task",
                "EMAIL",
                "John",
                TaskStatus.TODO
        );

        when(taskRepository.findByCategoryId(1L))
                .thenReturn(List.of(task));

        when(taskMapper.toDto(task))
                .thenReturn(dto);

        List<TaskResponseDto> result = taskService.findByCategory(1L);

        assertEquals(1, result.size());
        assertEquals(dto, result.getFirst());

        verify(taskRepository).findByCategoryId(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    void shouldReturnTasksByUser() {

        Task task = new Task();

        TaskResponseDto dto = new TaskResponseDto(
                1L,
                "Task",
                "EMAIL",
                "John",
                TaskStatus.TODO
        );

        when(taskRepository.findAllByUser(1L))
                .thenReturn(List.of(task));

        when(taskMapper.toDto(task))
                .thenReturn(dto);

        List<TaskResponseDto> result =
                taskService.findAllByUser(1L);

        assertEquals(1, result.size());

        verify(taskRepository).findAllByUser(1L);
        verify(taskMapper).toDto(task);
    }

    @Test
    void shouldReturnPageOfTasks() {

        Task task = new Task();

        TaskResponseDto dto = new TaskResponseDto(
                1L,
                "Task",
                "EMAIL",
                "John",
                TaskStatus.TODO
        );

        Page<Task> page = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        when(taskMapper.toDto(task))
                .thenReturn(dto);

        Page<TaskResponseDto> result =
                taskService.findAll(
                        0,
                        10,
                        "title",
                        Sort.Direction.ASC
                );

        assertEquals(1, result.getContent().size());

        verify(taskRepository).findAll(any(Pageable.class));
        verify(taskMapper).toDto(task);
    }

    @Test
    void shouldCreateCorrectPageable() {

        when(taskRepository.findAll(any(Pageable.class)))
                .thenReturn(Page.empty());

        taskService.findAll(
                2,
                5,
                "title",
                Sort.Direction.DESC
        );

        ArgumentCaptor<Pageable> captor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(taskRepository).findAll(captor.capture());

        Pageable pageable = captor.getValue();

        assertEquals(2, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());

        Sort.Order order =
                pageable.getSort().getOrderFor("title");

        assertNotNull(order);
        assertEquals(Sort.Direction.DESC, order.getDirection());
    }
}
