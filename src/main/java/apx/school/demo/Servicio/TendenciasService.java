package apx.school.demo.Servicio;

import apx.school.demo.Dto.TendenciaDto;
import apx.school.demo.Repository.SQL.IngresoRepository;
import apx.school.demo.Repository.SQL.SQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TendenciasService {

    @Autowired
    private SQLRepository gastoRepository;

    @Autowired
    private IngresoRepository ingresoRepository;

//    public List<TendenciaDto> obtenerTendenciaMensual(String userId) {
//        List<Object[]> gastos = gastoRepository.findGastosPorMes(userId);
//        List<Object[]> ingresos = ingresoRepository.findIngresosPorMes(userId);
//
//        return combinarTendencias(gastos, ingresos);
//    }

//    private List<TendenciaDto> combinarTendencias(List<Object[]> gastos, List<Object[]> ingresos) {
//        List<TendenciaDto> tendencias = new ArrayList<>();
//
//        LocalDate currentDate = LocalDate.now();  // la fecha actual
//        int currentMonth = currentDate.getMonthValue();  // el mes actual (1 = enero, 12 = diciembre)
//
//        for (int mes = currentMonth; mes <= currentMonth; mes++) {
//            int finalMes = mes;
//            double totalGastos = gastos.stream()
//                    .filter(g -> ((Number) g[0]).intValue() == finalMes)
//                    .mapToDouble(g -> (double) g[1])
//                    .sum();
//
//            double totalIngresos = ingresos.stream()
//                    .filter(i -> ((Number) i[0]).intValue() == finalMes)
//                    .mapToDouble(i -> (double) i[1])
//                    .sum();
//
//            tendencias.add(new TendenciaDto(mes, totalIngresos, totalGastos, totalIngresos - totalGastos));
//        }
//
//        return tendencias;
//    }

    public List<TendenciaDto> calcularTendencias(String userId) {
        List<Object[]> gastos = gastoRepository.findGastosPorMes(userId);
        List<Object[]> ingresos = ingresoRepository.findIngresosPorMes(userId);

        List<TendenciaDto> tendencias = new ArrayList<>();

        for (int mes = 1; mes <= 12; mes++) {
            int finalMes = mes;
            double totalGastos = gastos.stream()
                    .filter(g -> ((Number) g[0]).intValue() == finalMes)
                    .mapToDouble(g -> (double) g[1])
                    .sum();

            double totalIngresos = ingresos.stream()
                    .filter(i -> ((Number) i[0]).intValue() == finalMes)
                    .mapToDouble(i -> (double) i[1])
                    .sum();

            tendencias.add(new TendenciaDto(finalMes, totalIngresos, totalGastos, totalIngresos - totalGastos));
        }

        return tendencias;
    }

}
