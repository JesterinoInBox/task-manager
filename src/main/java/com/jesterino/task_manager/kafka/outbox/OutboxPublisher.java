
package com.jesterino.task_manager.kafka.outbox;

import com.jesterino.task_manager.kafka.event.TaskCreatedEvent;
import com.jesterino.task_manager.kafka.outbox.OutboxRepository;
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
    private final KafkaTemplate<String, TaskCreatedEvent> kafka;

    @Scheduled(fixedDelay = 5000)
    public void publish(){

        var events = repository.findAll();

        for(var event : events){

            kafka.send("task-events", event.getPayload())
                    .whenComplete((result, ex) -> {

                        if(ex == null){
                            repository.delete(event);
                        }
                        else {
                            log.error("Failed sending event", ex);
                        }

                    });

            log.info(
                    "Event {} sent",
                    event.getId()
            );
        }
    }
}