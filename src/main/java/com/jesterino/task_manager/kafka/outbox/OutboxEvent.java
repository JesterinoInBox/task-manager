package com.jesterino.task_manager.kafka.outbox;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {


    @Id
    private UUID id;


    private String aggregateType;


    private Long aggregateId;


    private String eventType;


    @Column(columnDefinition = "jsonb")
    private String payload;


    private Instant createdAt;
}