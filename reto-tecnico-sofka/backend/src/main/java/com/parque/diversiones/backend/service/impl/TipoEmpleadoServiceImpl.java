package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.TipoEmpleado;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.TipoEmpleadoRepository;
import com.parque.diversiones.backend.service.TipoEmpleadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de TipoEmpleado.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad TipoEmpleado.
 */
@Service
public class TipoEmpleadoServiceImpl implements TipoEmpleadoService {

    private final TipoEmpleadoRepository tipoEmpleadoRepository;
    public TipoEmpleadoServiceImpl(TipoEmpleadoRepository tipoEmpleadoRepository) {
        this.tipoEmpleadoRepository = tipoEmpleadoRepository;
    }

    /**
     * Obtiene todos los tipos de empleados que no han sido eliminados o que tienen estado igual a true.
     *
     * @return Lista los tiposEmpledos.
     */
    @Override
    public List<TipoEmpleado> findByStateTrue() {
        return tipoEmpleadoRepository.findAll()
                .stream()
                .filter(tipoEmpleado -> Boolean.TRUE.equals(tipoEmpleado.getEstado()))
                .toList();
    }

    @Override
    public Optional<TipoEmpleado> findById(Long id) {
        return tipoEmpleadoRepository.findById(id)
                .map(tipoEmpleado -> {
                    if (tipoEmpleado.getEstado() != null && tipoEmpleado.getEstado().equals(false)) {// Verifica si el tipoEmpleado está inactivo
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return tipoEmpleado;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public TipoEmpleado save(TipoEmpleado tipoEmpleado) {
        try {
            tipoEmpleado.setCreatedBy(1L);
            tipoEmpleado.setCreatedAt(LocalDateTime.now());
            return tipoEmpleadoRepository.save(tipoEmpleado);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO+ e.getMessage());
        }
    }

    @Override
    public TipoEmpleado update(Long id, TipoEmpleado tipoEmpleado) {
        // Busca el tipoEmpleado por ID
        Optional<TipoEmpleado> op = tipoEmpleadoRepository.findById(id);

        // Valida si el tipoEmpleado existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si el tipoEmpleado está inhabilitado
        TipoEmpleado entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO,Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy"};

        // Copia las propiedades del tipoEmpleado recibido al tipoEmpleado existente
        BeanUtils.copyProperties(tipoEmpleado, entityUpdate, ignoreProperties);

        // Actualiza los campos de auditoría
        entityUpdate.setUpdatedBy(2L); // Cambia esto según el usuario que esté realizando la acción
        entityUpdate.setUpdatedAt(LocalDateTime.now());

        // Guarda los cambios en el repositorio y retorna la entidad actualizada
        return tipoEmpleadoRepository.save(entityUpdate);
    }




    @Override
    public void delete(Long id) {
        Optional<TipoEmpleado> op = tipoEmpleadoRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        TipoEmpleado tipoEmpleado = op.get();
        tipoEmpleado.setDeletedBy(3L);
        tipoEmpleado.setEstado(false);
        tipoEmpleado.setDeletedAt(LocalDateTime.now());

        tipoEmpleadoRepository.save(tipoEmpleado);
    }
}
