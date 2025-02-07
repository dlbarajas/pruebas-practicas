package com.parque.diversiones.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo tipoEmpleado.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEmpleadoResponseDto {
    private String cargo;
    private String descripcion;
}
