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
public class PromotorDTO {

    private Integer idPromotor;

    @NotBlank(message = "El código de promotor es obligatorio")
    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigoPromotor;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100)
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15)
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100)
    private String correo;

    private Boolean estado;

    private LocalDateTime deletedAt;

    private LocalDateTime restoredAt;
}
