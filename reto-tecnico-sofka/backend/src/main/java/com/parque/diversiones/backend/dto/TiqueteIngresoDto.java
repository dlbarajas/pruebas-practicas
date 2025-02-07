package com.parque.diversiones.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define un modelo de atributos para transferencia de datos de tipo tiquete.
 *
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TiqueteIngresoDto {
    @JsonProperty("fechaAdquisicion")
    private String fechaAdquisicion;
    @JsonProperty("precio")
    private  Double precio;
    @JsonProperty("estadoTiquete")
    private Boolean estadoTiquete;
    @JsonProperty("idEstacion")
    private Long idEstacion;
    @JsonProperty("idTemporada")
    private Long idTemporada;
    @JsonProperty("numeroIdentificacionEmpleado")
    private String numeroIdentificacionEmpleado;
    @JsonProperty("numeroIdentificacionCliente")
    private String numeroIdentificacionCliente;
}
