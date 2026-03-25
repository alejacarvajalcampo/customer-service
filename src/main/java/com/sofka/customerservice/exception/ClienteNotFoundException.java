package com.sofka.customerservice.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long clienteId) {
        super("Cliente no encontrado con clienteId: " + clienteId);
    }
}
