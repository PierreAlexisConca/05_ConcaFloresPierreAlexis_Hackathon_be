package recuperacion.ConcaFlores.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.ProfesorDTO;
import recuperacion.ConcaFlores.service.ProfesorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profesores")
@RequiredArgsConstructor
public class ProfesorController {
    private final ProfesorService profesorService;

    @GetMapping
    public ResponseEntity<List<ProfesorDTO>> listar() { return ResponseEntity.ok(profesorService.listarTodos()); }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDTO> buscarPorId(@PathVariable Integer id) { return ResponseEntity.ok(profesorService.buscarPorId(id)); }

    @PostMapping
    public ResponseEntity<ProfesorDTO> crear(@Valid @RequestBody ProfesorDTO dto) { return ResponseEntity.status(HttpStatus.CREATED).body(profesorService.crear(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ProfesorDTO dto) { return ResponseEntity.ok(profesorService.actualizar(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) { profesorService.eliminar(id); return ResponseEntity.noContent().build(); }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) { profesorService.restaurar(id); return ResponseEntity.noContent().build(); }
}
