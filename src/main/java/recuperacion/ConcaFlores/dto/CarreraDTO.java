package recuperacion.ConcaFlores.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarreraDTO {

    private Integer idCarrera;

    @NotBlank(message = "El código de carrera es obligatorio")
    @Size(max = 10)
    private String codigoCarrera;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotNull(message = "La duración en ciclos es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 ciclo")
    private Byte duracionCiclos;

    @NotNull(message = "La inversión es obligatoria")
    @DecimalMin(value = "0.01", message = "La inversión debe ser mayor a 0")
    private BigDecimal inversion;

    private Boolean estado;

    private LocalDateTime deletedAt;

    private LocalDateTime restoredAt;
}
