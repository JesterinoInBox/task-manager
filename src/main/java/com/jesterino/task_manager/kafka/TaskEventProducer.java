package com.jesterino.task_manager.kafka;

import com.jesterino.task_manager.event.TaskCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskCreatedEvent> kafkaTemplate;

    public void sendTaskCreated(TaskCreatedEvent event) {
        kafkaTemplate.send("task-events", event);
    }
}