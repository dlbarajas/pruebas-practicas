package com.parque.diversiones.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "temporadas")
public class Temporada extends BaseEntity{
   @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_inicio",nullable = false)
    private String fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_fin",nullable = false)
    private String fechaFin;

    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;
    @Column(name = "estado_disponibilidad", nullable = false)
    private Boolean estadoDisponibilidad;
}
