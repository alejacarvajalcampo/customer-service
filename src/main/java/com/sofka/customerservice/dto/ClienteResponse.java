package com.sofka.customerservice.dto;

public record ClienteResponse(
        Long clienteId,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        String contrasena,
        Boolean estado
) {
}
