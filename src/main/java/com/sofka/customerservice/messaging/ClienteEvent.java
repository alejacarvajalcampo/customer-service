package com.sofka.customerservice.messaging;

public record ClienteEvent(
        String eventType,
        Long clienteId,
        String nombre,
        String identificacion,
        Boolean estado
) {
}
