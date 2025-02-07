package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Atraccion;

import java.util.List;
import java.util.Optional;

public interface AtraccionService {
    List<Atraccion> findByStateTrue();
    public Optional<Atraccion> findById(Long id);

    public Atraccion save(Atraccion atraccion);

    public Atraccion update(Long id, Atraccion atraccion);
    public void delete(Long id);
}
