package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {

    List<Profesor> findByEstadoTrue();
    List<Profesor> findByDeletedAtIsNull();

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);

    boolean existsByDniAndIdProfesorNot(String dni, Integer id);

    boolean existsByCorreoAndIdProfesorNot(String correo, Integer id);
}
