package com.jesterino.task_manager.mvc.dto.task;

public record TaskCreateDto (
            String title,
            Long categoryId,
            Long userId

) {}
