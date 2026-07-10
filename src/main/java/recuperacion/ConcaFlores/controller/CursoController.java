package recuperacion.ConcaFlores.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.CursoDTO;
import recuperacion.ConcaFlores.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {
    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listar() { return ResponseEntity.ok(cursoService.listarTodos()); }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> buscarPorId(@PathVariable Integer id) { return ResponseEntity.ok(cursoService.buscarPorId(id)); }

    @GetMapping("/carrera/{idCarrera}")
    public ResponseEntity<List<CursoDTO>> listarPorCarrera(@PathVariable Integer idCarrera) { return ResponseEntity.ok(cursoService.listarPorCarrera(idCarrera)); }

    @PostMapping
    public ResponseEntity<CursoDTO> crear(@Valid @RequestBody CursoDTO dto) { return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crear(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody CursoDTO dto) { return ResponseEntity.ok(cursoService.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) { cursoService.eliminar(id); return ResponseEntity.noContent().build(); }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) { cursoService.restaurar(id); return ResponseEntity.noContent().build(); }
}
