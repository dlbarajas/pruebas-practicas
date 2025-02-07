package com.parque.diversiones.backend.service;

import com.parque.diversiones.backend.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService{

    List<Cliente> findByStateTrue();
    public Optional<Cliente> findById(Long id);

    public Cliente save(Cliente cliente);

    public Cliente update(Long id, Cliente cliente);
    public void delete(Long id);
}
