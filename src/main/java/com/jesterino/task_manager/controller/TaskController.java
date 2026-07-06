package com.jesterino.task_manager.controller;

import com.jesterino.task_manager.entity.Task;
import com.jesterino.task_manager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TaskController")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(method = "Возвращает все задачи")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @Operation(method = "Возвращает задачу с нужным id")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Task>> getTasksByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(taskService.findByCategory(categoryId));
    }

    @Operation(method = "Создает задачу")
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @Operation(method = "Удаляет задачу")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @Operation(method = "Обновляет задачу")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@Valid @RequestBody Task updatedTask, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(updatedTask, id));
    }
}