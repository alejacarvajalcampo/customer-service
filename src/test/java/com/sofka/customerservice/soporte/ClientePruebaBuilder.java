package com.sofka.customerservice.soporte;

import com.sofka.customerservice.domain.Cliente;
import com.sofka.customerservice.dto.ClienteRequest;

public class ClientePruebaBuilder {

    private Long clienteId = 1L;
    private String nombre = "Jose Lema";
    private String genero = "Masculino";
    private Integer edad = 30;
    private String identificacion = "1234567890";
    private String direccion = "Otavalo sn y principal";
    private String telefono = "098254785";
    private String contrasena = "1234";
    private Boolean estado = true;

    public static ClientePruebaBuilder unCliente() {
        return new ClientePruebaBuilder();
    }

    public ClientePruebaBuilder conClienteId(Long clienteId) {
        this.clienteId = clienteId;
        return this;
    }

    public ClientePruebaBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ClientePruebaBuilder conGenero(String genero) {
        this.genero = genero;
        return this;
    }

    public ClientePruebaBuilder conEdad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public ClientePruebaBuilder conIdentificacion(String identificacion) {
        this.identificacion = identificacion;
        return this;
    }

    public ClientePruebaBuilder conDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public ClientePruebaBuilder conTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public ClientePruebaBuilder conContrasena(String contrasena) {
        this.contrasena = contrasena;
        return this;
    }

    public ClientePruebaBuilder conEstado(Boolean estado) {
        this.estado = estado;
        return this;
    }

    public Cliente construir() {
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

    public ClienteRequest construirRequest() {
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
