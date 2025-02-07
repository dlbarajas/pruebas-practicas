package com.parque.diversiones.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empleados")
public class Empleado extends BaseEntity{
    @Column(name = "nombre", length = 60, nullable = false)
    private String nombre;
    @Column(name = "apellido", length = 60, nullable = false)
    private String apellido;
    @Column(name = "email", length = 60, nullable = false)
    private String email;
    @Column(name = "edad", nullable = false)
    private Integer edad;
    @Column(name = "numero_identificacion", length = 12, nullable = false, unique = true)
    private String numeroIdentificacion;
    @Column(name = "telefono", length = 10, nullable = false)
    private String telefono;
    @Column(name = "horario_laboral", length = 50, nullable = false)
    private String horarioLaboral;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_empleado_id", nullable = false)
    private TipoEmpleado tipoEmpleado;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tiquete> tiquetes = new ArrayList<>(); // Relación con los tiquetes vendidos

}
