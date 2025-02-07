package com.parque.diversiones.backend.controllers;

import com.parque.diversiones.backend.constants.Constantes;
import com.parque.diversiones.backend.dto.ApiResponseDto;
import com.parque.diversiones.backend.dto.TipoEmpleadoResponseDto;
import com.parque.diversiones.backend.entity.TipoEmpleado;
import com.parque.diversiones.backend.service.TipoEmpleadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con tipoEmpleado.
 * Proporciona endpoints para listar, obtener, guardar, actualizar y eliminar tipoEmpleado.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/")
public class TipoEmpleadoController {
    private final TipoEmpleadoService tipoEmpleadoService;

    /**
     * Constructor que inicializa el servicio de tipoEmpleado.
     *
     * @param tipoEmpleadoService instancia del servicio de tipoEmpleado
     */
    public TipoEmpleadoController(TipoEmpleadoService tipoEmpleadoService) {
        this.tipoEmpleadoService = tipoEmpleadoService;
    }

    /**
     * Obtiene una lista de todos los tipoEmpleados con estado activo.
     *
     * @return ResponseEntity con una lista de tipoEmpleado encapsulada en un ApiResponseDto
     */
    @GetMapping("tipoEmpleados")
    public ResponseEntity<ApiResponseDto<List<TipoEmpleadoResponseDto>>> listarTodosLosTiposEmpleados() {
        // Obtén la lista de tipoEmpleados con estado true
        List<TipoEmpleado> tipoEmpleados = tipoEmpleadoService.findByStateTrue();

        // Mapea la lista de entidades TipoEmpleado a TipoEmpleadoResponseDto
        List<TipoEmpleadoResponseDto> tipoEmpleadoResponseDto = tipoEmpleados.stream()
                .map(tipoEmpleado -> TipoEmpleadoResponseDto.builder()
                        .cargo(tipoEmpleado.getCargo())
                        .descripcion(tipoEmpleado.getDescripcion())
                        .build()
                )
                .toList();
        return ResponseEntity.ok(new ApiResponseDto<List<TipoEmpleadoResponseDto>>(Constantes.DATOS_OBTENIDOS, tipoEmpleadoResponseDto, true));
    }

    /**
     * Obtiene un tipoEmpleado específico por su ID.
     *
     * @param id Identificador único del tipoEmpleado
     * @return ResponseEntity con el tipoEmpleado solicitado encapsulado en un ApiResponseDto
     */
    @GetMapping("tipoEmpleados/{id}")
    public ResponseEntity<ApiResponseDto<TipoEmpleadoResponseDto>> obtenerTipoEmpleadoPorId(@PathVariable Long id) {
        // Busca la entidad por ID
        TipoEmpleado tipoEmpleado = tipoEmpleadoService.findById(id)
                .orElseThrow(() -> new RuntimeException(Constantes.REGISTRO_NO_ENCONTRADO));

        // Mapea la entidad a un DTO
        TipoEmpleadoResponseDto tipoEmpleadoResponseDto = TipoEmpleadoResponseDto.builder()
                .cargo(tipoEmpleado.getCargo())
                .descripcion(tipoEmpleado.getDescripcion())
                .build();

        // Responde con un DTO envuelto en ApiResponseDto
        return ResponseEntity.ok(new ApiResponseDto<TipoEmpleadoResponseDto>(Constantes.REGISTRO_ENCONTRADO, tipoEmpleadoResponseDto, true));
    }

    /**
     * Guarda un nuevo tipoEmpleado.
     *
     * @param tipoEmpleado Objeto TipoEmpleado a guardar
     * @return ResponseEntity con el tipoEmpleado guardado encapsulado en un ApiResponseDto
     */

    @PostMapping("tipoEmpleados")
    public ResponseEntity<ApiResponseDto<TipoEmpleadoResponseDto>> guardarTipoEmpleado(@RequestBody TipoEmpleado tipoEmpleado) {
        tipoEmpleado.setCreatedAt(LocalDateTime.now());
        tipoEmpleado.setCreatedBy((long) 1);
        tipoEmpleado.setEstado(true);
        TipoEmpleado tipoEmpleadoAGuardar = tipoEmpleadoService.save(tipoEmpleado);

        // Mapea la entidad a un DTO
        TipoEmpleadoResponseDto tipoEmpleadoResponseDto = TipoEmpleadoResponseDto.builder()
                .cargo(tipoEmpleadoAGuardar.getCargo())
                .descripcion(tipoEmpleadoAGuardar.getDescripcion())
                .build();

        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_GUARDADOS, tipoEmpleadoResponseDto, true));
    }

    /**
     * Actualiza un tipoEmpleado existente por su ID.
     *
     * @param id      Identificador único del tipoEmpleado
     * @param tipoEmpleado Objeto TipoEmpleado con los datos actualizados
     * @return ResponseEntity con el tipoEmpleado actualizado encapsulado en un ApiResponseDto
     */
    @PutMapping("tipoEmpleados/{id}")
    public ResponseEntity<ApiResponseDto<TipoEmpleadoResponseDto>> actualizarTipoEmpleados(@PathVariable Long id, @RequestBody TipoEmpleado tipoEmpleado) {
        // Actualiza el estado del registro
        tipoEmpleado.setEstado(true);
        TipoEmpleado tipoEmpleadoActualizado = tipoEmpleadoService.update(id, tipoEmpleado);

        // Mapea la entidad actualizada a un DTO
        TipoEmpleadoResponseDto tipoEmpleadoResponseDto = TipoEmpleadoResponseDto.builder()
                .cargo(tipoEmpleadoActualizado.getCargo())
                .descripcion(tipoEmpleadoActualizado.getDescripcion())
                .build();

        // Retorna la respuesta con el DTO
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.DATOS_ACTUALIZADO, tipoEmpleadoResponseDto, true));
    }

    /**
     * Elimina un tipoEmpleado por su ID.
     *
     * @param id Identificador único del tipoEmpleado
     * @return ResponseEntity con un mensaje de éxito encapsulado en un ApiResponseDto
     */
    @DeleteMapping("tipoEmpleados/{id}")
    public ResponseEntity<ApiResponseDto<String>> eliminarTipoEmpleado(@PathVariable Long id) {
        tipoEmpleadoService.delete(id);
        return ResponseEntity.ok(new ApiResponseDto<>(Constantes.REGISTRO_ELIMINADO, null, true));
    }
}
