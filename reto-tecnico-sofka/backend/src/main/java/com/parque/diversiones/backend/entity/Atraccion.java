package com.parque.diversiones.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "atracciones")
public class Atraccion extends BaseEntity{
    @Column(name = "nombre", length = 60, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;
    @Column(name = "clasificacion", length = 500, nullable = false)
    private String clasificacion;
    @Column(name = "condiciones_uso", length = 500, nullable = false)
    private String condicionesUso;
    @Column(name = "estado_atraccion", length = 500, nullable = false)
    private Boolean estadoAtraccion;

    // Relación con Estación
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacion_id", nullable = false)
    private Estacion estacion;

    //empleado responsable

}
