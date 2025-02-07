package com.parque.diversiones.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo maquina.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaquinaResponseDto {

    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("estadoDisponibilidad")
    private Boolean estadoDisponibilidad;
    @JsonProperty("motivoInhabilitacion")
    private String motivoInhabilitacion;
    @JsonProperty("estacionId")
    private Long estacionId;
    @JsonProperty("empleadoMantenimientoId")
    private Long empleadoMantenimientoId;

}
