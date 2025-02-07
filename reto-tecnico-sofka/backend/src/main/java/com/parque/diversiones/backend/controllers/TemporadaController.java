package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.ApiResponseDto;
import com.parque.diversiones.backend.dto.TemporadaResponseDto;
import com.parque.diversiones.backend.entity.Temporada;
import com.parque.diversiones.backend.service.TemporadaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con temporada.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar temporada.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class TemporadaController {
    private final TemporadaService temporadaService;

    /**
     * Constructor que inicializa el servicio de la temporada.
     *
     * @param temporadaService instancia del servicio de la temporada
     */
    public TemporadaController(TemporadaService temporadaService) {

        this.temporadaService = temporadaService;
    }

    /**
     * Obtiene una lista de todos las temporadas con estado activo.
     *
     * @return ResponseEntity con una lista de temporadas encapsulada en un ApiResponseDto
     */
    @GetMapping("temporadas")
    public ResponseEntity<ApiResponseDto<List<TemporadaResponseDto>>> listarTodosLasTemporadas() {
        // Obtén la lista de temporadas con estado true
        List<Temporada> temporadas = temporadaService.findByStateTrue();

        // Mapea la lista de entidades temporada a TemporadaResponseDto
        List<TemporadaResponseDto> temporadaResponseDto = temporadas.stream()
                .map(temporada -> TemporadaResponseDto.builder()
                        .nombre(temporada.getNombre())
                        .fechaInicio(temporada.getFechaInicio())
                        .fechaFin(temporada.getFechaFin())
                        .descripcion(temporada.getDescripcion())
                        .estadoDisponibilidad(temporada.getEstado())
                        .build()
                )
                .toList();
        return ResponseEntity.ok(new ApiResponseDto<List<TemporadaResponseDto>>(Constantes.DATOS_OBTENIDOS, temporadaResponseDto, true));
    }

    /**
     * Obtiene una temporada específica por su ID.
     *
     * @param id Identificador único de la temporada
     * @return ResponseEntity con la temporada solicitada encapsulada en un ApiResponseDto
     */
    @GetMapping("temporadas/{id}")
    public ResponseEntity<ApiResponseDto<TemporadaResponseDto>> obtenerTemporadaPorId(@PathVariable Long id) {
        // Busca la entidad por ID
        Temporada temporada = temporadaService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Mapea la entidad a un DTO
        TemporadaResponseDto temporadaResponseDto = TemporadaResponseDto.builder()
                .nombre(temporada.getNombre())
                .fechaInicio(temporada.getFechaInicio())
                .fechaFin(temporada.getFechaFin())
                .descripcion(temporada.getDescripcion())
                .estadoDisponibilidad(temporada.getEstado())
                .build();
        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<TemporadaResponseDto>(Constantes.REGISTRO_ENCONTRADO, temporadaResponseDto, true));
    }

    /**
     * Guarda un nueva temporada.
     *
     * @param temporada Objeto Temporada a guardar
     * @return ResponseEntity con el temporada guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("temporadas")
    public ResponseEntity<ApiResponseDto<TemporadaResponseDto>> guardarTemporada(@RequestBody Temporada temporada) {
        temporada.setCreatedAt(LocalDateTime.now());
        temporada.setCreatedBy((long) 1);
        temporada.setEstado(true);
        Temporada temporadaAGuardar = temporadaService.save(temporada);

        // Mapea la entidad a un DTO
        TemporadaResponseDto temporadaResponseDto = TemporadaResponseDto.builder()
                .nombre(temporadaAGuardar.getNombre())
                .fechaInicio(temporadaAGuardar.getFechaInicio())
                .fechaFin(temporadaAGuardar.getFechaFin())
                .descripcion(temporadaAGuardar.getDescripcion())
                .estadoDisponibilidad(temporadaAGuardar.getEstado())
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, temporadaResponseDto, true));
    }

    /**
     * Actualiza una temporada existente por su ID.
     *
     * @param id      Identificador único de la temporada
     * @param temporada Objeto Temporada con los datos actualizados
     * @return ResponseEntity con el temporada actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("temporadas/{id}")
    public ResponseEntity<ApiResponseDto<TemporadaResponseDto>> actualizarTemporada(@PathVariable Long id, @RequestBody Temporada temporada) {
        // Actualiza el estado del registro
        temporada.setEstado(true);
        Temporada temporadaActualizada = temporadaService.update(id, temporada);

        // Mapea la entidad actualizada a un DTO
        TemporadaResponseDto temporadaResponseDto = TemporadaResponseDto.builder()
                .nombre(temporadaActualizada.getNombre())
                .fechaInicio(temporadaActualizada.getFechaInicio())
                .fechaFin(temporadaActualizada.getFechaFin())
                .descripcion(temporadaActualizada.getDescripcion())
                .estadoDisponibilidad(temporadaActualizada.getEstado())
                .build();

        // Retorna la respuesta con el DTO
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, temporadaResponseDto, true));
    }

    /**
     * Elimina una temporada por su ID.
     *
     * @param id Identificador único de la temporada
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("temporadas/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarTemporadaes(@PathVariable Long id) {
        temporadaService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }
}
