package com.sofka.customerservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.domain.OutboxEvent;
import com.sofka.customerservice.messaging.ClienteEvent;
import com.sofka.customerservice.repository.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClienteOutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final boolean messagingEnabled;

    public ClienteOutboxService(
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper,
            @Value("${messaging.customer-sync.enabled:true}") boolean messagingEnabled
    ) {
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.messagingEnabled = messagingEnabled;
    }

    public void registerUpsert(Cliente cliente) {
        if (!messagingEnabled) {
            return;
        }
        persist(cliente, "CLIENTE_UPSERT");
    }

    public void registerDelete(Cliente cliente) {
        if (!messagingEnabled) {
            return;
        }
        persist(cliente, "CLIENTE_DELETE");
    }

    private void persist(Cliente cliente, String eventType) {
        ClienteEvent event = new ClienteEvent(
                eventType,
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );
        try {
            outboxEventRepository.save(
                    OutboxEvent.pending("CLIENTE", cliente.getClienteId(), eventType, objectMapper.writeValueAsString(event))
            );
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("No fue posible serializar el evento de cliente", exception);
        }
    }
}
