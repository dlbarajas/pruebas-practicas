package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.ApiResponseDto;
import com.parque.diversiones.backend.dto.EmpleadoResponseDto;
import com.parque.diversiones.backend.dto.TipoEmpleadoResponseDto;
import com.parque.diversiones.backend.dto.VerEmpleadoDto;
import com.parque.diversiones.backend.entity.Empleado;
import com.parque.diversiones.backend.entity.TipoEmpleado;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.EmpleadoRepository;
import com.parque.diversiones.backend.repository.TipoEmpleadoRepository;
import com.parque.diversiones.backend.service.EmpleadoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones relacionadas con empleado.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar empleado.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class EmpleadoController {

    private final EmpleadoService empleadoService;
    private final TipoEmpleadoRepository tipoEmpleadoRepository;
    private final EmpleadoRepository empleadoRepository;

    /**
     * Constructor que inicializa el servicio de empleado.
     *
     * @param empleadoService        instancia del servicio de empleado
     * @param tipoEmpleadoRepository
     * @param empleadoRepository
     */
    public EmpleadoController(EmpleadoService empleadoService, TipoEmpleadoRepository tipoEmpleadoRepository, EmpleadoRepository empleadoRepository) {
        this.empleadoService = empleadoService;
        this.tipoEmpleadoRepository = tipoEmpleadoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Obtiene una lista de todos los empleados con estado activo.
     *
     * @return ResponseEntity con una lista de empleados encapsulada en un ApiResponseDto
     */
    @GetMapping("empleados")
    public ResponseEntity<ApiResponseDto<List<VerEmpleadoDto>>> listarTodosLosEmpleados() {
        // Obtén la lista de empleados con estado true
        List<Empleado> empleados = empleadoService.findByStateTrue();

        // Mapea la lista de entidades empleado a EmpleadoResponseDto, incluyendo información completa del tipo de empleado
        List<VerEmpleadoDto> verEmpleadoDtoList = empleados.stream()
                .map(empleado -> {
                    // Construye el DTO del tipo de empleado
                    TipoEmpleadoResponseDto tipoEmpleadoResponseDto = TipoEmpleadoResponseDto.builder()
                            .cargo(empleado.getTipoEmpleado().getCargo())
                            .descripcion(empleado.getTipoEmpleado().getDescripcion())
                            .build();

                    // Construye el DTO del empleado
                    return VerEmpleadoDto.builder()
                            .nombre(empleado.getNombre())
                            .apellido(empleado.getApellido())
                            .email(empleado.getEmail())
                            .edad(empleado.getEdad())
                            .numeroIdentificacion(empleado.getNumeroIdentificacion())
                            .telefono(empleado.getTelefono())
                            .horarioLaboral(empleado.getHorarioLaboral())
                            .tipoEmpleado(tipoEmpleadoResponseDto) // Incluye el DTO del tipo de empleado
                            .build();
                })
                .toList();

        // Retorna la lista de empleados en la respuesta
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_OBTENIDOS, verEmpleadoDtoList, true));
    }

    /**
     * Obtiene un empleado específico por su ID.
     *
     * @param id Identificador único del empleado
     * @return ResponseEntity con el empleado solicitado encapsulado en un ApiResponseDto
     */
    @GetMapping("empleados/{id}")
    public ResponseEntity<ApiResponseDto<VerEmpleadoDto>> obtenerEmpleadoPorId(@PathVariable Long id) {
        // Busca la entidad por ID
        Empleado empleado = empleadoService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        TipoEmpleadoResponseDto tipoEmpleadoResponseDto = TipoEmpleadoResponseDto.builder()
                .cargo(empleado.getTipoEmpleado().getCargo())
                .descripcion(empleado.getTipoEmpleado().getDescripcion())
                .build();

        // Mapea la entidad a un DTO
        VerEmpleadoDto verEmpleadoDto = VerEmpleadoDto.builder()
                .nombre(empleado.getNombre())
                .apellido(empleado.getApellido())
                .email(empleado.getEmail())
                .edad(empleado.getEdad())
                .numeroIdentificacion(empleado.getNumeroIdentificacion())
                .telefono(empleado.getTelefono())
                .horarioLaboral(empleado.getHorarioLaboral())
                .tipoEmpleado(tipoEmpleadoResponseDto)
                .build();
        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<VerEmpleadoDto>(Constantes.REGISTRO_ENCONTRADO, verEmpleadoDto, true));
    }

    /**
     * Guarda un nuevo empleado.
     *
     * @param empleadoRecibido Objeto Empleado a guardar
     * @return ResponseEntity con el empleado guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("empleados")
    public ResponseEntity<ApiResponseDto<EmpleadoResponseDto>> guardarEmpleado(@RequestBody EmpleadoResponseDto empleadoRecibido) {

        // Busca el tipo de empleado por su ID
        TipoEmpleado tipoEmpleado = tipoEmpleadoRepository.findById(empleadoRecibido.getTipoEmpleadoId())
                .orElseThrow(() -> new EntityNotFoundException("TipoEmpleado no encontrado"));

        Empleado empleado = new Empleado();
        empleado.setNombre(empleadoRecibido.getNombre());
        empleado.setApellido(empleadoRecibido.getApellido());
        empleado.setEmail(empleadoRecibido.getEmail());
        empleado.setEdad(empleadoRecibido.getEdad());
        empleado.setNumeroIdentificacion(empleadoRecibido.getNumeroIdentificacion());
        empleado.setTelefono(empleadoRecibido.getTelefono());
        empleado.setHorarioLaboral(empleadoRecibido.getHorarioLaboral());
        empleado.setTipoEmpleado(tipoEmpleado);

        empleado.setCreatedAt(LocalDateTime.now());
        empleado.setCreatedBy((long) 1);
        empleado.setEstado(true);
        Empleado empleadoAGuardar = empleadoService.save(empleado);

        // Mapea la entidad a un DTO
        EmpleadoResponseDto empleadoResponseDto = EmpleadoResponseDto.builder()
                .nombre(empleadoAGuardar.getNombre())
                .apellido(empleadoAGuardar.getApellido())
                .email(empleadoAGuardar.getEmail())
                .edad(empleadoAGuardar.getEdad())
                .numeroIdentificacion(empleadoAGuardar.getNumeroIdentificacion())
                .telefono(empleadoAGuardar.getTelefono())
                .horarioLaboral(empleado.getHorarioLaboral())
                .tipoEmpleadoId(empleado.getTipoEmpleado().getId())
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, empleadoResponseDto, true));
    }








    /**
     * Actualiza un empleado existente por su ID.
     *
     * @param id      Identificador único del empleado
     * @param empleadoResponseDto Objeto Empleado con los datos actualizados
     * @return ResponseEntity con el empleado actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("empleados/{id}")
    public ResponseEntity<ApiResponseDto<EmpleadoResponseDto>> actualizarEmpleado(
            @PathVariable Long id,
            @RequestBody EmpleadoResponseDto empleadoResponseDto) {

        // Busca el empleado por ID
        Optional<Empleado> op = empleadoRepository.findById(id);
        if (op.isEmpty()) {
            throw new GeneralException(GeneralException.Tipo.NOT_FOUND, Constantes.REGISTRO_NO_ENCONTRADO);
        }

        Empleado empleadoExistente = op.get();

        // Si el numeroIdentificacion enviado es diferente al actual, verificar duplicidad
        if (!empleadoResponseDto.getNumeroIdentificacion().equals(empleadoExistente.getNumeroIdentificacion())) {
            Optional<Empleado> empleadoConMismoNumeroIdentificacion = empleadoRepository.findByNumeroIdentificacion(
                    empleadoResponseDto.getNumeroIdentificacion());

            // Si existe un empleado con ese número de identificación y no es el mismo empleado, lanzamos excepción
            if (empleadoConMismoNumeroIdentificacion.isPresent()
                    && !empleadoConMismoNumeroIdentificacion.get().getId().equals(id)) {
                throw new GeneralException(GeneralException.Tipo.VALOR_NO_VALIDO,
                        "El número de identificación ya está siendo utilizado por otro empleado.");
            }

            // Actualizamos el numeroIdentificacion solo si no hay conflicto
            empleadoExistente.setNumeroIdentificacion(empleadoResponseDto.getNumeroIdentificacion());
        }

        // Actualizamos los demás campos del empleado
        empleadoExistente.setNombre(empleadoResponseDto.getNombre());
        empleadoExistente.setApellido(empleadoResponseDto.getApellido());
        empleadoExistente.setEmail(empleadoResponseDto.getEmail());
        empleadoExistente.setEdad(empleadoResponseDto.getEdad());
        empleadoExistente.setTelefono(empleadoResponseDto.getTelefono());
        empleadoExistente.setHorarioLaboral(empleadoResponseDto.getHorarioLaboral());

        // Verifica si el tipoEmpleadoId es válido y actualiza el tipo de empleado
        if (empleadoResponseDto.getTipoEmpleadoId() != null) {
            Optional<TipoEmpleado> tipoEmpleadoOpt = tipoEmpleadoRepository.findById(empleadoResponseDto.getTipoEmpleadoId());
            if (tipoEmpleadoOpt.isEmpty()) {
                throw new GeneralException(GeneralException.Tipo.NOT_FOUND, "Tipo de empleado no encontrado.");
            }
            empleadoExistente.setTipoEmpleado(tipoEmpleadoOpt.get());
        }

        // Actualiza el estado del registro
        empleadoExistente.setEstado(true);

        // Guarda los cambios
        Empleado empleadoActualizado = empleadoRepository.save(empleadoExistente);

        // Mapea la entidad actualizada a un DTO de respuesta
        EmpleadoResponseDto empleadoResponseDtoRespuesta = EmpleadoResponseDto.builder()
                .nombre(empleadoActualizado.getNombre())
                .apellido(empleadoActualizado.getApellido())
                .email(empleadoActualizado.getEmail())
                .edad(empleadoActualizado.getEdad())
                .numeroIdentificacion(empleadoActualizado.getNumeroIdentificacion())
                .telefono(empleadoActualizado.getTelefono())
                .horarioLaboral(empleadoActualizado.getHorarioLaboral())
                .tipoEmpleadoId(empleadoActualizado.getTipoEmpleado() != null ? empleadoActualizado.getTipoEmpleado().getId() : null)
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, empleadoResponseDtoRespuesta, true));
    }





    /**
     * Elimina un empleado por su ID.
     *
     * @param id Identificador único del empleado
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("empleados/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarEmpleado(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }


}
