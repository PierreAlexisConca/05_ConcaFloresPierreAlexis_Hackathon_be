package recuperacion.ConcaFlores.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Promotores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promotor")
    private Integer idPromotor;

    @Column(name = "codigo_promotor", nullable = false, length = 10, unique = true)
    private String codigoPromotor;

    @Column(name = "dni", nullable = false, length = 8, unique = true)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "correo", nullable = false, length = 100, unique = true)
    private String correo;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    @OneToMany(mappedBy = "promotor", fetch = FetchType.LAZY)
    private List<Matricula> matriculas;
}
