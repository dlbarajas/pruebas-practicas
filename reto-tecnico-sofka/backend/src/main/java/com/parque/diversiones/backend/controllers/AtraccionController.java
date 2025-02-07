package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.ApiResponseDto;
import com.parque.diversiones.backend.dto.AtraccionResponseDto;
import com.parque.diversiones.backend.entity.Atraccion;
import com.parque.diversiones.backend.service.AtraccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con atracciones.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar atracciones.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class AtraccionController {

    private final AtraccionService atraccionService;

    /**
     * Constructor que inicializa el servicio de atracciones.
     *
     * @param atraccionService instancia del servicio de atracciones
     */
    public AtraccionController(AtraccionService atraccionService) {
        this.atraccionService = atraccionService;
    }

    /**
     * Obtiene una lista de todos las atracciones con estado activo.
     *
     * @return ResponseEntity con una lista de atracciones encapsulada en un ApiResponseDto
     */
    @GetMapping("atracciones")
    public ResponseEntity<ApiResponseDto<List<AtraccionResponseDto>>> listarTodasLasAtracciones() {
        // Obtén la lista de atracciones con estado true
        List<Atraccion> atracciones = atraccionService.findByStateTrue();

        // Mapea la lista de entidades Atraccion a AtraccionResponseDto
        List<AtraccionResponseDto> atraccionResponseDto = atracciones.stream()
                .map(atraccion -> AtraccionResponseDto.builder()
                        .nombre(atraccion.getNombre())
                        .descripcion(atraccion.getDescripcion())
                        .clasificacion(atraccion.getClasificacion())
                        .condicionesUso(atraccion.getCondicionesUso())
                        .estadoAtraccion(atraccion.getEstadoAtraccion())
                        .build()
                )
                .toList();
        return ResponseEntity.ok(new ApiResponseDto<List<AtraccionResponseDto>>(Constantes.DATOS_OBTENIDOS, atraccionResponseDto, true));
    }

    /**
     * Obtiene una atraccion específico por su ID.
     *
     * @param id Identificador único de la atraccion
     * @return ResponseEntity con la araccion solicitado encapsulado en un ApiResponseDto
     */
    @GetMapping("atracciones/{id}")
    public ResponseEntity<ApiResponseDto<AtraccionResponseDto>> obtenerAtraccionPorId(@PathVariable Long id) {
        // Busca la entidad por ID
        Atraccion atraccion = atraccionService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Mapea la entidad a un DTO
        AtraccionResponseDto atraccionResponseDto = AtraccionResponseDto.builder()
                .nombre(atraccion.getNombre())
                .descripcion(atraccion.getDescripcion())
                .clasificacion(atraccion.getClasificacion())
                .condicionesUso(atraccion.getCondicionesUso())
                .estadoAtraccion(atraccion.getEstadoAtraccion())
                .build();
        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<AtraccionResponseDto>(Constantes.REGISTRO_ENCONTRADO, atraccionResponseDto, true));
    }

    /**
     * Guarda una nueva atraccion.
     *
     * @param atraccion Objeto Atraccion a guardar
     * @return ResponseEntity con el atraccion guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("atracciones")
    public ResponseEntity<ApiResponseDto<AtraccionResponseDto>> guardarAtraccion(@RequestBody Atraccion atraccion) {
        atraccion.setCreatedAt(LocalDateTime.now());
        atraccion.setCreatedBy((long) 1);
        atraccion.setEstado(true);
        Atraccion atraccionAGuardar = atraccionService.save(atraccion);

        // Mapea la entidad a un DTO
        AtraccionResponseDto atraccionResponseDto = AtraccionResponseDto.builder()
                .nombre(atraccionAGuardar.getNombre())
                .descripcion(atraccionAGuardar.getDescripcion())
                .clasificacion(atraccionAGuardar.getClasificacion())
                .condicionesUso(atraccionAGuardar.getCondicionesUso())
                .estadoAtraccion(atraccionAGuardar.getEstadoAtraccion())
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, atraccionResponseDto, true));
    }

    /**
     * Actualiza una atraccion existente por su ID.
     *
     * @param id      Identificador único de la atraccion
     * @param atraccion Objeto Atraccion con los datos actualizados
     * @return ResponseEntity con el atraccion actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("atracciones/{id}")
    public ResponseEntity<ApiResponseDto<AtraccionResponseDto>> actualizarAtraccion(@PathVariable Long id, @RequestBody Atraccion atraccion) {
        // Actualiza el estado del registro
        atraccion.setEstado(true);
        Atraccion atraccionActualizado = atraccionService.update(id, atraccion);

        // Mapea la entidad actualizada a un DTO
        AtraccionResponseDto atraccionResponseDto = AtraccionResponseDto.builder()
                .nombre(atraccionActualizado.getNombre())
                .descripcion(atraccionActualizado.getDescripcion())
                .clasificacion(atraccionActualizado.getClasificacion())
                .condicionesUso(atraccionActualizado.getCondicionesUso())
                .estadoAtraccion(atraccionActualizado.getEstadoAtraccion())
                .build();

        // Retorna la respuesta con el DTO
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, atraccionResponseDto, true));
    }

    /**
     * Elimina una atraccion por su ID.
     *
     * @param id Identificador único de la atraccion
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("atracciones/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarAtraccion(@PathVariable Long id) {
        atraccionService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }
}
