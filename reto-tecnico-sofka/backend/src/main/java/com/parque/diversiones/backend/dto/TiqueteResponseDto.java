package com.parque.diversiones.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TiqueteResponseDto {
    private String fechaAdquisicion;
    private  Double precio;
    private Boolean estadoTiquete;
    private EstacionResponseAddMaquinasDto estacion;
    private ClienteResponseDto informacionClente;
    private EmpleadoResponseDto informacionEmpleado;
    private TemporadaResponseDto informacionTemporada;
}
