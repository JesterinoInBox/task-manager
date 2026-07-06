package com.jesterino.task_manager.service;

import com.jesterino.task_manager.ResourceNotFoundException;
import com.jesterino.task_manager.entity.Task;
import com.jesterino.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task with id " + id + " not found"));
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }



    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task with id " + id +" not found");
        }
        taskRepository.deleteById(id);
    }

    public Task updateTask(Task updatedTask, Long id) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id +" not found"));
        existing.setTitle(updatedTask.getTitle());
        existing.setCompleted(updatedTask.isCompleted());
        existing.setCategory(updatedTask.getCategory());
        existing.setUser(updatedTask.getUser());
        return taskRepository.save(existing);
    }

    public List<Task> findByCategory(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }
}
