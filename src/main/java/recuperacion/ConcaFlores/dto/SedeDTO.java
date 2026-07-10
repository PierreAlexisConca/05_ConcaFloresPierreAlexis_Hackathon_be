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
public class SedeDTO {

    private Integer idSede;

    @NotBlank(message = "El código de sede es obligatorio")
    @Size(max = 10)
    private String codigoSede;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150)
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 50)
    private String ciudad;

    private Boolean estado;

    private LocalDateTime deletedAt;

    private LocalDateTime restoredAt;
}
