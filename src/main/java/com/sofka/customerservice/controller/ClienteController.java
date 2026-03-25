package com.sofka.customerservice.controller;

import com.sofka.customerservice.dto.ClienteRequest;
import com.sofka.customerservice.dto.ClienteResponse;
import com.sofka.customerservice.mapper.ClienteMapper;
import com.sofka.customerservice.service.ClienteApplicationService;
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
public class ClienteController {

    private final ClienteApplicationService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteApplicationService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse response = clienteMapper.toResponse(clienteService.crear(clienteMapper.toEntity(request)));
        return ResponseEntity.created(URI.create("/clientes/" + response.clienteId())).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponse>> listar(Pageable pageable) {
        Page<ClienteResponse> response = clienteService.listar(pageable)
                .map(clienteMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteResponse> obtener(@PathVariable Long clienteId) {
        ClienteResponse response = clienteMapper.toResponse(clienteService.obtenerPorClienteId(clienteId));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long clienteId,
            @Valid @RequestBody ClienteRequest request
    ) {
        ClienteResponse response = clienteMapper.toResponse(clienteService.actualizar(clienteId, request));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long clienteId) {
        clienteService.eliminar(clienteId);
        return ResponseEntity.noContent().build();
    }
}
