package com.jesterino.task_manager.mvc.dto.task;

import com.jesterino.task_manager.mvc.entity.TaskStatus;

public record TaskUpdateDto(
        String title,
        TaskStatus status
) {}