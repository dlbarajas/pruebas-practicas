package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Maquina;

import java.util.List;
import java.util.Optional;

public interface MaquinaService {
    List<Maquina> findByStateTrue();
    public Optional<Maquina> findById(Long id);

    public Maquina save(Maquina maquina);

    public Maquina update(Long id, Maquina maquina);
    public void delete(Long id);
}
