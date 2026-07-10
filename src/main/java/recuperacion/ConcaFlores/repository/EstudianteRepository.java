package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {

    List<Estudiante> findByEstadoTrue();
    List<Estudiante> findByDeletedAtIsNull();

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);

    boolean existsByDniAndIdEstudianteNot(String dni, Integer id);

    boolean existsByCorreoAndIdEstudianteNot(String correo, Integer id);

    Optional<Estudiante> findByDni(String dni);

    Optional<Estudiante> findByCorreo(String correo);
}
