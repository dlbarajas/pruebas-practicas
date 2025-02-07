package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Atraccion;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.AtraccionRepository;
import com.parque.diversiones.backend.service.AtraccionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de atracciones.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Atraccion.
 */
@Service
public class AtraccionServiceImpl implements AtraccionService {
    private final AtraccionRepository atraccionRepository;
    public AtraccionServiceImpl(AtraccionRepository atraccionRepository) {
        this.atraccionRepository = atraccionRepository;
    }

    /**
     * Obtiene todos las atracciones que no han sido eliminados o que tienen estado igual a true.
     *
     * @return Lista de atracciones.
     */
    @Override
    public List<Atraccion> findByStateTrue() {
        return atraccionRepository.findAll()
                .stream()
                .filter(atraccion -> Boolean.TRUE.equals(atraccion.getEstado()))
                .toList();
    }

    @Override
    public Optional<Atraccion> findById(Long id) {
        return atraccionRepository.findById(id)
                .map(atraccion -> {
                    if (atraccion.getEstado() != null && atraccion.getEstado().equals(false)) {// Verifica si la atraccion está inactivo
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return atraccion;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public Atraccion save(Atraccion atraccion) {
        try {
            atraccion.setCreatedBy(1L);
            atraccion.setCreatedAt(LocalDateTime.now());
            return atraccionRepository.save(atraccion);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO+ e.getMessage());
        }
    }

    @Override
    public Atraccion update(Long id, Atraccion atraccion) {
        // Busca la atraccion por ID
        Optional<Atraccion> op = atraccionRepository.findById(id);

        // Valida si la atraccion existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si la atraccion está inhabilitada
        Atraccion entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO,Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy"};

        // Copia las propiedades de la Atraccion recibido a la atraccion existente
        BeanUtils.copyProperties(atraccion, entityUpdate, ignoreProperties);

        // Actualiza los campos de auditoría
        entityUpdate.setUpdatedBy(2L); // Cambia esto según el usuario que esté realizando la acción
        entityUpdate.setUpdatedAt(LocalDateTime.now());

        // Guarda los cambios en el repositorio y retorna la entidad actualizada
        return atraccionRepository.save(entityUpdate);
    }

    @Override
    public void delete(Long id) {
        Optional<Atraccion> op = atraccionRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Atraccion atraccionteUpdate = op.get();
        atraccionteUpdate.setDeletedBy(3L);
        atraccionteUpdate.setEstado(false);
        atraccionteUpdate.setDeletedAt(LocalDateTime.now());

        atraccionRepository.save(atraccionteUpdate);
    }
}
