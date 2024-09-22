package apx.school.demo.Repository.SQL;

import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Entity.IngresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("ingRepository")
public interface IngresoRepository extends JpaRepository<IngresoEntity, String> {

    List<IngresoEntity> findIngresoByUserId(String userId);

    Optional<IngresoEntity> findIngresoByIdAndUserId(Long id, String userId);

    @Query("SELECT COALESCE(SUM(i.monto), 0) FROM IngresoEntity i WHERE i.userId = ?1")
    Double sumaIngresoporUserId(String userId);

    @Query("SELECT EXTRACT(MONTH FROM i.fecha) AS mes, SUM(i.monto) AS total "
            + "FROM IngresoEntity i WHERE i.userId = ?1 GROUP BY mes ORDER BY mes")
    List<Object[]> findIngresosPorMes(String userId);
}
