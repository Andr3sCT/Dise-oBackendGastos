package apx.school.demo.Repository.SQL;

import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Entity.IngresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("sqlRepository")
public interface SQLRepository extends JpaRepository <GastoEntity, String> {
    List<GastoEntity> findByUserId(String userId);

    Optional<GastoEntity> findByIdAndUserId(Long id, String userId);

    @Query("SELECT COALESCE(SUM(g.monto), 0) FROM GastoEntity g WHERE g.userId = ?1")
    Double sumaMontoporUserId(String userId);

    @Query("SELECT EXTRACT(MONTH FROM g.fecha) AS mes, SUM(g.monto) AS total "
            + "FROM GastoEntity g WHERE g.userId = ?1 GROUP BY mes ORDER BY mes")
    List<Object[]> findGastosPorMes(String userId);
}
