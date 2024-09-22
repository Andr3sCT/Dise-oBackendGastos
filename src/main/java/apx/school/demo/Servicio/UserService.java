package apx.school.demo.Servicio;

import apx.school.demo.Dto.UserDto;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Repository.SQL.IngresoRepository;
import apx.school.demo.Repository.SQL.SQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    @Qualifier("mongoDBRepository")
    private MongoDBRepository userMongoRepository;

    @Autowired
    private IngresoService ingresoService;

    @Autowired
    private GastoService gastoService;


    public List<UserDto> getAll(){
        return this.userMongoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto getById(String id){
        try{
        return this.userMongoRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
    }
    }

    public UserDto save(UserDto user){
        UserEntity entity = new UserEntity();
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        UserEntity entitySaved = this.userMongoRepository.save(entity);
        UserDto saved = this.toDto(entitySaved);
        return saved;
    }

    public UserDto update(UserDto user, String id){
        UserEntity entity = this.userMongoRepository.findById(id).orElse(null);
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        UserEntity entitySaved = this.userMongoRepository.save(entity);
        UserDto saved = this.toDto(entitySaved);
        return saved;
    }

    public void delete(String id){
        UserEntity entity = this.userMongoRepository.findById(id).orElse(null);
        this.userMongoRepository.delete(entity);
    }

    private UserDto toDto(UserEntity entity){
        return new UserDto(entity.getId(), entity.getName(), entity.getEmail());
    }

    public Map<String, Double> getResumenFinanciero(String userId) {
        double totalGastos = ingresoService.TotalGastos(userId);
        double totalIngresos = ingresoService.TotalIngresos(userId);

        Map<String, Double> resumen = new HashMap<>();
        resumen.put("Total de gastos:", totalGastos);
        resumen.put("Total de ingresos:", totalIngresos);
        resumen.put("Balance total:", totalIngresos - totalGastos);

        return resumen;
    }
}
