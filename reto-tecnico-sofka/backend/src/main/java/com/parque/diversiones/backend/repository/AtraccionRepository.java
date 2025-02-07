package com.parque.diversiones.backend.repository;

import com.parque.diversiones.backend.entity.Atraccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtraccionRepository extends JpaRepository<Atraccion, Long> {

}
