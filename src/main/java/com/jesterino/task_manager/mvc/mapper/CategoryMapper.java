package com.jesterino.task_manager.mvc.mapper;

import com.jesterino.task_manager.mvc.dto.category.CategoryCreateDto;
import com.jesterino.task_manager.mvc.dto.category.CategoryResponseDto;
import com.jesterino.task_manager.mvc.dto.category.CategoryUpdateDto;
import com.jesterino.task_manager.mvc.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateDto dto) {
        Category category = new Category();
        category.setCategoryName(dto.categoryName());
        return category;
    }

    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getCategoryName()
        );
    }

    public void updateEntity(Category category, CategoryUpdateDto dto) {
        if (dto.categoryName() != null) {
            category.setCategoryName(dto.categoryName());
        }
    }
}