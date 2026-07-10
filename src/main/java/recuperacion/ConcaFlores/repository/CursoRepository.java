package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    List<Curso> findByEstadoTrue();
    List<Curso> findByDeletedAtIsNull();

    List<Curso> findByCarrera_IdCarreraAndEstadoTrue(Integer idCarrera);

    boolean existsByCodigoCurso(String codigoCurso);
}
