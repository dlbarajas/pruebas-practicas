package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.ApiResponseDto;
import com.parque.diversiones.backend.dto.ClienteResponseDto;
import com.parque.diversiones.backend.dto.FamiliarResponseDto;
import com.parque.diversiones.backend.entity.Cliente;
import com.parque.diversiones.backend.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con clientes.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar clientes.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Constructor que inicializa el servicio de clientes.
     *
     * @param clienteService instancia del servicio de clientes
     */
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Obtiene una lista de todos los clientes con estado activo.
     *
     * @return ResponseEntity con una lista de clientes encapsulada en un ApiResponseDto
     */
    @GetMapping("clientes")
    public ResponseEntity<ApiResponseDto<List<ClienteResponseDto>>> listarTodosLosClientes() {
        // Obtén la lista de clientes con estado true
        List<Cliente> clientes = clienteService.findByStateTrue();

        // Mapea la lista de entidades Cliente a ClienteResponseDto
        List<ClienteResponseDto> clienteResponseDto = clientes.stream()
                .map(cliente -> {
                    // Mapea los familiares específicos de este cliente
                    List<FamiliarResponseDto> familiaresDto = cliente.getFamiliares() != null
                            ? cliente.getFamiliares().stream()
                            .map(familiar -> FamiliarResponseDto.builder()
                                    .numeroIdentificacion(familiar.getNumeroIdentificacion())
                                    .nombre(familiar.getNombre())
                                    .apellido(familiar.getApellido())
                                    .telefono(familiar.getTelefono())
                                    .relacion(familiar.getRelacion())
                                    .email(familiar.getEmail())
                                    .build())
                            .toList()
                            : Collections.emptyList();

                    // Construye el ClienteResponseDto
                    return ClienteResponseDto.builder()
                            .nombre(cliente.getNombre())
                            .apellido(cliente.getApellido())
                            .email(cliente.getEmail())
                            .edad(cliente.getEdad())
                            .estatura(cliente.getEstatura())
                            .numeroIdentificacion(cliente.getNumeroIdentificacion())
                            .telefono(cliente.getTelefono())
                            .familiares(familiaresDto)
                            .build();
                })
                .toList();

        return ResponseEntity.ok(new ApiResponseDto<>(
                Constantes.DATOS_OBTENIDOS,
                clienteResponseDto,
                true
        ));
    }


    /**
     * Obtiene un cliente específico por su ID.
     *
     * @param id Identificador único del cliente
     * @return ResponseEntity con el cliente solicitado encapsulado en un ApiResponseDto
     */
    @GetMapping("clientes/{id}")
    public ResponseEntity<ApiResponseDto<ClienteResponseDto>> obtenerClientePorId(@PathVariable Long id) {
        // Busca la entidad por ID
        Cliente cliente = clienteService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Mapea los familiares al DTO
        List<FamiliarResponseDto> familiaresDto = cliente.getFamiliares().stream()
                .map(familiar -> FamiliarResponseDto.builder()
                        .numeroIdentificacion(familiar.getNumeroIdentificacion())
                        .nombre(familiar.getNombre())
                        .apellido(familiar.getApellido())
                        .telefono(familiar.getTelefono())
                        .relacion(familiar.getRelacion())
                        .email(familiar.getEmail())
                        .build())
                .toList();

        // Mapea la entidad a un DTO
        ClienteResponseDto clienteResponseDto = ClienteResponseDto.builder()
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .email(cliente.getEmail())
                .edad(cliente.getEdad())
                .estatura(cliente.getEstatura())
                .numeroIdentificacion(cliente.getNumeroIdentificacion())
                .telefono(cliente.getTelefono())
                .familiares(familiaresDto)
                .build();
        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<ClienteResponseDto>(Constantes.REGISTRO_ENCONTRADO, clienteResponseDto, true));
    }

    /**
     * Guarda un nuevo cliente.
     *
     * @param cliente Objeto Cliente a guardar
     * @return ResponseEntity con el cliente guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("clientes")
    public ResponseEntity<ApiResponseDto<ClienteResponseDto>> guardarCliente(@RequestBody Cliente cliente) {
        cliente.setCreatedAt(LocalDateTime.now());
        cliente.setCreatedBy((long) 1);
        cliente.setEstado(true);

        Cliente clienteAGuardar = clienteService.save(cliente);

        // Mapea los familiares al DTO
        List<FamiliarResponseDto> familiaresDto = clienteAGuardar.getFamiliares().stream()
                .map(familiar -> FamiliarResponseDto.builder()
                        .numeroIdentificacion(familiar.getNumeroIdentificacion())
                        .nombre(familiar.getNombre())
                        .apellido(familiar.getApellido())
                        .telefono(familiar.getTelefono())
                        .relacion(familiar.getRelacion())
                        .email(familiar.getEmail())
                        .build())
                .toList();

        // Mapea la entidad a un DTO
        ClienteResponseDto clienteResponseDto = ClienteResponseDto.builder()
                .nombre(clienteAGuardar.getNombre())
                .apellido(clienteAGuardar.getApellido())
                .email(clienteAGuardar.getEmail())
                .edad(clienteAGuardar.getEdad())
                .estatura(clienteAGuardar.getEstatura())
                .numeroIdentificacion(clienteAGuardar.getNumeroIdentificacion())
                .telefono(clienteAGuardar.getTelefono())
                .familiares(familiaresDto)
                .build();

        // Enviamos mensaje de promoción si es mayor de edad
        String mensaje = Constantes.DATOS_GUARDADOS;
        if (clienteAGuardar.getEdad() >= 18) {
            mensaje += " " + Constantes.MENSAJE_PROMOCION;
        }

        return ResponseEntity.ok(new ApiResponseDto<>(mensaje, clienteResponseDto, true));
    }

    /**
     * Actualiza un cliente existente por su ID.
     *
     * @param id      Identificador único del cliente
     * @param cliente Objeto Cliente con los datos actualizados
     * @return ResponseEntity con el cliente actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("clientes/{id}")
    public ResponseEntity<ApiResponseDto<ClienteResponseDto>> actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        // Actualiza el estado del registro
        cliente.setEstado(true);
        Cliente clienteActualizado = clienteService.update(id, cliente);

        // Mapea los familiares al DTO
        List<FamiliarResponseDto> familiaresDto = clienteActualizado.getFamiliares().stream()
                .map(familiar -> FamiliarResponseDto.builder()
                        .numeroIdentificacion(familiar.getNumeroIdentificacion())
                        .nombre(familiar.getNombre())
                        .apellido(familiar.getApellido())
                        .telefono(familiar.getTelefono())
                        .relacion(familiar.getRelacion())
                        .email(familiar.getEmail())
                        .build())
                .toList();

        // Mapea la entidad actualizada a un DTO
        ClienteResponseDto clienteResponseDto = ClienteResponseDto.builder()
                .nombre(clienteActualizado.getNombre())
                .apellido(clienteActualizado.getApellido())
                .email(clienteActualizado.getEmail())
                .edad(clienteActualizado.getEdad())
                .estatura(clienteActualizado.getEstatura())
                .numeroIdentificacion(clienteActualizado.getNumeroIdentificacion())
                .telefono(clienteActualizado.getTelefono())
                .familiares(familiaresDto)
                .build();

        // Retorna la respuesta con el DTO
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, clienteResponseDto, true));
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id Identificador único del cliente
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("clientes/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarCliente(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }


}
