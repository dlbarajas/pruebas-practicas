package com.parque.diversiones.backend.repository;

import com.parque.diversiones.backend.entity.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    List<Maquina> findByEstacionId(Long idEstacion);
}
