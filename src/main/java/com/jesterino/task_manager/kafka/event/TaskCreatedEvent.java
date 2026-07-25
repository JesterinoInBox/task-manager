package com.jesterino.task_manager.kafka.event;

import com.jesterino.task_manager.mvc.entity.NotificationType;

public record TaskCreatedEvent(
        Long id,
        String title,
        String user,
        String category,
        String status,
        NotificationType notificationType
) {}
