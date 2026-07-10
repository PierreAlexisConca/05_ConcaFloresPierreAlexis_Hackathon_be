package recuperacion.ConcaFlores.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Carreras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrera")
    private Integer idCarrera;

    @Column(name = "codigo_carrera", nullable = false, length = 10, unique = true)
    private String codigoCarrera;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "duracion_ciclos", nullable = false)
    private Byte duracionCiclos;

    @Column(name = "inversion", nullable = false, precision = 10, scale = 2)
    private BigDecimal inversion;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    @OneToMany(mappedBy = "carrera", fetch = FetchType.LAZY)
    private List<Curso> cursos;

    @OneToMany(mappedBy = "carrera", fetch = FetchType.LAZY)
    private List<Matricula> matriculas;
}
