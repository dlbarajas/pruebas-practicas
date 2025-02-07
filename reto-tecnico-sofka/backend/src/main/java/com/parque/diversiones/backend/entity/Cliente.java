package com.parque.diversiones.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
public class Cliente extends BaseEntity {
    @Column(name = "nombre", length = 60, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 60, nullable = false)
    private String apellido;

    @Column(name = "email", length = 60, nullable = false)
    private String email;

    @Column(name = "edad", nullable = false)
    private Integer edad;

    @Column(name = "estatura", nullable = false)
    private Double estatura;

    @Column(name = "numero_identificacion", length = 60, nullable = false, unique = true)
    private String numeroIdentificacion;

    @Column(name = "telefono", length = 10, nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tiquete> tiquetes = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Familiar> familiares = new ArrayList<>();

}
