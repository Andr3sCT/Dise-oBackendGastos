package apx.school.demo.Repository.SQL;

import apx.school.demo.Entity.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("sqlRepository")
public interface SQLRepository extends JpaRepository <GastoEntity, String> {
    List<GastoEntity> findByUserId(String userId);
    Optional<GastoEntity> findByIdAndUserId(Long id, String userId);
}
