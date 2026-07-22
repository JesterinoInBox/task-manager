package com.jesterino.task_manager.event;

public record TaskCreatedEvent(
        Long id,
        String title,
        Long userId,
        Long categoryId,
        String status
) {}
