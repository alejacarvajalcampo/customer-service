package com.sofka.customerservice.messaging;

import com.sofka.customerservice.config.RabbitMqConfig;
import com.sofka.customerservice.domain.Cliente;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.CUSTOMER_EXCHANGE,
                RabbitMqConfig.CUSTOMER_ROUTING_KEY,
                new ClienteEvent(
                        "CLIENTE_UPSERT",
                        cliente.getClienteId(),
                        cliente.getNombre(),
                        cliente.getIdentificacion(),
                        cliente.getEstado()
                )
        );
    }

    public void publishDelete(Cliente cliente) {
        if (!messagingEnabled) {
            return;
        }
        rabbitTemplate.convertAndSend(
                RabbitMqConfig.CUSTOMER_EXCHANGE,
                RabbitMqConfig.CUSTOMER_ROUTING_KEY,
                new ClienteEvent(
                        "CLIENTE_DELETE",
                        cliente.getClienteId(),
                        cliente.getNombre(),
                        cliente.getIdentificacion(),
                        cliente.getEstado()
                )
        );
    }
}
