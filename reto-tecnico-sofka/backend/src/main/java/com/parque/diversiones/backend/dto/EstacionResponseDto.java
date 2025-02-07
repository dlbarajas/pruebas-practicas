package com.parque.diversiones.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo estacion.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstacionResponseDto {

    private String nombre;
    private String descripcion;
    private Boolean estadoDisponibilidad;
    private Double porcentajeOcupacion;
    private List<String> diasHabilitacion;

}
