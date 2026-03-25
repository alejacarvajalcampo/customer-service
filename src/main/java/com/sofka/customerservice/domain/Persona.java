package com.sofka.customerservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long personaId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String genero;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 20)
    private String telefono;
}
