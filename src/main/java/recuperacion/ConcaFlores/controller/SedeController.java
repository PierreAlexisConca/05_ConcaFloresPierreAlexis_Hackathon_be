package recuperacion.ConcaFlores.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.SedeDTO;
import recuperacion.ConcaFlores.service.SedeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
public class SedeController {
    private final SedeService sedeService;

    @GetMapping
    public ResponseEntity<List<SedeDTO>> listar() { return ResponseEntity.ok(sedeService.listarTodos()); }

    @GetMapping("/{id}")
    public ResponseEntity<SedeDTO> buscarPorId(@PathVariable Integer id) { return ResponseEntity.ok(sedeService.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<SedeDTO> crear(@Valid @RequestBody SedeDTO dto) { return ResponseEntity.status(HttpStatus.CREATED).body(sedeService.crear(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<SedeDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody SedeDTO dto) { return ResponseEntity.ok(sedeService.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) { sedeService.eliminar(id); return ResponseEntity.noContent().build(); }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) { sedeService.restaurar(id); return ResponseEntity.noContent().build(); }
}
