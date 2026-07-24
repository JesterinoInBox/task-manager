package com.jesterino.task_manager.kafka.outbox;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {


    private final OutboxRepository repository;

    private final KafkaTemplate<String,String> kafka;



    @Scheduled(fixedDelay = 5000)
    public void publish(){

        var events = repository.findAll();

        for(var event : events){

            kafka.send(
                    "task-events",
                    event.getPayload()
            );

            repository.delete(event);

            log.info(
                    "Event {} sent",
                    event.getId()
            );
        }
    }
}