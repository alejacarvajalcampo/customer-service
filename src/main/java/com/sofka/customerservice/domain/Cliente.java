package com.sofka.customerservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Cliente extends Persona {

    @Column(name = "cliente_id", nullable = false, unique = true)
    private Long clienteId;

    @Column(nullable = false, length = 120)
    private String contrasena;

    @Column(nullable = false)
    private Boolean estado;

    public Cliente(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            Long clienteId,
            String contrasena,
            Boolean estado
    ) {
        setNombre(nombre);
        setGenero(genero);
        setEdad(edad);
        setIdentificacion(identificacion);
        setDireccion(direccion);
        setTelefono(telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public void actualizarDatos(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            Boolean estado
    ) {
        setNombre(nombre);
        setGenero(genero);
        setEdad(edad);
        setIdentificacion(identificacion);
        setDireccion(direccion);
        setTelefono(telefono);
        this.contrasena = contrasena;
        this.estado = estado;
    }
}
