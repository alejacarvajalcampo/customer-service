package com.sofka.customerservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @Schema(description = "Identificador funcional del cliente", example = "2")
        @NotNull(message = "clienteId es obligatorio")
        Long clienteId,
        @Schema(description = "Nombre completo del cliente", example = "Marianela Montalvo")
        @NotBlank(message = "nombre es obligatorio")
        @Size(max = 100, message = "nombre no puede superar 100 caracteres")
        String nombre,
        @Schema(description = "Genero del cliente", example = "Femenino")
        @NotBlank(message = "genero es obligatorio")
        @Size(max = 20, message = "genero no puede superar 20 caracteres")
        String genero,
        @Schema(description = "Edad del cliente", example = "28")
        @NotNull(message = "edad es obligatoria")
        @Min(value = 0, message = "edad debe ser mayor o igual a 0")
        Integer edad,
        @Schema(description = "Numero de identificacion unico", example = "9876543210")
        @NotBlank(message = "identificacion es obligatoria")
        @Size(max = 20, message = "identificacion no puede superar 20 caracteres")
        String identificacion,
        @Schema(description = "Direccion de residencia", example = "Amazonas y NNUU")
        @NotBlank(message = "direccion es obligatoria")
        @Size(max = 150, message = "direccion no puede superar 150 caracteres")
        String direccion,
        @Schema(description = "Telefono de contacto", example = "097548965")
        @NotBlank(message = "telefono es obligatorio")
        @Size(max = 20, message = "telefono no puede superar 20 caracteres")
        String telefono,
        @Schema(description = "Contrasena del cliente", example = "5678")
        @NotBlank(message = "contrasena es obligatoria")
        @Size(max = 120, message = "contrasena no puede superar 120 caracteres")
        String contrasena,
        @Schema(description = "Estado del cliente", example = "true")
        @NotNull(message = "estado es obligatorio")
        Boolean estado
) {
}
