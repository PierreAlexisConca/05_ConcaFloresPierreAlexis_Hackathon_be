package recuperacion.ConcaFlores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.service.ImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Tag(name = "Importación", description = "Importar datos desde CSV")
public class ImportController {

    private final ImportService importService;

    @PostMapping("/promotores")
    @Operation(summary = "Importar Promotores desde CSV")
    public ResponseEntity<Map<String, String>> importarPromotores(@RequestParam("file") MultipartFile file) throws Exception {
        String mensaje = importService.importarPromotoresCsv(file);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/estudiantes")
    @Operation(summary = "Importar Estudiantes desde CSV")
    public ResponseEntity<Map<String, String>> importarEstudiantes(@RequestParam("file") MultipartFile file) throws Exception {
        String mensaje = importService.importarEstudiantesCsv(file);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sedes")
    @Operation(summary = "Importar Sedes desde CSV")
    public ResponseEntity<Map<String, String>> importarSedes(@RequestParam("file") MultipartFile file) throws Exception {
        String mensaje = importService.importarSedesCsv(file);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/carreras")
    @Operation(summary = "Importar Carreras desde CSV")
    public ResponseEntity<Map<String, String>> importarCarreras(@RequestParam("file") MultipartFile file) throws Exception {
        String mensaje = importService.importarCarrerasCsv(file);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profesores")
    @Operation(summary = "Importar Profesores desde CSV")
    public ResponseEntity<Map<String, String>> importarProfesores(@RequestParam("file") MultipartFile file) throws Exception {
        String mensaje = importService.importarProfesoresCsv(file);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cursos")
    @Operation(summary = "Importar Cursos desde CSV")
    public ResponseEntity<Map<String, String>> importarCursos(@RequestParam("file") MultipartFile file) throws Exception {
        String mensaje = importService.importarCursosCsv(file);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        return ResponseEntity.ok(response);
    }
}
