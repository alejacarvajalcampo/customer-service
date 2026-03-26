package com.sofka.customerservice.support;

import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.dto.ClienteRequest;

public class ClienteTestDataBuilder {

    private Long clienteId = 1L;
    private String nombre = "Jose Lema";
    private String genero = "Masculino";
    private Integer edad = 30;
    private String identificacion = "1234567890";
    private String direccion = "Otavalo sn y principal";
    private String telefono = "098254785";
    private String contrasena = "1234";
    private Boolean estado = true;

    public static ClienteTestDataBuilder unCliente() {
        return new ClienteTestDataBuilder();
    }

    public ClienteTestDataBuilder conClienteId(Long clienteId) {
        this.clienteId = clienteId;
        return this;
    }

    public ClienteTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ClienteTestDataBuilder conGenero(String genero) {
        this.genero = genero;
        return this;
    }

    public ClienteTestDataBuilder conEdad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public ClienteTestDataBuilder conIdentificacion(String identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public ClienteTestDataBuilder conDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public ClienteTestDataBuilder conTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public ClienteTestDataBuilder conContrasena(String contrasena) {
        this.contrasena = contrasena;
        return this;
    }

    public ClienteTestDataBuilder conEstado(Boolean estado) {
        this.estado = estado;
        return this;
    }

    public Cliente build() {
        return new Cliente(
                nombre,
                genero,
                edad,
                identificacion,
                direccion,
                telefono,
                clienteId,
                contrasena,
                estado
        );
    }

    public ClienteRequest buildRequest() {
        return new ClienteRequest(
                clienteId,
                nombre,
                genero,
                edad,
                identificacion,
                direccion,
                telefono,
                contrasena,
                estado
        );
    }
}
