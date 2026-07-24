package com.jesterino.task_manager.mvc.dto.task;

import com.jesterino.task_manager.mvc.entity.TaskStatus;

import java.io.Serializable;

public record TaskResponseDto (
        Long id,
        String title,
        String category,
        String username,
        TaskStatus status
) implements Serializable {}
