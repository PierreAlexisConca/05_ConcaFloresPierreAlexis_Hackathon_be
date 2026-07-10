package recuperacion.ConcaFlores.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {

    private Integer idCurso;

    @NotBlank(message = "El código de curso es obligatorio")
    @Size(max = 10)
    private String codigoCurso;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos mínimos son 1")
    @Max(value = 6, message = "Los créditos máximos son 6")
    private Byte creditos;

    @NotNull(message = "La carrera es obligatoria")
    private Integer idCarrera;

    private String nombreCarrera;

    @NotNull(message = "El profesor es obligatorio")
    private Integer idProfesor;

    private String nombreProfesor;

    private Boolean estado;

    private LocalDateTime deletedAt;

    private LocalDateTime restoredAt;
}
