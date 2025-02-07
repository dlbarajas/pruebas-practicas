package com.parque.diversiones.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo atraccion.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtraccionResponseDto {
    private String nombre;
    private String descripcion;
    private String clasificacion;
    private String condicionesUso;
    private Boolean estadoAtraccion;
}
