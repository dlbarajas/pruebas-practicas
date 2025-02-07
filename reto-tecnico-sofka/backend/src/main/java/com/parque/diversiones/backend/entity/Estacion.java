package com.parque.diversiones.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estaciones")
public class Estacion extends BaseEntity{
    @Column(name = "nombre", length = 60, nullable = false)
    private String nombre;
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;
    @Column(name = "estado_disponibilidad", nullable = false)
    private Boolean estadoDisponibilidad;
    @Column(name = "porcentaje_ocupacion", nullable = false)
    private Double porcentajeOcupacion;

    // Lista de días de la semana en que la estación puede estar habilitada.
    // Relación para almacenar los días habilitados en una tabla secundaria
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "dias_habilitacion",
            joinColumns = @JoinColumn(name = "estacion_id", nullable = false)
    )
    @Column(name = "dia", nullable = false)
    @Enumerated(EnumType.STRING) // Asegura que los enums se almacenen como texto en la tabla
    private List<DayOfWeek> diasHabilitacion;

    @ManyToMany
    @JoinTable(
            name = "estacion_temporada",
            joinColumns = @JoinColumn(name = "estacion_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "temporada_id")
    )
    private List<Temporada> temporadas; // Temporadas en las que la estación puede operar.

    // Relación con Atracciones
    @OneToMany(mappedBy = "estacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atraccion> atracciones;

}
