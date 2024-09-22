package apx.school.demo.Servicio;

import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Repository.SQL.SQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GastoService {

    @Autowired
    @Qualifier("sqlRepository")
    private SQLRepository sqlRepository;

    public double sumaGastoporUserId(String userId){
        return sqlRepository.sumaMontoporUserId(userId);
    }


    public GastoEntity saveGasto(GastoEntity gasto) {
        if (gasto.getFecha() == null) {
            gasto.setFecha(new Date());
        }
        return sqlRepository.save(gasto);
    }


    public List<GastoEntity> findGastosByUserId(String userId) {
        return sqlRepository.findByUserId(userId);
    }

    public Optional<GastoEntity> findGastoByIdAndUserId(Long id, String userId) {
        return sqlRepository.findByIdAndUserId(id, userId);
    }

    public void deleteGasto(GastoEntity gasto) {
        sqlRepository.delete(gasto);
    }
}
