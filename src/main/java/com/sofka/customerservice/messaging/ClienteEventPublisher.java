package com.sofka.customerservice.messaging;

import com.sofka.customerservice.config.RabbitMqConfig;
import com.sofka.customerservice.domain.Cliente;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final boolean messagingEnabled;

    public ClienteEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${messaging.customer-sync.enabled:true}") boolean messagingEnabled
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.messagingEnabled = messagingEnabled;
    }

    public void publishUpsert(Cliente cliente) {
        if (!messagingEnabled) {
            return;
        }
        ClienteEvent event = new ClienteEvent(
                "CLIENTE_UPSERT",
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );
        publishAfterCommit(event);
    }

    public void publishDelete(Cliente cliente) {
        if (!messagingEnabled) {
            return;
        }
        ClienteEvent event = new ClienteEvent(
                "CLIENTE_DELETE",
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );
        publishAfterCommit(event);
    }

    private void publishAfterCommit(ClienteEvent event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send(event);
                }
            });
            return;
        }
        send(event);
    }

    private void send(ClienteEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.CUSTOMER_EXCHANGE,
                RabbitMqConfig.CUSTOMER_ROUTING_KEY,
                event
        );
    }
}
