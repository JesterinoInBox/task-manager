package com.jesterino.task_manager.service;

import com.jesterino.task_manager.dto.categoryDto.CategoryCreateDto;
import com.jesterino.task_manager.dto.categoryDto.CategoryResponseDto;
import com.jesterino.task_manager.dto.categoryDto.CategoryUpdateDto;
import com.jesterino.task_manager.entity.Category;
import com.jesterino.task_manager.exception.AlreadyExistsException;
import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.mapper.CategoryMapper;
import com.jesterino.task_manager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id " + id + " not found"));

        return categoryMapper.toDto(category);
    }

    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryResponseDto createCategory(CategoryCreateDto dto) {

        if (categoryRepository.existsByCategoryName(dto.categoryName())) {
            throw new AlreadyExistsException(
                    "Category '" + dto.categoryName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(dto);

        return categoryMapper.toDto(
                categoryRepository.save(category)
        );
    }

    public CategoryResponseDto updateCategory(Long id, CategoryUpdateDto dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id " + id + " not found"));

        if (!category.getCategoryName().equals(dto.categoryName())
                && categoryRepository.existsByCategoryName(dto.categoryName())) {

            throw new AlreadyExistsException(
                    "User '" + dto.categoryName() + "' already exists");
        }

        categoryMapper.updateEntity(category, dto);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id + " not found");
        }

        categoryRepository.deleteById(id);
    }
}