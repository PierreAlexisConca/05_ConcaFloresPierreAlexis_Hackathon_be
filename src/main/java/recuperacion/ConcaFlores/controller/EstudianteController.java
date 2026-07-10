package recuperacion.ConcaFlores.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.EstudianteDTO;
import recuperacion.ConcaFlores.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {
    private final EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> listar() { return ResponseEntity.ok(estudianteService.listarTodos()); }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteDTO> buscarPorId(@PathVariable Integer id) { return ResponseEntity.ok(estudianteService.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<EstudianteDTO> crear(@Valid @RequestBody EstudianteDTO dto) { return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.crear(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody EstudianteDTO dto) { return ResponseEntity.ok(estudianteService.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) { estudianteService.eliminar(id); return ResponseEntity.noContent().build(); }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) { estudianteService.restaurar(id); return ResponseEntity.noContent().build(); }
}
