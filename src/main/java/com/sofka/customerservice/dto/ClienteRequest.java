package com.sofka.customerservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotNull(message = "clienteId es obligatorio")
        Long clienteId,
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 100, message = "nombre no puede superar 100 caracteres")
        String nombre,
        @NotBlank(message = "genero es obligatorio")
        @Size(max = 20, message = "genero no puede superar 20 caracteres")
        String genero,
        @NotNull(message = "edad es obligatoria")
        @Min(value = 0, message = "edad debe ser mayor o igual a 0")
        Integer edad,
        @NotBlank(message = "identificacion es obligatoria")
        @Size(max = 20, message = "identificacion no puede superar 20 caracteres")
        String identificacion,
        @NotBlank(message = "direccion es obligatoria")
        @Size(max = 150, message = "direccion no puede superar 150 caracteres")
        String direccion,
        @NotBlank(message = "telefono es obligatorio")
        @Size(max = 20, message = "telefono no puede superar 20 caracteres")
        String telefono,
        @NotBlank(message = "contrasena es obligatoria")
        @Size(max = 120, message = "contrasena no puede superar 120 caracteres")
        String contrasena,
        @NotNull(message = "estado es obligatorio")
        Boolean estado
) {
}
