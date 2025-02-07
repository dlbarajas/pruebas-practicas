package com.parque.diversiones.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaquinaRecibidaDto {

    private String nombre;
    private String descripcion;
    private Boolean estadoDisponibilidad;
    private String motivoInhabilitacion;
    //private Estacion estacion;
    //private Empleado empleadoMantenimiento;

    private EstacionResponseDto estacion;
    private EmpleadoResponseDto empleadoMantenimiento;


}
