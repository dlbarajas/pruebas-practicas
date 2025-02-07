package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Temporada;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.TemporadaRepository;
import com.parque.diversiones.backend.service.TemporadaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de temporada.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Temporada.
 */
@Service
public class TemporadaServiceImpl implements TemporadaService {
    private final TemporadaRepository temporadaRepository;

    public TemporadaServiceImpl(TemporadaRepository temporadaRepository) {
        this.temporadaRepository = temporadaRepository;
    }

    /**
     * Obtiene todas las temporadas que no han sido eliminados o que tienen estado igual a true.
     *
     * @return Lista de las temporadas.
     */
    @Override
    public List<Temporada> findByStateTrue() {
        return temporadaRepository.findAll()
                .stream()
                .filter(temporada -> Boolean.TRUE.equals(temporada.getEstado()))
                .toList();
    }

    @Override
    public Optional<Temporada> findById(Long id) {
        return temporadaRepository.findById(id)
                .map(temporada -> {
                    if (temporada.getEstado() != null && temporada.getEstado().equals(false)) {// Verifica si la temporada está inactivo
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return temporada;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public Temporada save(Temporada temporada) {
        try {
            temporada.setCreatedBy(1L);
            temporada.setCreatedAt(LocalDateTime.now());
            return temporadaRepository.save(temporada);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO + e.getMessage());
        }
    }

    @Override
    public Temporada update(Long id, Temporada temporada) {
        // Busca la temporada por ID
        Optional<Temporada> op = temporadaRepository.findById(id);

        // Valida si la temporada existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si la temporada está inhabilitado
        Temporada entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO,Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy"};

        // Copia las propiedades de la temporada recibido la temporada existente
        BeanUtils.copyProperties(temporada, entityUpdate, ignoreProperties);

        // Actualiza los campos de auditoría
        entityUpdate.setUpdatedBy(2L); // Cambia esto según el usuario que esté realizando la acción
        entityUpdate.setUpdatedAt(LocalDateTime.now());

        // Guarda los cambios en el repositorio y retorna la entidad actualizada
        return temporadaRepository.save(entityUpdate);
    }




    @Override
    public void delete(Long id) {
        Optional<Temporada> op = temporadaRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Temporada temporadaUpdate = op.get();
        temporadaUpdate.setDeletedBy(3L);
        temporadaUpdate.setEstado(false);
        temporadaUpdate.setDeletedAt(LocalDateTime.now());

        temporadaRepository.save(temporadaUpdate);
    }
}
