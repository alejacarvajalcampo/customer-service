package com.sofka.customerservice.messaging;

public record ClienteEvent(
        TipoEventoCliente eventType,
        Long clienteId,
        String nombre,
        String identificacion,
        Boolean estado
) {
}
