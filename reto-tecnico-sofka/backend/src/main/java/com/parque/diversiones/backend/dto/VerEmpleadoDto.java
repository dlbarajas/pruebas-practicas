package com.parque.diversiones.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo empleado.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerEmpleadoDto {
    private String nombre;
    private String apellido;
    private String email;
    private Integer edad;
    private String numeroIdentificacion;
    private String telefono;
    private String horarioLaboral;
    private TipoEmpleadoResponseDto tipoEmpleado;
}
