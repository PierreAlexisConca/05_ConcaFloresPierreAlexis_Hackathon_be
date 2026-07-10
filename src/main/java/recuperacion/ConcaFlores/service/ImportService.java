package recuperacion.ConcaFlores.service;

import lombok.RequiredArgsConstructor;
import recuperacion.ConcaFlores.dto.*;
import recuperacion.ConcaFlores.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final PromotorService promotorService;
    private final EstudianteService estudianteService;
    private final CarreraService carreraService;
    private final SedeService sedeService;
    private final ProfesorService profesorService;
    private final CursoService cursoService;

    /**
     * Importa Promotores desde CSV
     * Formato esperado: codigo_promotor,dni,nombres,apellidos,telefono,correo
     */
    public String importarPromotoresCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("El archivo está vacío");

        List<PromotorDTO> promotores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header
                
                String[] fields = line.split(",");
                if (fields.length != 6) {
                    throw new BusinessException("Línea " + lineNum + ": Formato incorrecto. Se esperan 6 campos.");
                }

                PromotorDTO dto = PromotorDTO.builder()
                        .codigoPromotor(fields[0].trim())
                        .dni(fields[1].trim())
                        .nombres(fields[2].trim())
                        .apellidos(fields[3].trim())
                        .telefono(fields[4].trim())
                        .correo(fields[5].trim())
                        .estado(true)
                        .build();

                promotores.add(dto);
            }
        }

        int count = 0;
        for (PromotorDTO dto : promotores) {
            try {
                promotorService.crear(dto);
                count++;
            } catch (Exception e) {
                // Continúa con el siguiente registro
            }
        }

        return "Se importaron " + count + " promotores de " + promotores.size() + " registros.";
    }

    /**
     * Importa Estudiantes desde CSV
     * Formato esperado: dni,nombres,apellidos,correo,telefono,direccion
     */
    public String importarEstudiantesCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("El archivo está vacío");

        List<EstudianteDTO> estudiantes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header

                String[] fields = line.split(",");
                if (fields.length < 5 || fields.length > 6) {
                    throw new BusinessException("Línea " + lineNum + ": Formato incorrecto. Se esperan 5-6 campos.");
                }

                EstudianteDTO dto = EstudianteDTO.builder()
                        .dni(fields[0].trim())
                        .nombres(fields[1].trim())
                        .apellidos(fields[2].trim())
                        .correo(fields[3].trim())
                        .telefono(fields[4].trim())
                        .direccion(fields.length > 5 ? fields[5].trim() : null)
                        .estado(true)
                        .build();

                estudiantes.add(dto);
            }
        }

        int count = 0;
        for (EstudianteDTO dto : estudiantes) {
            try {
                estudianteService.crear(dto);
                count++;
            } catch (Exception e) {
                // Continúa con el siguiente registro
            }
        }

        return "Se importaron " + count + " estudiantes de " + estudiantes.size() + " registros.";
    }

    /**
     * Importa Sedes desde CSV
     * Formato esperado: codigo_sede,nombre,direccion,ciudad
     */
    public String importarSedesCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("El archivo está vacío");

        List<SedeDTO> sedes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header

                String[] fields = line.split(",");
                if (fields.length != 4) {
                    throw new BusinessException("Línea " + lineNum + ": Formato incorrecto. Se esperan 4 campos.");
                }

                SedeDTO dto = SedeDTO.builder()
                        .codigoSede(fields[0].trim())
                        .nombre(fields[1].trim())
                        .direccion(fields[2].trim())
                        .ciudad(fields[3].trim())
                        .estado(true)
                        .build();

                sedes.add(dto);
            }
        }

        int count = 0;
        for (SedeDTO dto : sedes) {
            try {
                sedeService.crear(dto);
                count++;
            } catch (Exception e) {
                // Continúa con el siguiente registro
            }
        }

        return "Se importaron " + count + " sedes de " + sedes.size() + " registros.";
    }

    /**
     * Importa Carreras desde CSV
     * Formato esperado: codigo_carrera,nombre,duracion_ciclos,inversion
     */
    public String importarCarrerasCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("El archivo está vacío");

        List<CarreraDTO> carreras = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header

                String[] fields = line.split(",");
                if (fields.length != 4) {
                    throw new BusinessException("Línea " + lineNum + ": Formato incorrecto. Se esperan 4 campos.");
                }

                CarreraDTO dto = CarreraDTO.builder()
                        .codigoCarrera(fields[0].trim())
                        .nombre(fields[1].trim())
                        .duracionCiclos(Byte.valueOf(fields[2].trim()))
                        .inversion(new BigDecimal(fields[3].trim()))
                        .estado(true)
                        .build();

                carreras.add(dto);
            }
        }

        int count = 0;
        for (CarreraDTO dto : carreras) {
            try {
                carreraService.crear(dto);
                count++;
            } catch (Exception e) {
                // Continúa con el siguiente registro
            }
        }

        return "Se importaron " + count + " carreras de " + carreras.size() + " registros.";
    }

    /**
     * Importa Profesores desde CSV
     * Formato esperado: codigo_profesor,dni,nombres,apellidos,especialidad,telefono,correo
     */
    public String importarProfesoresCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("El archivo está vacío");

        List<ProfesorDTO> profesores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header

                String[] fields = line.split(",");
                if (fields.length != 7) {
                    throw new BusinessException("Línea " + lineNum + ": Formato incorrecto. Se esperan 7 campos.");
                }

                ProfesorDTO dto = ProfesorDTO.builder()
                        .codigoProfesor(fields[0].trim())
                        .dni(fields[1].trim())
                        .nombres(fields[2].trim())
                        .apellidos(fields[3].trim())
                        .especialidad(fields[4].trim())
                        .telefono(fields[5].trim())
                        .correo(fields[6].trim())
                        .estado(true)
                        .build();

                profesores.add(dto);
            }
        }

        int count = 0;
        for (ProfesorDTO dto : profesores) {
            try {
                profesorService.crear(dto);
                count++;
            } catch (Exception e) {
                // Continúa con el siguiente registro
            }
        }

        return "Se importaron " + count + " profesores de " + profesores.size() + " registros.";
    }

    /**
     * Importa Cursos desde CSV
     * Formato esperado: codigo_curso,nombre,creditos,id_carrera,id_profesor
     */
    public String importarCursosCsv(MultipartFile file) throws Exception {
        if (file.isEmpty()) throw new BusinessException("El archivo está vacío");

        List<CursoDTO> cursos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (lineNum == 1) continue; // Skip header

                String[] fields = line.split(",");
                if (fields.length != 5) {
                    throw new BusinessException("Línea " + lineNum + ": Formato incorrecto. Se esperan 5 campos.");
                }

                CursoDTO dto = CursoDTO.builder()
                        .codigoCurso(fields[0].trim())
                        .nombre(fields[1].trim())
                        .creditos(Byte.valueOf(fields[2].trim()))
                        .idCarrera(Integer.parseInt(fields[3].trim()))
                        .idProfesor(Integer.parseInt(fields[4].trim()))
                        .estado(true)
                        .build();

                cursos.add(dto);
            }
        }

        int count = 0;
        for (CursoDTO dto : cursos) {
            try {
                cursoService.crear(dto);
                count++;
            } catch (Exception e) {
                // Continúa con el siguiente registro
            }
        }

        return "Se importaron " + count + " cursos de " + cursos.size() + " registros.";
    }
}
