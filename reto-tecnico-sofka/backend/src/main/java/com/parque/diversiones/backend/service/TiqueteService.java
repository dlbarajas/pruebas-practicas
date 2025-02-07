package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Tiquete;

import java.util.List;
import java.util.Optional;

public interface TiqueteService {
    List<Tiquete> findByStateTrue();
    public Optional<Tiquete> findById(Long id);

    public Tiquete save(Tiquete tiquete);

    public Tiquete update(Long id, Tiquete tiquete);
    public void delete(Long id);
}
