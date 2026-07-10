package recuperacion.ConcaFlores.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import recuperacion.ConcaFlores.dto.*;
import recuperacion.ConcaFlores.repository.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExportService {

    private final PromotorService promotorService;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final SedeService sedeService;
    private final ProfesorService profesorService;
    private final CursoService cursoService;
    private final MatriculaService matriculaService;

    public ExportService(PromotorService promotorService, EstudianteService estudianteService,
                         CarreraService carreraService, SedeService sedeService,
                         ProfesorService profesorService, CursoService cursoService,
                         MatriculaService matriculaService) {
        this.promotorService = promotorService;
        this.estudianteService = estudianteService;
        this.carreraService = carreraService;
        this.sedeService = sedeService;
        this.profesorService = profesorService;
        this.cursoService = cursoService;
        this.matriculaService = matriculaService;
    }

    public byte[] exportarTodoExcel() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Sheet Promotores
        exportarPromotoresSheet(workbook, headerStyle);

        // Sheet Estudiantes
        exportarEstudiantesSheet(workbook, headerStyle);

        // Sheet Sedes
        exportarSedesSheet(workbook, headerStyle);

        // Sheet Carreras
        exportarCarrerasSheet(workbook, headerStyle);

        // Sheet Profesores
        exportarProfesoresSheet(workbook, headerStyle);

        // Sheet Cursos
        exportarCursosSheet(workbook, headerStyle);

        // Sheet Matrículas
        exportarMatriculasSheet(workbook, headerStyle);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        workbook.write(output);
        workbook.close();
        return output.toByteArray();
    }

    public byte[] exportarTodoPdf() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        doc.add(new Paragraph("SISTEMA ACADÉMICO - REPORTE GENERAL").setBold().setFontSize(16));
        doc.add(new Paragraph("Generado: " + LocalDateTime.now()).setFontSize(10));
        doc.add(new Paragraph("\n"));

        // Tabla Promotores
        doc.add(new Paragraph("PROMOTORES").setBold().setFontSize(12));
        doc.add(exportarPromotoresPdf());
        doc.add(new Paragraph("\n"));

        // Tabla Estudiantes
        doc.add(new Paragraph("ESTUDIANTES").setBold().setFontSize(12));
        doc.add(exportarEstudiantesPdf());
        doc.add(new Paragraph("\n"));

        // Tabla Sedes
        doc.add(new Paragraph("SEDES").setBold().setFontSize(12));
        doc.add(exportarSedesPdf());
        doc.add(new Paragraph("\n"));

        // Tabla Carreras
        doc.add(new Paragraph("CARRERAS").setBold().setFontSize(12));
        doc.add(exportarCarrerasPdf());
        doc.add(new Paragraph("\n"));

        // Tabla Profesores
        doc.add(new Paragraph("PROFESORES").setBold().setFontSize(12));
        doc.add(exportarProfesoresPdf());
        doc.add(new Paragraph("\n"));

        // Tabla Cursos
        doc.add(new Paragraph("CURSOS").setBold().setFontSize(12));
        doc.add(exportarCursosPdf());
        doc.add(new Paragraph("\n"));

        doc.close();
        return output.toByteArray();
    }

    // ===== EXCEL EXPORTS =====

    private void exportarPromotoresSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Promotores");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "DNI", "Nombres", "Apellidos", "Teléfono", "Correo", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<PromotorDTO> promotores = promotorService.listarTodos();
        int rowNum = 1;
        for (PromotorDTO p : promotores) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getIdPromotor());
            row.createCell(1).setCellValue(p.getCodigoPromotor());
            row.createCell(2).setCellValue(p.getDni());
            row.createCell(3).setCellValue(p.getNombres());
            row.createCell(4).setCellValue(p.getApellidos());
            row.createCell(5).setCellValue(p.getTelefono());
            row.createCell(6).setCellValue(p.getCorreo());
            row.createCell(7).setCellValue(p.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private void exportarEstudiantesSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Estudiantes");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "DNI", "Nombres", "Apellidos", "Correo", "Teléfono", "Dirección", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<EstudianteDTO> estudiantes = estudianteService.listarTodos();
        int rowNum = 1;
        for (EstudianteDTO e : estudiantes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getIdEstudiante());
            row.createCell(1).setCellValue(e.getDni());
            row.createCell(2).setCellValue(e.getNombres());
            row.createCell(3).setCellValue(e.getApellidos());
            row.createCell(4).setCellValue(e.getCorreo());
            row.createCell(5).setCellValue(e.getTelefono());
            row.createCell(6).setCellValue(e.getDireccion() != null ? e.getDireccion() : "");
            row.createCell(7).setCellValue(e.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private void exportarSedesSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Sedes");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "Nombre", "Dirección", "Ciudad", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<SedeDTO> sedes = sedeService.listarTodos();
        int rowNum = 1;
        for (SedeDTO s : sedes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(s.getIdSede());
            row.createCell(1).setCellValue(s.getCodigoSede());
            row.createCell(2).setCellValue(s.getNombre());
            row.createCell(3).setCellValue(s.getDireccion());
            row.createCell(4).setCellValue(s.getCiudad());
            row.createCell(5).setCellValue(s.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private void exportarCarrerasSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Carreras");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "Nombre", "Ciclos", "Inversión", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<CarreraDTO> carreras = carreraService.listarTodos();
        int rowNum = 1;
        for (CarreraDTO c : carreras) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(c.getIdCarrera());
            row.createCell(1).setCellValue(c.getCodigoCarrera());
            row.createCell(2).setCellValue(c.getNombre());
            row.createCell(3).setCellValue(c.getDuracionCiclos());
            row.createCell(4).setCellValue(c.getInversion().doubleValue());
            row.createCell(5).setCellValue(c.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private void exportarProfesoresSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Profesores");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "DNI", "Nombres", "Apellidos", "Especialidad", "Teléfono", "Correo", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<ProfesorDTO> profesores = profesorService.listarTodos();
        int rowNum = 1;
        for (ProfesorDTO p : profesores) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getIdProfesor());
            row.createCell(1).setCellValue(p.getCodigoProfesor());
            row.createCell(2).setCellValue(p.getDni());
            row.createCell(3).setCellValue(p.getNombres());
            row.createCell(4).setCellValue(p.getApellidos());
            row.createCell(5).setCellValue(p.getEspecialidad());
            row.createCell(6).setCellValue(p.getTelefono());
            row.createCell(7).setCellValue(p.getCorreo());
            row.createCell(8).setCellValue(p.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private void exportarCursosSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Cursos");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "Nombre", "Créditos", "Carrera", "Profesor", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<CursoDTO> cursos = cursoService.listarTodos();
        int rowNum = 1;
        for (CursoDTO c : cursos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(c.getIdCurso());
            row.createCell(1).setCellValue(c.getCodigoCurso());
            row.createCell(2).setCellValue(c.getNombre());
            row.createCell(3).setCellValue(c.getCreditos());
            row.createCell(4).setCellValue(c.getNombreCarrera() != null ? c.getNombreCarrera() : "");
            row.createCell(5).setCellValue(c.getNombreProfesor() != null ? c.getNombreProfesor() : "");
            row.createCell(6).setCellValue(c.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private void exportarMatriculasSheet(XSSFWorkbook workbook, CellStyle headerStyle) {
        Sheet sheet = workbook.createSheet("Matrículas");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Código", "Fecha", "Periodo", "Monto", "Estudiante", "Carrera", "Estado"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        List<MatriculaResponseDTO> matriculas = matriculaService.listar();
        int rowNum = 1;
        for (MatriculaResponseDTO m : matriculas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(m.getIdMatricula());
            row.createCell(1).setCellValue(m.getCodigoMatricula());
            row.createCell(2).setCellValue(m.getFechaMatricula().toString());
            row.createCell(3).setCellValue(m.getPeriodo());
            row.createCell(4).setCellValue(m.getMontoTotal().doubleValue());
            row.createCell(5).setCellValue(m.getNombreEstudiante());
            row.createCell(6).setCellValue(m.getNombreCarrera());
            row.createCell(7).setCellValue(m.getEstado() ? "Activo" : "Inactivo");
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    // ===== PDF EXPORTS =====

    private Table exportarPromotoresPdf() {
        Table table = new Table(new float[]{1, 1.5f, 1, 2, 2, 1.5f, 1});
        table.addCell("ID").addCell("Código").addCell("DNI").addCell("Nombres")
                .addCell("Apellidos").addCell("Teléfono").addCell("Estado");
        
        List<PromotorDTO> promotores = promotorService.listarTodos();
        for (PromotorDTO p : promotores) {
            table.addCell(p.getIdPromotor().toString());
            table.addCell(p.getCodigoPromotor());
            table.addCell(p.getDni());
            table.addCell(p.getNombres());
            table.addCell(p.getApellidos());
            table.addCell(p.getTelefono());
            table.addCell(p.getEstado() ? "Activo" : "Inactivo");
        }
        return table;
    }

    private Table exportarEstudiantesPdf() {
        Table table = new Table(new float[]{1, 1, 2, 2, 2, 1.5f, 1});
        table.addCell("ID").addCell("DNI").addCell("Nombres").addCell("Apellidos")
                .addCell("Correo").addCell("Teléfono").addCell("Estado");
        
        List<EstudianteDTO> estudiantes = estudianteService.listarTodos();
        for (EstudianteDTO e : estudiantes) {
            table.addCell(e.getIdEstudiante().toString());
            table.addCell(e.getDni());
            table.addCell(e.getNombres());
            table.addCell(e.getApellidos());
            table.addCell(e.getCorreo());
            table.addCell(e.getTelefono());
            table.addCell(e.getEstado() ? "Activo" : "Inactivo");
        }
        return table;
    }

    private Table exportarSedesPdf() {
        Table table = new Table(new float[]{1, 1.5f, 2, 2, 1.5f, 1});
        table.addCell("ID").addCell("Código").addCell("Nombre").addCell("Dirección")
                .addCell("Ciudad").addCell("Estado");
        
        List<SedeDTO> sedes = sedeService.listarTodos();
        for (SedeDTO s : sedes) {
            table.addCell(s.getIdSede().toString());
            table.addCell(s.getCodigoSede());
            table.addCell(s.getNombre());
            table.addCell(s.getDireccion());
            table.addCell(s.getCiudad());
            table.addCell(s.getEstado() ? "Activo" : "Inactivo");
        }
        return table;
    }

    private Table exportarCarrerasPdf() {
        Table table = new Table(new float[]{1, 1.5f, 2, 1, 1.5f, 1});
        table.addCell("ID").addCell("Código").addCell("Nombre").addCell("Ciclos")
                .addCell("Inversión").addCell("Estado");
        
        List<CarreraDTO> carreras = carreraService.listarTodos();
        for (CarreraDTO c : carreras) {
            table.addCell(c.getIdCarrera().toString());
            table.addCell(c.getCodigoCarrera());
            table.addCell(c.getNombre());
            table.addCell(c.getDuracionCiclos().toString());
            table.addCell(c.getInversion().toString());
            table.addCell(c.getEstado() ? "Activo" : "Inactivo");
        }
        return table;
    }

    private Table exportarProfesoresPdf() {
        Table table = new Table(new float[]{1, 1, 1, 1.5f, 1.5f, 1.5f, 1});
        table.addCell("ID").addCell("DNI").addCell("Nombres").addCell("Apellidos")
                .addCell("Especialidad").addCell("Correo").addCell("Estado");
        
        List<ProfesorDTO> profesores = profesorService.listarTodos();
        for (ProfesorDTO p : profesores) {
            table.addCell(p.getIdProfesor().toString());
            table.addCell(p.getDni());
            table.addCell(p.getNombres());
            table.addCell(p.getApellidos());
            table.addCell(p.getEspecialidad());
            table.addCell(p.getCorreo());
            table.addCell(p.getEstado() ? "Activo" : "Inactivo");
        }
        return table;
    }

    private Table exportarCursosPdf() {
        Table table = new Table(new float[]{1, 1, 2, 1, 2, 2, 1});
        table.addCell("ID").addCell("Código").addCell("Nombre").addCell("Créditos")
                .addCell("Carrera").addCell("Profesor").addCell("Estado");
        
        List<CursoDTO> cursos = cursoService.listarTodos();
        for (CursoDTO c : cursos) {
            table.addCell(c.getIdCurso().toString());
            table.addCell(c.getCodigoCurso());
            table.addCell(c.getNombre());
            table.addCell(c.getCreditos().toString());
            table.addCell(c.getNombreCarrera() != null ? c.getNombreCarrera() : "");
            table.addCell(c.getNombreProfesor() != null ? c.getNombreProfesor() : "");
            table.addCell(c.getEstado() ? "Activo" : "Inactivo");
        }
        return table;
    }
}
