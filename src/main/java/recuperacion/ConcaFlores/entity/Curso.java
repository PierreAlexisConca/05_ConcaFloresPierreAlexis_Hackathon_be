package recuperacion.ConcaFlores.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "codigo_curso", nullable = false, length = 10, unique = true)
    private String codigoCurso;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "creditos", nullable = false)
    private Byte creditos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Carrera carrera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor", nullable = false)
    private Profesor profesor;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<DetalleMatricula> detalles;
}
