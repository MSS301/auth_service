package com.auth_svc.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            kafkaTemplate.send("user.registered", event);

            log.info("Published user.registered event: userId={}, email={}", event.getUserId(), event.getEmail());
        } catch (Exception e) {
            log.error(
                    "Failed to publish user.registered event for userId {}: {}", event.getUserId(), e.getMessage(), e);
        }
    }
}
