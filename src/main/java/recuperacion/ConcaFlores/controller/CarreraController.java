package recuperacion.ConcaFlores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.CarreraDTO;
import recuperacion.ConcaFlores.service.CarreraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
@RequiredArgsConstructor
@Tag(name = "Carreras", description = "CRUD de Carreras")
public class CarreraController {

    private final CarreraService carreraService;

    @GetMapping
    @Operation(summary = "Listar todas las carreras (activas e inactivas)")
    public ResponseEntity<List<CarreraDTO>> listar() {
        return ResponseEntity.ok(carreraService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar carrera por ID")
    public ResponseEntity<CarreraDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(carreraService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva carrera")
    public ResponseEntity<CarreraDTO> crear(@Valid @RequestBody CarreraDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carreraService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de una carrera")
    public ResponseEntity<CarreraDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CarreraDTO dto) {
        return ResponseEntity.ok(carreraService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar carrera (borrado logico)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        carreraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar carrera desactivada")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) {
        carreraService.restaurar(id);
        return ResponseEntity.noContent().build();
    }
}
