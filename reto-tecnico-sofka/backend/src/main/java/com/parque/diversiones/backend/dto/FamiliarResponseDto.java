package com.parque.diversiones.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo Familiar.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamiliarResponseDto {
    private String numeroIdentificacion;
    private String nombre;
    private String apellido;
    private String telefono;
    private String relacion;
    private String email;
}
