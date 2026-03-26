package com.sofka.customerservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.customerservice.config.RabbitMqConfig;
import com.sofka.customerservice.domain.OutboxEvent;
import com.sofka.customerservice.repository.OutboxEventRepository;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final boolean messagingEnabled;
    private final int maxAttempts;

    public OutboxEventProcessor(
            OutboxEventRepository outboxEventRepository,
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper,
            @Value("${messaging.customer-sync.enabled:true}") boolean messagingEnabled,
            @Value("${messaging.outbox.max-attempts:5}") int maxAttempts
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.messagingEnabled = messagingEnabled;
        this.maxAttempts = maxAttempts;
    }

    @Scheduled(fixedDelayString = "${messaging.outbox.fixed-delay-ms:5000}")
    @Transactional
    public void publishPendingEvents() {
        if (!messagingEnabled) {
            return;
        }
        List<OutboxEvent> events = outboxEventRepository.findTop50ByStatusInOrderByIdAsc(List.of("PENDING", "FAILED"));
        for (OutboxEvent event : events) {
            try {
                ClienteEvent payload = objectMapper.readValue(event.getPayload(), ClienteEvent.class);
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.CUSTOMER_EXCHANGE,
                        RabbitMqConfig.CUSTOMER_ROUTING_KEY,
                        payload
                );
                event.markPublished();
            } catch (JsonProcessingException exception) {
                event.markFailed("Payload invalido para outbox: " + exception.getOriginalMessage(), maxAttempts);
            } catch (Exception exception) {
                event.markFailed(exception.getMessage(), maxAttempts);
            }
        }
    }
}
