package com.sofka.customerservice.controller;

import com.sofka.customerservice.dto.ClienteRequest;
import com.sofka.customerservice.dto.ClienteResponse;
import com.sofka.customerservice.mapper.ClienteMapper;
import com.sofka.customerservice.service.ClienteApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "CRUD de clientes. Publica eventos asincronos a RabbitMQ cuando un cliente se crea, actualiza o elimina.")
public class ClienteController {

    private final ClienteApplicationService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteApplicationService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente y publica un evento CLIENTE_UPSERT.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"VALIDATION_ERROR\",\"message\":\"La solicitud contiene datos inválidos\"}"))),
            @ApiResponse(responseCode = "409", description = "Cliente duplicado", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"CLIENTE_DUPLICATE\",\"message\":\"Ya existe un cliente con clienteId: 2\"}")))
    })
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteMapper.toResponse(clienteService.crear(clienteMapper.toEntity(request)));
        return ResponseEntity.created(URI.create("/clientes/" + response.clienteId())).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna clientes paginados. Soporta page, size y sort.")
    public ResponseEntity<Page<ClienteResponse>> listar(Pageable pageable) {
        Page<ClienteResponse> response = clienteService.listar(pageable)
                .map(clienteMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clienteId}")
    @Operation(summary = "Consultar cliente por id", description = "Obtiene el detalle de un cliente por su clienteId.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"CLIENTE_NOT_FOUND\",\"message\":\"Cliente no encontrado con clienteId: 99\"}")))
    })
    public ResponseEntity<ClienteResponse> obtener(@Parameter(description = "clienteId del cliente", example = "2") @PathVariable Long clienteId) {
        ClienteResponse response = clienteMapper.toResponse(clienteService.obtenerPorClienteId(clienteId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{clienteId}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente y publica un evento CLIENTE_UPSERT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "400", description = "Validacion invalida", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"VALIDATION_ERROR\",\"message\":\"La solicitud contiene datos inválidos\"}"))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"CLIENTE_NOT_FOUND\",\"message\":\"Cliente no encontrado con clienteId: 99\"}"))),
            @ApiResponse(responseCode = "409", description = "Cliente duplicado", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"CLIENTE_DUPLICATE\",\"message\":\"Ya existe un cliente con identificacion: 9876543210\"}")))
    })
    public ResponseEntity<ClienteResponse> actualizar(
            @Parameter(description = "clienteId del cliente", example = "2") @PathVariable Long clienteId,
            @Valid @RequestBody ClienteRequest request
    ) {
        ClienteResponse response = clienteMapper.toResponse(clienteService.actualizar(clienteId, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clienteId}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente y publica un evento CLIENTE_DELETE.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(schema = @Schema(implementation = com.sofka.customerservice.exception.ApiError.class),
                    examples = @ExampleObject(value = "{\"code\":\"CLIENTE_NOT_FOUND\",\"message\":\"Cliente no encontrado con clienteId: 99\"}")))
    })
    public ResponseEntity<Void> eliminar(@Parameter(description = "clienteId del cliente", example = "2") @PathVariable Long clienteId) {
        clienteService.eliminar(clienteId);
        return ResponseEntity.noContent().build();
    }
}
