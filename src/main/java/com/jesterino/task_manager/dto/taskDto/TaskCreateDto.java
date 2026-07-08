package com.jesterino.task_manager.dto.taskDto;

public record TaskCreateDto (
            String title,
            Long categoryId,
            Long userId

) {}
