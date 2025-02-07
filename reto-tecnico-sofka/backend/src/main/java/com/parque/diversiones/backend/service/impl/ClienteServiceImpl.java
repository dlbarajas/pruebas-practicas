package com.parque.diversiones.backend.service.impl;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.entity.Cliente;
import com.parque.diversiones.backend.entity.Familiar;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.ClienteRepository;
import com.parque.diversiones.backend.repository.FamiliarRepository;
import com.parque.diversiones.backend.service.ClienteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de clientes.
 * Proporciona métodos para realizar operaciones CRUD sobre la entidad Cliente.
 */
@Service
public class ClienteServiceImpl implements ClienteService {


    private final ClienteRepository clienteRepository;
    private final FamiliarRepository familiarRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, FamiliarRepository familiarRepository) {
        this.clienteRepository = clienteRepository;
        this.familiarRepository = familiarRepository;
    }

    /**
     * Obtiene todos los clientes que no han sido eliminados o que tienen estado igual a true.
     *
     * @return Lista de clientes.
     */
    @Override
    public List<Cliente> findByStateTrue() {
        return clienteRepository.findAll()
                .stream()
                .filter(cliente -> Boolean.TRUE.equals(cliente.getEstado()))
                .toList();
    }


    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    if (cliente.getEstado() != null && cliente.getEstado().equals(false)) {// Verifica si el cliente está inactivo
                        throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_INACTIVO_O_ELIMINADO + id);
                    }
                    return cliente;
                })
                .or(() -> {
                    throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_ID_NO_ENCONTRADO + id);
                });
    }


    @Override
    public Cliente save(Cliente cliente) {

        try {
            // Valida si el cliente tiene familiares en caso de ser menor de edad
            if (cliente.getEdad() < 18 && (cliente.getFamiliares() == null || cliente.getFamiliares().isEmpty())) {
                throw new GeneralException(GeneralException.Tipo.VALOR_NO_VALIDO, Constantes.EDAD_NO_VALIDA);
            }

            // Relaciona los familiares con el cliente
            if (cliente.getFamiliares() != null) {
                cliente.getFamiliares().forEach(familiar -> familiar.setCliente(cliente));
            }
            cliente.setCreatedBy(1L);
            cliente.setCreatedAt(LocalDateTime.now());
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            if (e.getMessage().equals(Constantes.EDAD_NO_VALIDA)) {
                throw new GeneralException(GeneralException.Tipo.VALOR_NO_VALIDO, Constantes.EDAD_NO_VALIDA);
            }
            throw new GeneralException(GeneralException.Tipo.ERROR, Constantes.ERROR_GUARDAR_REGISTRO + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Cliente update(Long id, Cliente cliente) {
        // Busca el cliente por ID
        Optional<Cliente> op = clienteRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Cliente entityUpdate = op.get();
        if (entityUpdate.getDeletedAt() != null) {
            throw new GeneralException(GeneralException.Tipo.INHABILITADO, Constantes.REGISTRO_INHABILITADO);
        }

        // Copia las propiedades ignorando campos que no deben cambiar
        String[] ignoreProperties = {"id", "createdAt", "deletedAt", "createdBy", "deletedBy", "familiares"};
        BeanUtils.copyProperties(cliente, entityUpdate, ignoreProperties);

        // Manejo de familiares
        if (cliente.getFamiliares() != null) {
            // Mapear los familiares actuales del cliente en un Map por numeroIdentificacion
            Map<String, Familiar> familiaresExistentes = entityUpdate.getFamiliares().stream()
                    .collect(Collectors.toMap(Familiar::getNumeroIdentificacion, familiar -> familiar));

            // Procesar la lista de familiares enviada en el request
            List<Familiar> familiaresActualizados = new ArrayList<>();

            for (Familiar familiarNuevo : cliente.getFamiliares()) {
                Familiar familiarProcesado;

                if (familiaresExistentes.containsKey(familiarNuevo.getNumeroIdentificacion())) {
                    // Si el familiar ya existe, actualiza las propiedades necesarias
                    familiarProcesado = familiaresExistentes.get(familiarNuevo.getNumeroIdentificacion());
                    BeanUtils.copyProperties(familiarNuevo, familiarProcesado, "id", "cliente", "numeroIdentificacion");
                } else {
                    // Si no existe, verifica en la base de datos por numeroIdentificacion
                    Optional<Familiar> familiarBD = familiarRepository.findByNumeroIdentificacion(familiarNuevo.getNumeroIdentificacion());
                    if (familiarBD.isPresent()) {
                        // Si el familiar se encuentra, actualiza las propiedades
                        familiarProcesado = familiarBD.get();
                        BeanUtils.copyProperties(familiarNuevo, familiarProcesado, "id", "cliente", "numeroIdentificacion");
                    } else {
                        // Si no existe en BD, crea uno nuevo
                        familiarProcesado = new Familiar();
                        BeanUtils.copyProperties(familiarNuevo, familiarProcesado, "id");
                    }
                    // Asocia el cliente al nuevo familiar
                    familiarProcesado.setCliente(entityUpdate);
                }

                familiaresActualizados.add(familiarProcesado);
            }

            // Elimina familiares que ya no están en la lista
            entityUpdate.getFamiliares().removeIf(familiar ->
                    familiaresActualizados.stream().noneMatch(f -> f.getNumeroIdentificacion().equals(familiar.getNumeroIdentificacion()))
            );

            // Agrega o actualiza los familiares en la lista del cliente
            for (Familiar familiar : familiaresActualizados) {
                if (!entityUpdate.getFamiliares().contains(familiar)) {
                    entityUpdate.getFamiliares().add(familiar);
                }
            }
        }

        // Guarda el cliente actualizado
        return clienteRepository.save(entityUpdate);
    }












    @Override
    public void delete(Long id) {
        Optional<Cliente> op = clienteRepository.findById(id);

        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Cliente clienteUpdate = op.get();
        clienteUpdate.setDeletedBy(3L);
        clienteUpdate.setEstado(false);
        clienteUpdate.setDeletedAt(LocalDateTime.now());

        clienteRepository.save(clienteUpdate);
    }
}
