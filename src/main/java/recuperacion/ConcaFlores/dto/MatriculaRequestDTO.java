package recuperacion.ConcaFlores.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculaRequestDTO {

    @NotBlank(message = "El código de matrícula es obligatorio")
    @Size(max = 15)
    private String codigoMatricula;

    @NotNull(message = "La fecha de matrícula es obligatoria")
    private LocalDate fechaMatricula;

    @NotBlank(message = "El período es obligatorio")
    @Size(max = 10)
    private String periodo;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoTotal;

    @NotNull(message = "El estudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "El promotor es obligatorio")
    private Integer idPromotor;

    @NotNull(message = "La sede es obligatoria")
    private Integer idSede;

    @NotNull(message = "La carrera es obligatoria")
    private Integer idCarrera;

    @NotEmpty(message = "Debe seleccionar al menos un curso")
    private List<Integer> idCursos;
}
