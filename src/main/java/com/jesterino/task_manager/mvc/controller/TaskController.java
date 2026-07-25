package com.jesterino.task_manager.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jesterino.task_manager.mvc.dto.task.TaskCreateDto;
import com.jesterino.task_manager.mvc.dto.task.TaskResponseDto;
import com.jesterino.task_manager.mvc.dto.task.TaskUpdateDto;
import com.jesterino.task_manager.mvc.entity.NotificationType;
import com.jesterino.task_manager.mvc.entity.TaskStatus;
import com.jesterino.task_manager.mvc.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Controller", description = "API для работы с задачами")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Получить все задачи")
    @ApiResponse(responseCode = "200", description = "Список задач")
    @GetMapping
    public ResponseEntity<Page<TaskResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        return ResponseEntity.ok(
                taskService.findAll(page, size, sortBy, direction)
        );
    }
    @Operation(summary = "Получить все задачи пользователя")
    @ApiResponse(responseCode = "200", description = "Список задач")
    @GetMapping("/getAllByUser/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.findAllByUser(userId));
    }

    @Operation(summary = "Получить задачу по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @Operation(summary = "Создать задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Задача создана"),
            @ApiResponse(responseCode = "404", description = "Пользователь или категория не найдены", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TaskResponseDto> create(
            @Valid @RequestBody TaskCreateDto dto,
            @RequestParam NotificationType notificationType) throws JsonProcessingException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(dto, notificationType));
    }

    @Operation(summary = "Обновить задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDto dto) {

        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    @Operation(summary = "Удалить задачу")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить задачи определённой категории")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список задач"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content)
    })
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TaskResponseDto>> getByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(taskService.findByCategory(categoryId));
    }

    @Operation(summary = "Получить задачи по статусу")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список задач"),
            @ApiResponse(responseCode = "404", description = "Задачи не найдены", content = @Content)
    })
    @GetMapping("/status")
    public ResponseEntity<List<TaskResponseDto>> getByStatus(
            @RequestParam TaskStatus status) {

        return ResponseEntity.ok(taskService.findByStatus(status));
    }
}