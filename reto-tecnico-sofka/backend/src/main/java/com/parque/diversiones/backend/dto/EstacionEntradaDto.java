package com.parque.diversiones.backend.dto;

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
public class EstacionEntradaDto {

    private String nombre;
    private String descripcion;
    private Boolean estadoDisponibilidad;
    private Double porcentajeOcupacion;
    private List<DayOfWeek> diasHabilitacion;
    private String identificacionEmpleado;
}
