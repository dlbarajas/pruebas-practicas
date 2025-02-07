package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Estacion;

import java.util.List;
import java.util.Optional;

public interface EstacionService {

    List<Estacion> findByStateTrue();
    public Optional<Estacion> findById(Long id);

    public Estacion save(Estacion estacion);

    public Estacion update(Long id, Estacion estacion);
    public void delete(Long id);
}
