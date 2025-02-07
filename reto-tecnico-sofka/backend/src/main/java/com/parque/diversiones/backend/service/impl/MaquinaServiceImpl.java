package com.parque.diversiones.backend.service.impl;


import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Maquina;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.MaquinaRepository;
import com.parque.diversiones.backend.service.MaquinaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de maquina.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Maquina.
 */
@Service
public class MaquinaServiceImpl implements MaquinaService {
    private final MaquinaRepository maquinaRepository;

    public MaquinaServiceImpl(MaquinaRepository maquinaRepository) {
        this.maquinaRepository = maquinaRepository;
    }

    /**
     * Obtiene todas las maquinas que no han sido eliminadas o que tienen estado igual a true.
     *
     * @return Lista de maquina.
     */
    @Override
    public List<Maquina> findByStateTrue() {
        return maquinaRepository.findAll()
                .stream()
                .filter(maquina -> Boolean.TRUE.equals(maquina.getEstado()))
                .toList();
    }

    @Override
    public Optional<Maquina> findById(Long id) {
        return maquinaRepository.findById(id)
                .map(maquina -> {
                    if (maquina.getEstado() != null && maquina.getEstado().equals(false)) {// Verifica si la maquina está inactiva
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return maquina;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public Maquina save(Maquina maquina) {
        try {
            maquina.setCreatedBy(1L);
            maquina.setCreatedAt(LocalDateTime.now());
            return maquinaRepository.save(maquina);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO+ e.getMessage());
        }
    }

    @Override
    public Maquina update(Long id, Maquina maquina) {
        // Busca la maquina por ID
        Optional<Maquina> op = maquinaRepository.findById(id);

        // Valida si la maquina existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si la maquina está inhabilitada
        Maquina entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO,Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy"};

        // Copia las propiedades de la maquina recibida la maquina existente
        BeanUtils.copyProperties(maquina, entityUpdate, ignoreProperties);

        // Actualiza los campos de auditoría
        entityUpdate.setUpdatedBy(2L); // Cambia esto según el usuario que esté realizando la acción
        entityUpdate.setUpdatedAt(LocalDateTime.now());

        // Guarda los cambios en el repositorio y retorna la entidad actualizada
        return maquinaRepository.save(entityUpdate);
    }




    @Override
    public void delete(Long id) {
        Optional<Maquina> op = maquinaRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Maquina maquinaUpdate = op.get();
        maquinaUpdate.setDeletedBy(3L);
        maquinaUpdate.setEstado(false);
        maquinaUpdate.setDeletedAt(LocalDateTime.now());

        maquinaRepository.save(maquinaUpdate);
    }
}
