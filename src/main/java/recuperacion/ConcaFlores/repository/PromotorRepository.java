package recuperacion.ConcaFlores.repository;

import recuperacion.ConcaFlores.entity.Promotor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromotorRepository extends JpaRepository<Promotor, Integer> {

    // Solo activos (deleted_at IS NULL)
    List<Promotor> findByDeletedAtIsNull();

    Optional<Promotor> findByIdPromotorAndDeletedAtIsNull(Integer id);

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);

    boolean existsByDniAndIdPromotorNot(String dni, Integer id);

    boolean existsByCorreoAndIdPromotorNot(String correo, Integer id);

    Optional<Promotor> findByDni(String dni);
}
