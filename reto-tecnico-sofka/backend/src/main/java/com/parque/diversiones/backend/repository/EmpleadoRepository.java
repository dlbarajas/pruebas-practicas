package com.parque.diversiones.backend.repository;

import com.parque.diversiones.backend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByNumeroIdentificacion(String numeroIdentificacion);
}
