package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Estacion;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.EstacionRepository;
import com.parque.diversiones.backend.service.EstacionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de estacion.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Estacion.
 */
@Service
public class EstacionServiceImpl implements EstacionService {
    private final EstacionRepository estacionRepository;

    public EstacionServiceImpl(EstacionRepository estacionRepository) {
        this.estacionRepository = estacionRepository;
    }

    /**
     * Obtiene todas las estacines que no han sido eliminados o que tienen estado igual a true.
     *
     * @return Lista de estaciones.
     */
    @Override
    public List<Estacion> findByStateTrue() {
        return estacionRepository.findAll()
                .stream()
                .filter(estacion -> Boolean.TRUE.equals(estacion.getEstado()))
                .toList();
    }

    @Override
    public Optional<Estacion> findById(Long id) {
        return estacionRepository.findById(id)
                .map(estacion -> {
                    if (estacion.getEstado() != null && estacion.getEstado().equals(false)) {// Verifica si la estacion está inactivo
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return estacion;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public Estacion save(Estacion estacion) {
        try {
            estacion.setCreatedBy(1L);
            estacion.setCreatedAt(LocalDateTime.now());
            return estacionRepository.save(estacion);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO+ e.getMessage());
        }
    }

    @Override
    public Estacion update(Long id, Estacion estacion) {
        // Busca la estacion por ID
        Optional<Estacion> op = estacionRepository.findById(id);

        // Valida si la estacion existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si la estacion está inhabilitado
        Estacion entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO,Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy"};

        // Copia las propiedades de la estacion recibido la eestacion existente
        BeanUtils.copyProperties(estacion, entityUpdate, ignoreProperties);

        // Actualiza los campos de auditoría
        entityUpdate.setUpdatedBy(2L); // Cambia esto según el usuario que esté realizando la acción
        entityUpdate.setUpdatedAt(LocalDateTime.now());

        // Guarda los cambios en el repositorio y retorna la entidad actualizada
        return estacionRepository.save(entityUpdate);
    }




    @Override
    public void delete(Long id) {
        Optional<Estacion> op = estacionRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Estacion estacionUpdate = op.get();
        estacionUpdate.setDeletedBy(3L);
        estacionUpdate.setEstado(false);
        estacionUpdate.setDeletedAt(LocalDateTime.now());

        estacionRepository.save(estacionUpdate);
    }

}
