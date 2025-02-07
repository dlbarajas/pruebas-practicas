package com.parque.diversiones.backend.repository;

import com.parque.diversiones.backend.entity.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface EstacionRepository extends JpaRepository<Estacion, Long> {

    //Consulta por estaciones disponibles en un día específico
    @Query("SELECT e FROM Estacion e JOIN e.diasHabilitacion d WHERE d = :dia")
    List<Estacion> findByDiaHabilitacion(@Param("dia") DayOfWeek dia);

    //Consulta los días habilitados para una estación específica
    @Query("SELECT e.diasHabilitacion FROM Estacion e WHERE e.id = :estacionId")
    List<DayOfWeek> findDiasHabilitacionByEstacionId(@Param("estacionId") Long estacionId);



}
