package recuperacion.ConcaFlores.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Tag(name = "Exportación", description = "Exportar datos a Excel y PDF")
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/excel")
    @Operation(summary = "Exportar todos los datos a Excel")
    public ResponseEntity<byte[]> exportarExcel() throws Exception {
        byte[] excelFile = exportService.exportarTodoExcel();
        String filename = "EduFuturo_" + LocalDate.now() + ".xlsx";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }

    @GetMapping("/pdf")
    @Operation(summary = "Exportar todos los datos a PDF")
    public ResponseEntity<byte[]> exportarPdf() throws Exception {
        byte[] pdfFile = exportService.exportarTodoPdf();
        String filename = "EduFuturo_" + LocalDate.now() + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfFile);
    }
}
