package com.jesterino.task_manager.dto.taskDto;

import com.jesterino.task_manager.entity.TaskStatus;

public record TaskResponseDto (
        Long id,
        String title,
        String category,
        String username,
        TaskStatus status
) {}
