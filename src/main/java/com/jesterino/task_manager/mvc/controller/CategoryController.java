package com.jesterino.task_manager.mvc.controller;

import com.jesterino.task_manager.mvc.dto.category.CategoryCreateDto;
import com.jesterino.task_manager.mvc.dto.category.CategoryResponseDto;
import com.jesterino.task_manager.mvc.dto.category.CategoryUpdateDto;
import com.jesterino.task_manager.mvc.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "API для работы с категориями")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Получить все категории")
    @ApiResponse(responseCode = "200", description = "Список категорий")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Получить категорию по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(summary = "Создать категорию")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Категория создана"),
            @ApiResponse(responseCode = "409", description = "Категория уже существует", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(
            @Valid @RequestBody CategoryCreateDto dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(dto));
    }

    @Operation(summary = "Обновить категорию")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория обновлена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDto dto) {

        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @Operation(summary = "Удалить категорию")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Категория удалена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}