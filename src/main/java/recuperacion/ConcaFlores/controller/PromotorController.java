package recuperacion.ConcaFlores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.PromotorDTO;
import recuperacion.ConcaFlores.service.PromotorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotores")
@RequiredArgsConstructor
@Tag(name = "Promotores", description = "CRUD de Promotores con borrado logico")
public class PromotorController {

    private final PromotorService promotorService;

    @GetMapping
    public ResponseEntity<List<PromotorDTO>> listar() {
        return ResponseEntity.ok(promotorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromotorDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(promotorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PromotorDTO> crear(@Valid @RequestBody PromotorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promotorService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotorDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody PromotorDTO dto) {
        return ResponseEntity.ok(promotorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Borrado logico - marca deleted_at")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        promotorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar promotor desactivado")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) {
        promotorService.restaurar(id);
        return ResponseEntity.noContent().build();
    }
}
