package com.jesterino.task_manager.kafka.outbox;

import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository
        extends JpaRepository<OutboxEvent, UUID> {


}