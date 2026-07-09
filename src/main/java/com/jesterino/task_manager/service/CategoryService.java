package com.jesterino.task_manager.service;

import com.jesterino.task_manager.dto.category.CategoryCreateDto;
import com.jesterino.task_manager.dto.category.CategoryResponseDto;
import com.jesterino.task_manager.dto.category.CategoryUpdateDto;
import com.jesterino.task_manager.entity.Category;
import com.jesterino.task_manager.exception.AlreadyExistsException;
import com.jesterino.task_manager.exception.ResourceNotFoundException;
import com.jesterino.task_manager.mapper.CategoryMapper;
import com.jesterino.task_manager.repository.CategoryRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Cacheable(value = "categories", key = "#id")
    public CategoryResponseDto findById(Long id) {

        log.info("Loading category {} from database", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id " + id + " not found"));

        return categoryMapper.toDto(category);
    }

    @Cacheable("categoriesList")
    public List<CategoryResponseDto> findAll() {

        log.info("Loading all categories");

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @CacheEvict(value = "categoriesList", allEntries = true)
    public CategoryResponseDto createCategory(CategoryCreateDto dto) {

        log.info("Creating category '{}'", dto.categoryName());

        if (categoryRepository.existsByCategoryName(dto.categoryName())) {
            throw new AlreadyExistsException(
                    "Category '" + dto.categoryName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(dto);

        Category saved = categoryRepository.save(category);

        log.info("Category {} created", saved.getId());

        return categoryMapper.toDto(saved);
    }

    @Caching(
            put = {
                    @CachePut(value = "categories", key = "#id")
            },
            evict = {
                    @CacheEvict(value = "categoriesList", allEntries = true)
            }
    )
    public CategoryResponseDto updateCategory(Long id, CategoryUpdateDto dto) {

        log.info("Updating category {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id " + id + " not found"));

        if (!category.getCategoryName().equals(dto.categoryName())
                && categoryRepository.existsByCategoryName(dto.categoryName())) {

            throw new AlreadyExistsException(
                    "Category '" + dto.categoryName() + "' already exists");
        }

        categoryMapper.updateEntity(category, dto);

        Category updated = categoryRepository.save(category);

        log.info("Category {} updated", id);

        return categoryMapper.toDto(updated);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "categories", key = "#id"),
                    @CacheEvict(value = "categoriesList", allEntries = true)
            }
    )
    public void deleteCategory(Long id) {

        log.info("Deleting category {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Category with id " + id + " not found");
        }

        categoryRepository.deleteById(id);

        log.info("Category {} deleted", id);
    }
}