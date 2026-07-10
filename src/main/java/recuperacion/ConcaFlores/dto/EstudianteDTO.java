package recuperacion.ConcaFlores.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDTO {

    private Integer idEstudiante;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100)
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15)
    private String telefono;

    @Size(max = 150)
    private String direccion;

    private LocalDate fechaNacimiento;

    private Boolean estado;

    private LocalDateTime deletedAt;

    private LocalDateTime restoredAt;
}
