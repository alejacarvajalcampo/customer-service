package com.sofka.customerservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.dto.ClienteRequest;
import com.sofka.customerservice.exception.DuplicateClienteException;
import com.sofka.customerservice.repository.ClienteRepository;
import com.sofka.customerservice.soporte.ClientePruebaBuilder;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteOutboxService clienteOutboxService;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deberiaCrearClienteYRegistrarEventoDeUpsert() {
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();

        when(clienteRepository.existsByClienteId(1L)).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.crear(cliente);

        assertSame(cliente, resultado);
        verify(clienteOutboxService).registerUpsert(cliente);
    }

    @Test
    void deberiaRechazarCreacionSiElClienteIdYaExiste() {
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();
        when(clienteRepository.existsByClienteId(1L)).thenReturn(true);

        assertThrows(DuplicateClienteException.class, () -> clienteService.crear(cliente));

        verify(clienteRepository, never()).save(cliente);
        verify(clienteOutboxService, never()).registerUpsert(cliente);
    }

    @Test
    void deberiaRechazarCreacionSiLaIdentificacionYaExiste() {
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();
        when(clienteRepository.existsByClienteId(1L)).thenReturn(false);
        when(clienteRepository.existsByIdentificacion("1234567890")).thenReturn(true);

        assertThrows(DuplicateClienteException.class, () -> clienteService.crear(cliente));

        verify(clienteRepository, never()).save(cliente);
    }

    @Test
    void deberiaActualizarClienteYPublicarUpsert() {
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();
        ClienteRequest request = ClientePruebaBuilder.unCliente()
                .conNombre("Maria Jose")
                .conDireccion("Nueva direccion 123")
                .conTelefono("099999999")
                .conContrasena("5678")
                .conEstado(false)
                .construirRequest();

        when(clienteRepository.findByClienteId(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdentificacionAndClienteIdNot("1234567890", 1L)).thenReturn(false);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.actualizar(1L, request);

        assertSame(cliente, resultado);
        assertEquals("Maria Jose", resultado.getNombre());
        assertEquals("Nueva direccion 123", resultado.getDireccion());
        assertEquals("099999999", resultado.getTelefono());
        assertEquals("5678", resultado.getContrasena());
        assertEquals(false, resultado.getEstado());
        verify(clienteOutboxService).registerUpsert(cliente);
    }

    @Test
    void deberiaRechazarActualizacionSiElClienteIdNoCoincide() {
        ClienteRequest request = ClientePruebaBuilder.unCliente().conClienteId(2L).construirRequest();
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();

        when(clienteRepository.findByClienteId(1L)).thenReturn(Optional.of(cliente));

        assertThrows(DuplicateClienteException.class, () -> clienteService.actualizar(1L, request));

        verify(clienteRepository, never()).save(cliente);
        verify(clienteOutboxService, never()).registerUpsert(cliente);
    }

    @Test
    void deberiaRechazarActualizacionSiLaIdentificacionPerteneceAOtroCliente() {
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();
        ClienteRequest request = ClientePruebaBuilder.unCliente().construirRequest();

        when(clienteRepository.findByClienteId(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByIdentificacionAndClienteIdNot("1234567890", 1L)).thenReturn(true);

        assertThrows(DuplicateClienteException.class, () -> clienteService.actualizar(1L, request));

        verify(clienteRepository, never()).save(cliente);
    }

    @Test
    void deberiaEliminarClienteYRegistrarEventoDelete() {
        Cliente cliente = ClientePruebaBuilder.unCliente().construir();
        when(clienteRepository.findByClienteId(1L)).thenReturn(Optional.of(cliente));

        clienteService.eliminar(1L);

        verify(clienteOutboxService).registerDelete(cliente);
        verify(clienteRepository).delete(cliente);
    }
}
