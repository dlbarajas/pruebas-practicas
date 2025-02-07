package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.TipoEmpleado;

import java.util.List;
import java.util.Optional;

public interface TipoEmpleadoService {
    List<TipoEmpleado> findByStateTrue();
    public Optional<TipoEmpleado> findById(Long id);

    public TipoEmpleado save(TipoEmpleado tipoEmpleado);

    public TipoEmpleado update(Long id, TipoEmpleado tipoEmpleado);
    public void delete(Long id);
}
