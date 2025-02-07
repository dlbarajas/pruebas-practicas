package com.parque.diversiones.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo cliente.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDto {
    private String nombre;
    private String apellido;
    private String email;
    private Integer edad;
    private Double estatura;
    private String numeroIdentificacion;
    private String telefono;
    private List<FamiliarResponseDto> familiares;

}
