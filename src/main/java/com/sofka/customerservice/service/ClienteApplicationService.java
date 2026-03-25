package com.sofka.customerservice.service;

import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.dto.ClienteRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteApplicationService {

    Cliente crear(Cliente cliente);

    Page<Cliente> listar(Pageable pageable);

    Cliente obtenerPorClienteId(Long clienteId);

    Cliente actualizar(Long clienteId, ClienteRequest request);

    void eliminar(Long clienteId);
}
