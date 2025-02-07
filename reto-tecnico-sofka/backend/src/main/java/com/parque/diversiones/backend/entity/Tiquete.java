package com.parque.diversiones.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tiquetes")
public class Tiquete{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "estado_tiquete", nullable = true)
    private Boolean estadoTiquete;

    @Column(name = "fecha_adquisicion", nullable = false)
    private String fechaAdquisicion;
    @ManyToOne
    @JoinColumn(name = "estacion_id", nullable = false)
    private Estacion estacion;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(name = "precio", nullable = false)
    private  Double precio;

}
