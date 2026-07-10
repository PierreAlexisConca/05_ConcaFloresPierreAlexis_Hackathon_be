package recuperacion.ConcaFlores.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Sedes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Integer idSede;

    @Column(name = "codigo_sede", nullable = false, length = 10, unique = true)
    private String codigoSede;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "restored_at")
    private LocalDateTime restoredAt;

    @OneToMany(mappedBy = "sede", fetch = FetchType.LAZY)
    private List<Matricula> matriculas;
}
