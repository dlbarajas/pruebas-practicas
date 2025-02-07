package com.parque.diversiones.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tipo_empleados")
public class TipoEmpleado extends BaseEntity {
    @Column(name = "cargo", length = 60, nullable = false)
    private String cargo;

    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

}
