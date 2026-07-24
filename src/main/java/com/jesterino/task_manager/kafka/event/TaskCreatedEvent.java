package com.jesterino.task_manager.kafka.event;

public record TaskCreatedEvent(
        Long id,
        String title,
        String user,
        String category,
        String status
) {}
