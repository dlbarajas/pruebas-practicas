package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {
    List<Empleado> findByStateTrue();
    public Optional<Empleado> findById(Long id);

    public Empleado save(Empleado empleado);

    public Empleado update(Long id, Empleado empleado);
    public void delete(Long id);
}
