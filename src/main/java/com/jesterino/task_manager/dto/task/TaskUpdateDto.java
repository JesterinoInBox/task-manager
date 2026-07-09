package com.jesterino.task_manager.dto.task;

import com.jesterino.task_manager.entity.TaskStatus;

public record TaskUpdateDto(
        String title,
        TaskStatus status
) {}