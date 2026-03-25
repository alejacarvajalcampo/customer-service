package com.sofka.customerservice.service;

import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.dto.ClienteRequest;
import com.sofka.customerservice.exception.ClienteNotFoundException;
import com.sofka.customerservice.exception.DuplicateClienteException;
import com.sofka.customerservice.messaging.ClienteEventPublisher;
import com.sofka.customerservice.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService implements ClienteApplicationService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher clienteEventPublisher;

    public ClienteService(ClienteRepository clienteRepository, ClienteEventPublisher clienteEventPublisher) {
        this.clienteRepository = clienteRepository;
        this.clienteEventPublisher = clienteEventPublisher;
    }

    @Override
    @Transactional
    public Cliente crear(Cliente cliente) {
        validarDuplicadosEnCreacion(cliente);
        Cliente saved = clienteRepository.save(cliente);
        clienteEventPublisher.publishUpsert(saved);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> listar(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerPorClienteId(Long clienteId) {
        return clienteRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));
    }

    @Override
    @Transactional
    public Cliente actualizar(Long clienteId, ClienteRequest request) {
        Cliente cliente = obtenerPorClienteId(clienteId);
        validarDuplicadosEnActualizacion(request, clienteId);
        cliente.actualizarDatos(
                request.nombre(),
                request.genero(),
                request.edad(),
                request.identificacion(),
                request.direccion(),
                request.telefono(),
                request.contrasena(),
                request.estado()
        );
        Cliente saved = clienteRepository.save(cliente);
        clienteEventPublisher.publishUpsert(saved);
        return saved;
    }

    @Override
    @Transactional
    public void eliminar(Long clienteId) {
        Cliente cliente = obtenerPorClienteId(clienteId);
        clienteEventPublisher.publishDelete(cliente);
        clienteRepository.delete(cliente);
    }

    private void validarDuplicadosEnCreacion(Cliente cliente) {
        if (clienteRepository.existsByClienteId(cliente.getClienteId())) {
            throw new DuplicateClienteException("Ya existe un cliente con clienteId: " + cliente.getClienteId());
        }
        if (clienteRepository.existsByIdentificacion(cliente.getIdentificacion())) {
            throw new DuplicateClienteException(
                    "Ya existe un cliente con identificacion: " + cliente.getIdentificacion()
            );
        }
    }

    private void validarDuplicadosEnActualizacion(ClienteRequest request, Long clienteId) {
        if (!clienteId.equals(request.clienteId())) {
            throw new DuplicateClienteException("clienteId del path y del body deben coincidir");
        }
        if (clienteRepository.existsByIdentificacionAndClienteIdNot(request.identificacion(), clienteId)) {
            throw new DuplicateClienteException(
                    "Ya existe un cliente con identificacion: " + request.identificacion()
            );
        }
    }
}
