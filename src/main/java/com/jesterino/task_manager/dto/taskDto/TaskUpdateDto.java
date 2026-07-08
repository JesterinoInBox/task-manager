package com.jesterino.task_manager.dto.taskDto;

import com.jesterino.task_manager.entity.TaskStatus;

public record TaskUpdateDto(
        String title,
        TaskStatus status
) {}