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
public class EstacionResponseAddMaquinasDto {
    private String nombre;
    private String descripcion;
    private Boolean estadoDisponibilidad;
    private Double porcentajeOcupacion;
    private List<String> diasHabilitacion;
    private List<MaquinaRecibidaDto> maquinasDeLaEstacion;

}
