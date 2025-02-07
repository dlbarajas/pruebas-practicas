package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.ApiResponseDto;
import com.parque.diversiones.backend.dto.EstacionEntradaDto;
import com.parque.diversiones.backend.dto.EstacionResponseDto;
import com.parque.diversiones.backend.entity.Empleado;
import com.parque.diversiones.backend.entity.Estacion;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.EmpleadoRepository;
import com.parque.diversiones.backend.service.EstacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con estacion.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar estacion.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class EstacionController {
    private final EstacionService estacionService;
    private final EmpleadoRepository empleadoRepository;

    /**
     * Constructor que inicializa el servicio de la estacion.
     *
     * @param estacionService    instancia del servicio de estacion
     * @param empleadoRepository
     */
    public EstacionController(EstacionService estacionService, EmpleadoRepository empleadoRepository) {
        this.estacionService = estacionService;
        this.empleadoRepository = empleadoRepository;
    }

    /**
     * Obtiene una lista de todos las estaciones con estado activo.
     *
     * @return ResponseEntity con una lista de estaciones encapsulada en un ApiResponseDto
     */
    @GetMapping("estaciones")
    public ResponseEntity<ApiResponseDto<List<EstacionResponseDto>>> listarTodosLasEstaciones() {
        // Obtén la lista de estaciones con estado true
        List<Estacion> estaciones = estacionService.findByStateTrue();

        // Mapea la lista de entidades estaciones a EstacionResponseDto
        List<EstacionResponseDto> estacionResponseDto = estaciones.stream()
                .map(estacion -> EstacionResponseDto.builder()
                        .nombre(estacion.getNombre())
                        .descripcion(estacion.getDescripcion())
                        .estadoDisponibilidad(estacion.getEstado())
                        .porcentajeOcupacion(estacion.getPorcentajeOcupacion())
                        .diasHabilitacion(
                                estacion.getDiasHabilitacion().stream()
                                        .map(DayOfWeek::name)
                                        .collect(Collectors.toList())
                        )
                        .build()
                )
                .toList();
        return ResponseEntity.ok(new ApiResponseDto<List<EstacionResponseDto>>(Constantes.DATOS_OBTENIDOS, estacionResponseDto, true));
    }

    /**
     * Obtiene una estacion específico por su ID.
     *
     * @param id Identificador único de la estacion
     * @return ResponseEntity con la estacion solicitada encapsulada en un ApiResponseDto
     */
    @GetMapping("estaciones/{id}")
    public ResponseEntity<ApiResponseDto<EstacionResponseDto>> obtenerEstacionPorId(@PathVariable Long id) {
        // Busca la entidad por ID
        Estacion estacion = estacionService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Mapea la entidad a un DTO
        EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                .nombre(estacion.getNombre())
                .descripcion(estacion.getDescripcion())
                .estadoDisponibilidad(estacion.getEstado())
                .porcentajeOcupacion(estacion.getPorcentajeOcupacion())
                .diasHabilitacion(
                        estacion.getDiasHabilitacion().stream()
                                .map(DayOfWeek::name)
                                .collect(Collectors.toList())
                )
                .build();
        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<EstacionResponseDto>(Constantes.REGISTRO_ENCONTRADO, estacionResponseDto, true));
    }

    /**
     * Guarda un nueva estacion.
     *
     * @param estacion Objeto Estacion a guardar
     * @return ResponseEntity con el estacion guardado encapsulado en un ApiResponseDto
     */
    @PostMapping("estaciones")
    public ResponseEntity<ApiResponseDto<EstacionResponseDto>> guardarEstacion(@RequestBody EstacionEntradaDto estacion) {

        // Busca el tipo de empleado por su ID
        Empleado empleadoCreacionTiquete = empleadoRepository.findByNumeroIdentificacion(estacion.getIdentificacionEmpleado())
                .orElseThrow(() -> new GeneralException(GeneralException.Tipo.NOT_FOUND, "Empleado no registrado en base de datos."));

        if(empleadoCreacionTiquete.getTipoEmpleado().getCargo() != null
                && !empleadoCreacionTiquete.getTipoEmpleado().getCargo().isEmpty()
                && !empleadoCreacionTiquete.getTipoEmpleado().getCargo().equals("ADMINISTRATIVO")){

            throw new GeneralException(GeneralException.Tipo.UNAUTHORIZED,
                    Constantes.PERMISOS_INSUFICIENTES +
                            empleadoCreacionTiquete.getNumeroIdentificacion() + " no cuenta con permisos para la creación de nuevas estaciones.");
        }




        if (estacion.getDiasHabilitacion() != null && !estacion.getDiasHabilitacion().isEmpty()) {
            estacion.setDiasHabilitacion(estacion.getDiasHabilitacion());
        }

        Estacion estacionRecibida = new Estacion();
        estacionRecibida.setNombre(estacion.getNombre());
        estacionRecibida.setDescripcion(estacion.getDescripcion());
        estacionRecibida.setEstadoDisponibilidad(estacion.getEstadoDisponibilidad());
        estacionRecibida.setPorcentajeOcupacion(estacion.getPorcentajeOcupacion());
        estacionRecibida.setDiasHabilitacion(estacion.getDiasHabilitacion());
        estacionRecibida.setCreatedAt(LocalDateTime.now());
        estacionRecibida.setCreatedBy((long) 1);
        estacionRecibida.setEstado(true);

        Estacion estacionAGuardar = estacionService.save(estacionRecibida);

        // Mapeo de la entidad a DTO para devolver la respuesta
        EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                .nombre(estacionAGuardar.getNombre())
                .descripcion(estacionAGuardar.getDescripcion())
                .estadoDisponibilidad(estacionAGuardar.getEstadoDisponibilidad())
                .porcentajeOcupacion(estacionAGuardar.getPorcentajeOcupacion())
                .diasHabilitacion(
                        estacionAGuardar.getDiasHabilitacion().stream()
                                .map(DayOfWeek::name)
                                .collect(Collectors.toList())
                )
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, estacionResponseDto, true));
    }

    /**
     * Actualiza una estacion existente por su ID.
     *
     * @param id      Identificador único de la estacion
     * @param estacion Objeto Estacion con los datos actualizados
     * @return ResponseEntity con el estacion actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("estaciones/{id}")
    public ResponseEntity<ApiResponseDto<EstacionResponseDto>> actualizarEstacion(@PathVariable Long id, @RequestBody EstacionEntradaDto estacion) {

        // Busca el tipo de empleado por su ID
        Empleado empleadoCreacionTiquete = empleadoRepository.findByNumeroIdentificacion(estacion.getIdentificacionEmpleado())
                .orElseThrow(() -> new GeneralException(GeneralException.Tipo.NOT_FOUND, "Empleado no registrado en base de datos."));

        if(empleadoCreacionTiquete.getTipoEmpleado().getCargo() != null
                && !empleadoCreacionTiquete.getTipoEmpleado().getCargo().isEmpty()
                && !empleadoCreacionTiquete.getTipoEmpleado().getCargo().equals("ADMINISTRATIVO")){

            throw new GeneralException(GeneralException.Tipo.UNAUTHORIZED,
                    Constantes.PERMISOS_INSUFICIENTES +
                            empleadoCreacionTiquete.getNumeroIdentificacion() + " no cuenta con permisos para la creación de nuevas estaciones.");
        }



        // Actualiza el estado del registro
        Estacion estacionRecibida = new Estacion();
        estacionRecibida.setNombre(estacion.getNombre());
        estacionRecibida.setDescripcion(estacion.getDescripcion());
        estacionRecibida.setEstadoDisponibilidad(estacion.getEstadoDisponibilidad());
        estacionRecibida.setPorcentajeOcupacion(estacion.getPorcentajeOcupacion());
        estacionRecibida.setDiasHabilitacion(estacion.getDiasHabilitacion());

        Estacion estacionGuardada = estacionService.update(id, estacionRecibida);

        // Mapea la entidad actualizada a un DTO
        EstacionResponseDto estacionResponseDto = EstacionResponseDto.builder()
                .nombre(estacionGuardada.getNombre())
                .descripcion(estacionGuardada.getDescripcion())
                .estadoDisponibilidad(estacionGuardada.getEstadoDisponibilidad())
                .porcentajeOcupacion(estacionGuardada.getPorcentajeOcupacion())
                .build();
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, estacionResponseDto, true));
    }

    /**
     * Elimina una estacion por su ID.
     *
     * @param id Identificador único de la estacion
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("estaciones/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarEstaciones(@PathVariable Long id) {
        estacionService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }
}
