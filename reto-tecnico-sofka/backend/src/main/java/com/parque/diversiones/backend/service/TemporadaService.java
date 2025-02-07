package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Temporada;

import java.util.List;
import java.util.Optional;

public interface TemporadaService {
    List<Temporada> findByStateTrue();
    public Optional<Temporada> findById(Long id);

    public Temporada save(Temporada temporada);

    public Temporada update(Long id, Temporada temporada);
    public void delete(Long id);
}
