package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.DetalleMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleMatriculaRepository extends JpaRepository<DetalleMatricula, Integer> {

    List<DetalleMatricula> findByMatricula_IdMatricula(Integer idMatricula);

    void deleteByMatricula_IdMatricula(Integer idMatricula);
}
