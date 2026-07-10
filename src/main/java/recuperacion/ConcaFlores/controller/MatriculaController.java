package recuperacion.ConcaFlores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.MatriculaRequestDTO;
import recuperacion.ConcaFlores.dto.MatriculaResponseDTO;
import recuperacion.ConcaFlores.service.MatriculaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
@Tag(name = "Matrículas", description = "Registro y consulta de matrículas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    @GetMapping
    @Operation(summary = "Listar todas las matrículas")
    public ResponseEntity<List<MatriculaResponseDTO>> listar() {
        return ResponseEntity.ok(matriculaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar matrícula por ID")
    public ResponseEntity<MatriculaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(matriculaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nueva matrícula (usa sp_RegistrarMatricula)")
    public ResponseEntity<MatriculaResponseDTO> registrar(@Valid @RequestBody MatriculaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculaService.registrar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar matrícula")
    public ResponseEntity<MatriculaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody MatriculaRequestDTO dto) {
        return ResponseEntity.ok(matriculaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar matrícula (soft delete)")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        matriculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restaurar")
    @Operation(summary = "Restaurar matrícula eliminada")
    public ResponseEntity<Void> restaurar(@PathVariable Integer id) {
        matriculaService.restaurar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/promotor/{idPromotor}")
    @Operation(summary = "Listar matrículas por promotor")
    public ResponseEntity<List<MatriculaResponseDTO>> porPromotor(@PathVariable Integer idPromotor) {
        return ResponseEntity.ok(matriculaService.listarPorPromotor(idPromotor));
    }

    @GetMapping("/estudiante/{idEstudiante}")
    @Operation(summary = "Listar matrículas por estudiante")
    public ResponseEntity<List<MatriculaResponseDTO>> porEstudiante(@PathVariable Integer idEstudiante) {
        return ResponseEntity.ok(matriculaService.listarPorEstudiante(idEstudiante));
    }
}
