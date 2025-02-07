package com.parque.diversiones.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo temporada.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemporadaResponseDto {
    private String nombre;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String fechaInicio;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String fechaFin;
    private String descripcion;
    private Boolean estadoDisponibilidad;
}
