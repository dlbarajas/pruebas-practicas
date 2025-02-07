package com.parque.diversiones.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstacionEnviadaDto {

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("estadoDisponibilidad")
    private Boolean estadoDisponibilidad;

    @JsonProperty("porcentajeOcupacion")
    private Double porcentajeOcupacion;

    @JsonProperty("diasHabilitacion")
    private List<DayOfWeek> diasHabilitacion;
}
