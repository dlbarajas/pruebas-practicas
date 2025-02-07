package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Tiquete;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.TiqueteRepository;
import com.parque.diversiones.backend.service.TiqueteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Tiquete.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Tiquete.
 */
@Service
public class TiqueteServiceImpl implements TiqueteService {
    private final TiqueteRepository tiqueteRepository;

    public TiqueteServiceImpl(TiqueteRepository tiqueteRepository) {
        this.tiqueteRepository = tiqueteRepository;
    }

    /**
     * Obtiene todas las temporadas que no han sido eliminadas o que tienen estado igual a true.
     *
     * @return Lista de temporada.
     */
    @Override
    public List<Tiquete> findByStateTrue() {
        return tiqueteRepository.findAll()
                .stream()
                .filter(tiquete -> Boolean.TRUE.equals(tiquete.getEstadoTiquete()))
                .toList();
    }

    @Override
    public Optional<Tiquete> findById(Long id) {
        return tiqueteRepository.findById(id)
                .map(tiquete -> {
                    if (tiquete.getEstadoTiquete() != null && tiquete.getEstadoTiquete().equals(false)) {// Verifica si la tiquete está inactiva
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return tiquete;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public Tiquete save(Tiquete tiquete) {
        try {
            return tiqueteRepository.save(tiquete);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO + e.getMessage());
        }
    }

    @Override
    public Tiquete update(Long id, Tiquete tiquete) {
        // Busca la tiquete por ID
        Optional<Tiquete> op = tiqueteRepository.findById(id);

        // Valida si la tiquete existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si la tiquete está inhabilitado
        Tiquete entityUpdate = op.get();
        if (entityUpdate.getEstadoTiquete() .equals(false)) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO, Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "estado"};

        // Copia las propiedades de la tiquete recibida la tiquete existente
        BeanUtils.copyProperties(tiquete, entityUpdate, ignoreProperties);
        return tiqueteRepository.save(entityUpdate);
    }

    @Override
    public void delete(Long id) {
        Optional<Tiquete> op = tiqueteRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }
        Tiquete tiqueteUpdate = op.get();
        tiqueteUpdate.setEstadoTiquete(false);
        tiqueteRepository.save(tiqueteUpdate);
    }
}
