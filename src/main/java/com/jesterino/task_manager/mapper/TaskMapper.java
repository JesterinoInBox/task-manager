package com.jesterino.task_manager.mapper;

import com.jesterino.task_manager.dto.taskDto.TaskCreateDto;
import com.jesterino.task_manager.dto.taskDto.TaskResponseDto;
import com.jesterino.task_manager.dto.taskDto.TaskUpdateDto;
import com.jesterino.task_manager.entity.Category;
import com.jesterino.task_manager.entity.Task;
import com.jesterino.task_manager.entity.TaskStatus;
import com.jesterino.task_manager.entity.User;
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
