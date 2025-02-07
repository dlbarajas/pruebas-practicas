package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.*;
import com.parque.diversiones.backend.entity.Empleado;
import com.parque.diversiones.backend.entity.Estacion;
import com.parque.diversiones.backend.entity.Maquina;
import com.parque.diversiones.backend.repository.EmpleadoRepository;
import com.parque.diversiones.backend.repository.EstacionRepository;
import com.parque.diversiones.backend.service.MaquinaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con maquina.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar maquina.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class MaquinaController {

    private final MaquinaService maquinaService;
    private final EstacionRepository estacionRepository;
    private final EmpleadoRepository empleadoRepository;

    /**
     * Constructor que inicializa el servicio de la maquina.
     *
     * @param maquinaService     instancia del servicio de maquina
     * @param estacionRepository
     * @param empleadoRepository
     */
    public MaquinaController(MaquinaService maquinaService, EstacionRepository estacionRepository, EmpleadoRepository empleadoRepository) {
        this.maquinaService = maquinaService;
        this.estacionRepository = estacionRepository;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Obtiene una lista de todos las maquinas con estado activo.
     *
     * @return ResponseEntity con una lista de las maquinas encapsuladas en un ApiResponseDto
     */
    @GetMapping("maquinas")
    public ResponseEntity<ApiResponseDto<List<MaquinaRecibidaDto>>> listarTodosLasMaquinas() {
        // Obtén la lista de máquinas con estado true
        List<Maquina> maquinas = maquinaService.findByStateTrue();

        // Mapea la lista de entidades Maquina a MaquinaRecibidaDto
        List<MaquinaRecibidaDto> maquinasResponseDto = maquinas.stream()
                .map(maquina -> {
                    // Obtener los datos relacionados de estación
                    EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                            .nombre(maquina.getEstacion().getNombre())
                            .descripcion(maquina.getEstacion().getDescripcion())
                            .estadoDisponibilidad(maquina.getEstacion().getEstadoDisponibilidad())
                            .porcentajeOcupacion(maquina.getEstacion().getPorcentajeOcupacion())
                            .diasHabilitacion(
                                    maquina.getEstacion().getDiasHabilitacion().stream()
                                            .map(DayOfWeek::name)
                                            .collect(Collectors.toList())
                            )
                            .build();

                    // Obtener los datos relacionados del empleado de mantenimiento
                    EmpleadoResponseDto empleadoResponseDto = EmpleadoResponseDto.builder()
                            .nombre(maquina.getEmpleadoMantenimiento().getNombre())
                            .apellido(maquina.getEmpleadoMantenimiento().getApellido())
                            .telefono(maquina.getEmpleadoMantenimiento().getTelefono())
                            .email(maquina.getEmpleadoMantenimiento().getEmail())
                            .edad(maquina.getEmpleadoMantenimiento().getEdad())
                            .numeroIdentificacion(maquina.getEmpleadoMantenimiento().getNumeroIdentificacion())
                            .horarioLaboral(maquina.getEmpleadoMantenimiento().getHorarioLaboral())
                            .tipoEmpleadoId(maquina.getEmpleadoMantenimiento().getTipoEmpleado().getId())
                            .build();

                    // Crear el DTO de respuesta para la máquina
                    return MaquinaRecibidaDto.builder()
                            .nombre(maquina.getNombre())
                            .descripcion(maquina.getDescripcion())
                            .estadoDisponibilidad(maquina.getEstadoDisponibilidad())
                            .motivoInhabilitacion(maquina.getMotivoInhabilitacion())
                            .estacion(estacionResponseDto)
                            .empleadoMantenimiento(empleadoResponseDto)
                            .build();
                })
                .toList();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_OBTENIDOS, maquinasResponseDto, true));
    }


    /**
     * Obtiene una maquina específico por su ID.
     *
     * @param id Identificador único de la maquina
     * @return ResponseEntity con la maquina solicitada encapsulada en un ApiResponseDto
     */
    @GetMapping("maquinas/{id}")
    public ResponseEntity<ApiResponseDto<MaquinaRecibidaDto>> obtenerMaquinaPorId(@PathVariable Long id) {
        // Busca la entidad Maquina por ID
        Maquina maquina = maquinaService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Obtener los datos relacionados de estación
        EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                .nombre(maquina.getEstacion().getNombre())
                .descripcion(maquina.getEstacion().getDescripcion())
                .estadoDisponibilidad(maquina.getEstacion().getEstadoDisponibilidad())
                .porcentajeOcupacion(maquina.getEstacion().getPorcentajeOcupacion())
                .diasHabilitacion(
                        maquina.getEstacion().getDiasHabilitacion().stream()
                                .map(DayOfWeek::name)
                                .collect(Collectors.toList())
                )
                .build();

        // Obtener los datos relacionados del empleado de mantenimiento
        EmpleadoResponseDto empleadoResponseDto = EmpleadoResponseDto.builder()
                .nombre(maquina.getEmpleadoMantenimiento().getNombre())
                .apellido(maquina.getEmpleadoMantenimiento().getApellido())
                .telefono(maquina.getEmpleadoMantenimiento().getTelefono())
                .email(maquina.getEmpleadoMantenimiento().getEmail())
                .edad(maquina.getEmpleadoMantenimiento().getEdad())
                .numeroIdentificacion(maquina.getEmpleadoMantenimiento().getNumeroIdentificacion())
                .horarioLaboral(maquina.getEmpleadoMantenimiento().getHorarioLaboral())
                .tipoEmpleadoId(maquina.getEmpleadoMantenimiento().getTipoEmpleado().getId())
                .build();

        // Construir el DTO de respuesta
        MaquinaRecibidaDto maquinaResponseDto = MaquinaRecibidaDto.builder()
                .nombre(maquina.getNombre())
                .descripcion(maquina.getDescripcion())
                .estadoDisponibilidad(maquina.getEstadoDisponibilidad())
                .motivoInhabilitacion(maquina.getMotivoInhabilitacion())
                .estacion(estacionResponseDto)
                .empleadoMantenimiento(empleadoResponseDto)
                .build();

        // Responder con el DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ENCONTRADO, maquinaResponseDto, true));
    }


    /**
     * Guarda un nueva maquina.
     *
     * @param maquina Objeto Maquina a guardar
     * @return ResponseEntity con el maquina guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("maquinas")
    public ResponseEntity<ApiResponseDto<MaquinaRecibidaDto>> guardarMaquina(@RequestBody MaquinaResponseDto maquina) {

        // Busca el tipo de empleado por su ID
        Estacion tipoEstacionAlQuePertenece = estacionRepository.findById(maquina.getEstacionId())
                .orElseThrow(() -> new EntityNotFoundException("Estacion no encontrada en base de datos."));

        Empleado empleadoMantenimiento = empleadoRepository.findById(maquina.getEmpleadoMantenimientoId())
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado en base de datos."));

        Maquina maquinaRecibida = new Maquina();
        maquinaRecibida.setNombre(maquina.getNombre());
        maquinaRecibida.setDescripcion(maquina.getDescripcion());
        maquinaRecibida.setEstadoDisponibilidad(maquina.getEstadoDisponibilidad());
        maquinaRecibida.setEstacion(tipoEstacionAlQuePertenece);
        maquinaRecibida.setEmpleadoMantenimiento(empleadoMantenimiento);
        maquinaRecibida.setCreatedAt(LocalDateTime.now());
        maquinaRecibida.setCreatedBy((long) 1);
        maquinaRecibida.setEstado(true);
        Maquina maquinaAGuardar = maquinaService.save(maquinaRecibida);

        EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                .nombre(tipoEstacionAlQuePertenece.getNombre())
                .descripcion(tipoEstacionAlQuePertenece.getDescripcion())
                .estadoDisponibilidad(tipoEstacionAlQuePertenece.getEstadoDisponibilidad())
                .porcentajeOcupacion(tipoEstacionAlQuePertenece.getPorcentajeOcupacion())
                .diasHabilitacion(
                        tipoEstacionAlQuePertenece.getDiasHabilitacion().stream()
                                .map(DayOfWeek::name)
                                .collect(Collectors.toList())
                )
                .build();

        EmpleadoResponseDto empleadoResponseDto = EmpleadoResponseDto.builder()
                .nombre(empleadoMantenimiento.getNombre())
                .apellido(empleadoMantenimiento.getApellido())
                .telefono(empleadoMantenimiento.getTelefono())
                .email(empleadoMantenimiento.getEmail())
                .edad(empleadoMantenimiento.getEdad())
                .numeroIdentificacion(empleadoMantenimiento.getNumeroIdentificacion())
                .horarioLaboral(empleadoMantenimiento.getHorarioLaboral())
                .tipoEmpleadoId(empleadoMantenimiento.getTipoEmpleado().getId())
                .build();

        // Mapea la entidad a un DTO
        MaquinaRecibidaDto maquinaResponseDto = MaquinaRecibidaDto.builder()
                .nombre(maquina.getNombre())
                .descripcion(maquinaAGuardar.getDescripcion())
                .estadoDisponibilidad(maquinaAGuardar.getEstadoDisponibilidad())
                .motivoInhabilitacion(maquinaAGuardar.getMotivoInhabilitacion())
                .estacion(estacionResponseDto)
                .empleadoMantenimiento(empleadoResponseDto)
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, maquinaResponseDto, true));
    }

    /**
     * Actualiza una maquina existente por su ID.
     *
     * @param id      Identificador único de la maquina
     * @param maquina Objeto Maquina con los datos actualizados
     * @return ResponseEntity con el maquina actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("maquinas/{id}")
    public ResponseEntity<ApiResponseDto<MaquinaRecibidaDto>> actualizarMaquina(
            @PathVariable Long id,
            @RequestBody MaquinaResponseDto maquina) {

        // Busca la máquina existente
        Maquina maquinaExistente = maquinaService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Actualiza los campos básicos
        maquinaExistente.setNombre(maquina.getNombre());
        maquinaExistente.setDescripcion(maquina.getDescripcion());
        maquinaExistente.setEstadoDisponibilidad(maquina.getEstadoDisponibilidad());
        maquinaExistente.setMotivoInhabilitacion(maquina.getMotivoInhabilitacion());

        // Verifica y actualiza la relación de estación si el ID es diferente
        if (!maquinaExistente.getEstacion().getId().equals(maquina.getEstacionId())) {
            Estacion nuevaEstacion = estacionRepository.findById(maquina.getEstacionId())
                    .orElseThrow(() -> new EntityNotFoundException("Estación no encontrada en base de datos."));
            maquinaExistente.setEstacion(nuevaEstacion);
        }

        // Verifica y actualiza la relación de empleado si el ID es diferente
        if (!maquinaExistente.getEmpleadoMantenimiento().getId().equals(maquina.getEmpleadoMantenimientoId())) {
            Empleado nuevoEmpleado = empleadoRepository.findById(maquina.getEmpleadoMantenimientoId())
                    .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado en base de datos."));
            maquinaExistente.setEmpleadoMantenimiento(nuevoEmpleado);
        }

        // Actualiza los datos en la base de datos
        Maquina maquinaActualizada = maquinaService.save(maquinaExistente);

        // Construye los DTOs para la respuesta
        EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                .nombre(maquinaActualizada.getEstacion().getNombre())
                .descripcion(maquinaActualizada.getEstacion().getDescripcion())
                .estadoDisponibilidad(maquinaActualizada.getEstacion().getEstadoDisponibilidad())
                .porcentajeOcupacion(maquinaActualizada.getEstacion().getPorcentajeOcupacion())
                .diasHabilitacion(
                        maquinaActualizada.getEstacion().getDiasHabilitacion().stream()
                                .map(DayOfWeek::name)
                                .collect(Collectors.toList())
                )
                .build();

        EmpleadoResponseDto empleadoResponseDto = EmpleadoResponseDto.builder()
                .nombre(maquinaActualizada.getEmpleadoMantenimiento().getNombre())
                .apellido(maquinaActualizada.getEmpleadoMantenimiento().getApellido())
                .telefono(maquinaActualizada.getEmpleadoMantenimiento().getTelefono())
                .email(maquinaActualizada.getEmpleadoMantenimiento().getEmail())
                .edad(maquinaActualizada.getEmpleadoMantenimiento().getEdad())
                .numeroIdentificacion(maquinaActualizada.getEmpleadoMantenimiento().getNumeroIdentificacion())
                .horarioLaboral(maquinaActualizada.getEmpleadoMantenimiento().getHorarioLaboral())
                .tipoEmpleadoId(maquinaActualizada.getEmpleadoMantenimiento().getTipoEmpleado().getId())
                .build();

        MaquinaRecibidaDto maquinaResponseDto = MaquinaRecibidaDto.builder()
                .nombre(maquinaActualizada.getNombre())
                .descripcion(maquinaActualizada.getDescripcion())
                .estadoDisponibilidad(maquinaActualizada.getEstadoDisponibilidad())
                .motivoInhabilitacion(maquinaActualizada.getMotivoInhabilitacion())
                .estacion(estacionResponseDto)
                .empleadoMantenimiento(empleadoResponseDto)
                .build();

        // Retorna la respuesta con el DTO
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, maquinaResponseDto, true));
    }

    /**
     * Elimina una maquina por su ID.
     *
     * @param id Identificador único de la maquina
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("maquinas/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarMaquina(@PathVariable Long id) {
        maquinaService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }
}
