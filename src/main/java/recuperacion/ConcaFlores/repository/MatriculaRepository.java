package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    List<Matricula> findByEstadoTrue();

    List<Matricula> findAll();

    Optional<Matricula> findByCodigoMatricula(String codigoMatricula);

    // Validar duplicado: mismo estudiante, carrera y periodo
    boolean existsByEstudiante_IdEstudianteAndCarrera_IdCarreraAndPeriodo(
            Integer idEstudiante, Integer idCarrera, String periodo);

    List<Matricula> findByPromotor_IdPromotor(Integer idPromotor);

    List<Matricula> findByEstudiante_IdEstudiante(Integer idEstudiante);
}
