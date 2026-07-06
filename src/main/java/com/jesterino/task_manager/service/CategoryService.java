package com.jesterino.task_manager.service;

import com.jesterino.task_manager.ResourceNotFoundException;
import com.jesterino.task_manager.entity.Category;
import com.jesterino.task_manager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category with id " + id + " not found"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if(!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with id " + id +" not found");
        }
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Category updatedCategory, Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id +" not found"));
        existing.setCategoryName(updatedCategory.getCategoryName());
        return categoryRepository.save(existing);
    }

}
