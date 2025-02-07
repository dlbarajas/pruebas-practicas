package com.parque.diversiones.backend.repository;

import com.parque.diversiones.backend.entity.Familiar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamiliarRepository extends JpaRepository<Familiar,Long> {
    Optional<Familiar> findByNumeroIdentificacion(String numeroIdentificacion);
}
