package com.jesterino.task_manager.mvc.mapper;

import com.jesterino.task_manager.mvc.dto.task.TaskCreateDto;
import com.jesterino.task_manager.mvc.dto.task.TaskResponseDto;
import com.jesterino.task_manager.mvc.dto.task.TaskUpdateDto;
import com.jesterino.task_manager.mvc.entity.Category;
import com.jesterino.task_manager.mvc.entity.Task;
import com.jesterino.task_manager.mvc.entity.TaskStatus;
import com.jesterino.task_manager.mvc.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskResponseDto toDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getCategory().getCategoryName(),
                task.getUser().getName(),
                task.getTaskStatus()
        );
    }

    public Task toEntity(
            TaskCreateDto dto,
            Category category,
            User user
    ) {
        Task task = new Task();

        task.setTitle(dto.title());
        task.setCategory(category);
        task.setUser(user);
        task.setTaskStatus(TaskStatus.TODO);

        return task;
    }

    public void updateEntity(Task task, TaskUpdateDto dto) {

        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.status() != null) {
            task.setTaskStatus(dto.status());
        }
    }
}
