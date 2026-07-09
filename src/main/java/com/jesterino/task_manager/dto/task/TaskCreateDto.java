package com.jesterino.task_manager.dto.task;

public record TaskCreateDto (
            String title,
            Long categoryId,
            Long userId

) {}
