package com.sofka.customerservice.repository;

import com.sofka.customerservice.domain.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByClienteId(Long clienteId);

    boolean existsByClienteId(Long clienteId);

    boolean existsByIdentificacion(String identificacion);

    boolean existsByIdentificacionAndClienteIdNot(String identificacion, Long clienteId);
}
