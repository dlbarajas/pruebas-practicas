package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Empleado;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.EmpleadoRepository;
import com.parque.diversiones.backend.service.EmpleadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de empleado.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Empleado.
 */
@Service
public class EmpleadoServiceImpl implements EmpleadoService {
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Obtiene todos los empleados que no han sido eliminados o que tienen estado igual a true.
     *
     * @return Lista de empleados.
     */
    @Override
    public List<Empleado> findByStateTrue() {
        return empleadoRepository.findAll()
                .stream()
                .filter(empleado -> Boolean.TRUE.equals(empleado.getEstado()))
                .toList();
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        return empleadoRepository.findById(id)
                .map(empleado -> {
                    if (empleado.getEstado() != null && empleado.getEstado().equals(false)) {// Verifica si el empleado está inactivo
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return empleado;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }

    @Override
    public Empleado save(Empleado empleado) {
        try {
            empleado.setCreatedBy(1L);
            empleado.setCreatedAt(LocalDateTime.now());
            return empleadoRepository.save(empleado);
        } catch (Exception e) {
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO+ e.getMessage());
        }
    }

    @Override
    public Empleado update(Long id, Empleado empleado) {
        // Busca el empleado por ID
        Optional<Empleado> op = empleadoRepository.findById(id);

        // Valida si el empleado existe
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        // Valida si el empleado está inhabilitado
        Empleado entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO,Constantes.REGISTRO_INHABILITADO);
        }

        // Define las propiedades que no se deben actualizar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy"};

        // Copia las propiedades del empleado recibido al empleado existente
        BeanUtils.copyProperties(empleado, entityUpdate, ignoreProperties);

        // Actualiza los campos de auditoría
        entityUpdate.setUpdatedBy(2L); // Cambia esto según el usuario que esté realizando la acción
        entityUpdate.setUpdatedAt(LocalDateTime.now());

        // Guarda los cambios en el repositorio y retorna la entidad actualizada
        return empleadoRepository.save(entityUpdate);
    }




    @Override
    public void delete(Long id) {
        Optional<Empleado> op = empleadoRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Empleado empleadoUpdate = op.get();
        empleadoUpdate.setDeletedBy(3L);
        empleadoUpdate.setEstado(false);
        empleadoUpdate.setDeletedAt(LocalDateTime.now());

        empleadoRepository.save(empleadoUpdate);
    }
}
