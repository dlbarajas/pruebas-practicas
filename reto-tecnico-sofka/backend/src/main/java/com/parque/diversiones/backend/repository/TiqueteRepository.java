package com.parque.diversiones.backend.repository;

import com.parque.diversiones.backend.entity.Tiquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiqueteRepository  extends JpaRepository<Tiquete, Long> {
}
