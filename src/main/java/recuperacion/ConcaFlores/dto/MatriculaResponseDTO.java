package recuperacion.ConcaFlores.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculaResponseDTO {

    private Integer idMatricula;
    private String codigoMatricula;
    private LocalDate fechaMatricula;
    private String periodo;
    private BigDecimal montoTotal;
    private Boolean estado;
    private LocalDateTime deletedAt;
    private LocalDateTime restoredAt;

    // Datos del estudiante
    private Integer idEstudiante;
    private String nombreEstudiante;
    private String dniEstudiante;

    // Datos del promotor
    private Integer idPromotor;
    private String nombrePromotor;
    private String codigoPromotor;

    // Datos de la sede
    private Integer idSede;
    private String nombreSede;

    // Datos de la carrera
    private Integer idCarrera;
    private String nombreCarrera;

    // Cursos matriculados
    private List<CursoDTO> cursos;
}
