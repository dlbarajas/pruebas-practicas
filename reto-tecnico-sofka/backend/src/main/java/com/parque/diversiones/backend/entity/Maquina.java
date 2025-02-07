package com.parque.diversiones.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maquinas")
public class Maquina extends BaseEntity{

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

    @Column(name = "estado_disponibilidad",  nullable = false)
    private Boolean estadoDisponibilidad;

    @Column(name = "motivo_inhabilitacion", length = 500)
    private String motivoInhabilitacion;

    @ManyToOne
    @JoinColumn(name = "estacion_id", nullable = false)
    private Estacion estacion;

    // Relación con el empleado de mantenimiento
    @ManyToOne
    @JoinColumn(name = "empleado_mantenimiento_id")
    private Empleado empleadoMantenimiento;


    //condiciones de uso
    //numero de ingresos
}
