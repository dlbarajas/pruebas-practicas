package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.Utils.UtilsGeneral;
import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.*;
import com.parque.diversiones.backend.entity.*;
import com.parque.diversiones.backend.exceptions.GeneralException;
import com.parque.diversiones.backend.repository.*;
import com.parque.diversiones.backend.service.TiqueteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con tiquete.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar tiquete.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class TiqueteController {
    private final TiqueteService tiqueteService;
    private final EstacionRepository estacionRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final TemporadaRepository temporadaRepository;
    private final MaquinaRepository maquinaRepository;

    /**
     * Constructor que inicializa el servicio de la tiquete.
     *
     * @param tiqueteService      instancia del servicio de tiquete
     * @param estacionRepository
     * @param clienteRepository
     * @param empleadoRepository
     * @param temporadaRepository
     * @param maquinaRepository
     */
    public TiqueteController(TiqueteService tiqueteService,
                             EstacionRepository estacionRepository,
                             ClienteRepository clienteRepository,
                             EmpleadoRepository empleadoRepository, TemporadaRepository temporadaRepository, MaquinaRepository maquinaRepository) {
        this.tiqueteService = tiqueteService;
        this.estacionRepository = estacionRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.temporadaRepository = temporadaRepository;
        this.maquinaRepository = maquinaRepository;
    }

    /**
     * Obtiene una lista de todos los tiquetes con estado activo.
     *
     * @return ResponseEntity con una lista de los tiquetes encapsuladas en un ApiResponseDto
     */
    @GetMapping("tiquetes")
    public ResponseEntity<ApiResponseDto<List<TiqueteIngresoDto>>> listarTodosLosTiquetes() {
        // Obtén la lista de los tiquetes con estado true
        List<Tiquete> tiquetes = tiqueteService.findByStateTrue();

        // Mapea la lista de entidades tiquete a TiqueteResponseDto
        List<TiqueteIngresoDto> tiqueteIngresoDto = tiquetes.stream()
                .map(tiquete -> TiqueteIngresoDto.builder()
                        .fechaAdquisicion(tiquete.getFechaAdquisicion())
                        .precio(tiquete.getPrecio())
                        .estadoTiquete(tiquete.getEstadoTiquete())
                        .build()
                )
                .toList();
        return ResponseEntity.ok(new ApiResponseDto<List<TiqueteIngresoDto>>(Constantes.DATOS_OBTENIDOS, tiqueteIngresoDto, true));
    }

    /**
     * Obtiene una tiquete específico por su ID.
     *
     * @param id Identificador único de la tiquete
     * @return ResponseEntity con el tiquete solicitado encapsulado en un ApiResponseDto
     */
    @GetMapping("tiquetes/{id}")
    public ResponseEntity<ApiResponseDto<TiqueteIngresoDto>> obtenerTiquetePorId(@PathVariable Long id) {
        // Busca la entidad por ID
        Tiquete tiquete = tiqueteService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Mapea la entidad a un DTO
        TiqueteIngresoDto tiqueteIngresoDto = TiqueteIngresoDto.builder()
                .fechaAdquisicion(tiquete.getFechaAdquisicion())
                .precio(tiquete.getPrecio())
                .build();
        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<TiqueteIngresoDto>(Constantes.REGISTRO_ENCONTRADO, tiqueteIngresoDto, true));
    }


    /**
     * Guarda un nuevo tiquete.
     *
     * @param tiquete Objeto Tiquete a guardar
     * @return ResponseEntity con el tiquete guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("tiquetes")
    public ResponseEntity<ApiResponseDto<TiqueteResponseDto>> guardarTiquete(@RequestBody TiqueteIngresoDto tiquete) {

        List<MaquinaRecibidaDto> maquinasDeLaEstacionDto = null;

        if(!UtilsGeneral.esFormatoValido(tiquete.getFechaAdquisicion())){
            throw new GeneralException(GeneralException.Tipo.VALOR_NO_VALIDO, Constantes.FECHA_CON_FORMATO_INVALIDO);
        }

        // Busca el tipo de empleado por su ID
        Empleado empleadoCreacionTiquete = empleadoRepository.findByNumeroIdentificacion(tiquete.getNumeroIdentificacionEmpleado())
                .orElseThrow(() -> new GeneralException(GeneralException.Tipo.NOT_FOUND, "Empleado no registrado en base de datos."));

        if(empleadoCreacionTiquete.getTipoEmpleado().getCargo() != null
                && !empleadoCreacionTiquete.getTipoEmpleado().getCargo().isEmpty()
                && !empleadoCreacionTiquete.getTipoEmpleado().getCargo().equals("LOGÍSTICA")){

            throw new GeneralException(GeneralException.Tipo.UNAUTHORIZED,
                    Constantes.PERMISOS_INSUFICIENTES +
                            empleadoCreacionTiquete.getNumeroIdentificacion() + " no cuenta con permisos para la creación de nuevos tiquetes.");
        }


        // Busca el tipo de cliente por su ID
        Cliente clienteCompraTiquete = clienteRepository.findByNumeroIdentificacion(tiquete.getNumeroIdentificacionCliente())
                .orElseThrow(() -> new GeneralException(GeneralException.Tipo.NOT_FOUND, "Cliente no registrado en base de datos."));

        // Busca el tipo de estacion por su ID
        Estacion tipoEstacionCreacionTiquete = estacionRepository.findById(tiquete.getIdEstacion())
                .orElseThrow(() -> new GeneralException(GeneralException.Tipo.NOT_FOUND, "Estacion enviada no encontrada en base de datos."));


        if(!tipoEstacionCreacionTiquete.getEstadoDisponibilidad().equals(true)){
            throw new GeneralException(GeneralException.Tipo.VALOR_NO_VALIDO,
                    Constantes.ESTACION_INHABILITADA +
                            tipoEstacionCreacionTiquete.getNombre() + " se encuentra inhabilitada.");
        }

        // Busca el tipo de estacion por su ID
        Temporada temporadaCreacionTiquete = temporadaRepository.findById(tiquete.getIdTemporada())
                .orElseThrow(() -> new GeneralException(GeneralException.Tipo.NOT_FOUND, "Temporada enviada no encontrada en base de datos."));

        String fechaInicioTemporada = temporadaCreacionTiquete.getFechaInicio();
        String fechaFinTemporada = temporadaCreacionTiquete.getFechaFin();

        if(!temporadaCreacionTiquete.getEstadoDisponibilidad().equals(true)){
            throw new GeneralException(GeneralException.Tipo.VALOR_NO_VALIDO,
                    Constantes.ESTACION_INHABILITADA_FECHA_ESTADO +
                            temporadaCreacionTiquete.getNombre());
        }

        // Busca el tipo de estacion por su ID
       List<Maquina> maquinasEstacionesTiquete = maquinaRepository.findByEstacionId(tiquete.getIdEstacion());

        if(maquinasEstacionesTiquete.isEmpty() || maquinasEstacionesTiquete == null){
           throw  new GeneralException(GeneralException.Tipo.NOT_FOUND, "No se encontraron maquinas registradas para esa estación.");
        }


         maquinasDeLaEstacionDto = maquinasEstacionesTiquete.stream()
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
                            .build();
                })
                .toList();

        EmpleadoResponseDto empleadoResponseDto = EmpleadoResponseDto.builder()
                .nombre(empleadoCreacionTiquete.getNombre())
                .apellido(empleadoCreacionTiquete.getApellido())
                .email(empleadoCreacionTiquete.getEmail())
                .edad(empleadoCreacionTiquete.getEdad())
                .numeroIdentificacion(empleadoCreacionTiquete.getNumeroIdentificacion())
                .telefono(empleadoCreacionTiquete.getTelefono())
                .horarioLaboral(empleadoCreacionTiquete.getHorarioLaboral())
                .tipoEmpleadoId(empleadoCreacionTiquete.getTipoEmpleado().getId())
                .build();


        // Mapea los familiares al DTO
        List<FamiliarResponseDto> familiaresDto = clienteCompraTiquete.getFamiliares().stream()
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
                .nombre(clienteCompraTiquete.getNombre())
                .apellido(clienteCompraTiquete.getApellido())
                .email(clienteCompraTiquete.getEmail())
                .edad(clienteCompraTiquete.getEdad())
                .estatura(clienteCompraTiquete.getEstatura())
                .numeroIdentificacion(clienteCompraTiquete.getNumeroIdentificacion())
                .telefono(clienteCompraTiquete.getTelefono())
                .familiares(familiaresDto)
                .build();


        EstacionResponseAddMaquinasDto estacionMaquinasResponseDto = EstacionResponseAddMaquinasDto.builder()
                .nombre(tipoEstacionCreacionTiquete.getNombre())
                .descripcion(tipoEstacionCreacionTiquete.getDescripcion())
                .estadoDisponibilidad(tipoEstacionCreacionTiquete.getEstadoDisponibilidad())
                .porcentajeOcupacion(tipoEstacionCreacionTiquete.getPorcentajeOcupacion())
                .diasHabilitacion(
                        tipoEstacionCreacionTiquete.getDiasHabilitacion().stream()
                                .map(DayOfWeek::name)
                                .collect(Collectors.toList())
                )
                .maquinasDeLaEstacion(maquinasDeLaEstacionDto)
                .build();

        // Mapea la entidad a un DTO
        TemporadaResponseDto temporadaRecibidaDto = TemporadaResponseDto.builder()
                .nombre(temporadaCreacionTiquete.getNombre())
                .fechaInicio(temporadaCreacionTiquete.getFechaInicio())
                .fechaFin(temporadaCreacionTiquete.getFechaFin())
                .descripcion(temporadaCreacionTiquete.getDescripcion())
                .estadoDisponibilidad(temporadaCreacionTiquete.getEstado())
                .build();

        Tiquete tiqueteRespuestaDto = Tiquete.builder()
                .fechaAdquisicion(tiquete.getFechaAdquisicion())
                .precio(tiquete.getPrecio())
                .estadoTiquete(true)
                .estacion(tipoEstacionCreacionTiquete)
                .cliente(clienteCompraTiquete)
                .empleado(empleadoCreacionTiquete)
                .build();

        tiquete.setEstadoTiquete(true);
        Tiquete tiqueteAGuardar = tiqueteService.save(tiqueteRespuestaDto);

        // Mapea la entidad a un DTO
        TiqueteResponseDto tiqueteResponseDto = TiqueteResponseDto.builder()
                .fechaAdquisicion(tiqueteAGuardar.getFechaAdquisicion())
                .precio(tiqueteAGuardar.getPrecio())
                .estadoTiquete(tiqueteAGuardar.getEstadoTiquete())
                .estacion(estacionMaquinasResponseDto)
                .informacionClente(clienteResponseDto)
                .informacionEmpleado(empleadoResponseDto)
                .informacionTemporada(temporadaRecibidaDto)
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, tiqueteResponseDto, true));
    }

    /**
     * Actualiza un tiquete existente por su ID.
     *
     * @param id      Identificador único de la tiquete
     * @param tiquete Objeto Tiquete con los datos actualizados
     * @return ResponseEntity con el tiquete actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("tiquetes/{id}")
    public ResponseEntity<ApiResponseDto<TiqueteIngresoDto>> actualizarTiquete(@PathVariable Long id, @RequestBody Tiquete tiquete) {
        // Actualiza el estado del registro
        tiquete.setEstadoTiquete(true);
        Tiquete tiqueteActualizada = tiqueteService.update(id, tiquete);

        // Mapea la entidad actualizada a un DTO
        TiqueteIngresoDto tiqueteIngresoDto = TiqueteIngresoDto.builder()
                .fechaAdquisicion(tiqueteActualizada.getFechaAdquisicion())
                .precio(tiqueteActualizada.getPrecio())
                .build();

        // Retorna la respuesta con el DTO
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, tiqueteIngresoDto, true));
    }

    /**
     * Elimina un tiquete por su ID.
     *
     * @param id Identificador único del tiquete
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("tiquetes/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarTiquete(@PathVariable Long id) {
        tiqueteService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }
}
