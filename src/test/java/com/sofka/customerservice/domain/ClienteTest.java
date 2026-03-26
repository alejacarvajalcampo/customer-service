package com.sofka.customerservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sofka.customerservice.support.ClienteTestDataBuilder;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void shouldUpdateClienteData() {
        Cliente cliente = ClienteTestDataBuilder.unCliente().build();

        cliente.actualizarDatos(
                "Jose Lema Actualizado",
                "Masculino",
                31,
                "1234567890",
                "Nueva direccion",
                "098254786",
                "5678",
                false
        );

        assertEquals("Jose Lema Actualizado", cliente.getNombre());
        assertEquals(31, cliente.getEdad());
        assertEquals("Nueva direccion", cliente.getDireccion());
        assertEquals("5678", cliente.getContrasena());
        assertFalse(cliente.getEstado());
    }
}
