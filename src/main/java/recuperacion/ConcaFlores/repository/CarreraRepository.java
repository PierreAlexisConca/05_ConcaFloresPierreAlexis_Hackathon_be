package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {

    List<Carrera> findByEstadoTrue();
    List<Carrera> findByDeletedAtIsNull();

    boolean existsByCodigoCarrera(String codigoCarrera);
}
