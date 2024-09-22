package apx.school.demo.Servicio;

import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Entity.IngresoEntity;
import apx.school.demo.Repository.SQL.IngresoRepository;
import apx.school.demo.Repository.SQL.SQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IngresoService {

    @Autowired
    @Qualifier("ingRepository")
    private IngresoRepository ingRepository;

    @Autowired
    @Qualifier("sqlRepository")
    private SQLRepository sqlRepository;

    public double sumaIngresoporUserId(String userId){
        return ingRepository.sumaIngresoporUserId(userId);
    }


    public IngresoEntity saveIngreso(IngresoEntity ingreso) {
        if (ingreso.getFecha() == null) {
            ingreso.setFecha(new Date());
        }
        return ingRepository.save(ingreso);
    }


    public List<IngresoEntity> findIngresosByUserId(String userId) {
        return ingRepository.findIngresoByUserId(userId);
    }

    public Optional<IngresoEntity> findIngresoByIdAndUserId(Long id, String userId) {
        return ingRepository.findIngresoByIdAndUserId(id, userId);
    }

    public void deleteIngreso(IngresoEntity ingreso) {
        ingRepository.delete(ingreso);
    }

    public Double TotalGastos(String userId) {
        try {
            return sqlRepository.sumaMontoporUserId(userId);
        } catch (Exception e) {
            System.out.println("Error al sumar gastos para el usuario " + userId);
            throw e;
        }
    }

    public Double TotalIngresos(String userId) {
        try {
            return ingRepository.sumaIngresoporUserId(userId);
        } catch (Exception e) {
            System.out.println("Error al sumar ingresos para el usuario " + userId);
            throw e;
        }
    }

    public double getBalanceFinanciero(String userId) {
        double totalGastos = TotalGastos(userId);
        double totalIngresos = TotalIngresos(userId);
        return totalIngresos - totalGastos;
    }

}
