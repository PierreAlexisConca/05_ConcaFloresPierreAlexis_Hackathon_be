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
public class ProfesorDTO {

    private Integer idProfesor;

    @NotBlank(message = "El código de profesor es obligatorio")
    @Size(max = 10)
    private String codigoProfesor;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100)
    private String especialidad;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100)
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15)
    private String telefono;

    private Boolean estado;

    private LocalDateTime deletedAt;

    private LocalDateTime restoredAt;
}
