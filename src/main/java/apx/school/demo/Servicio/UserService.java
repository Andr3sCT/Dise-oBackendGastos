package apx.school.demo.Servicio;

import apx.school.demo.Dto.UserDto;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    @Qualifier("mongoDBRepository")
    private MongoDBRepository userMongoRepository;

    public List<UserDto> getAll(){
        // return new ArrayList<UserDto>(list.values());
        return this.userMongoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto getById(String id){
        //return list.get(id);
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
        //list.remove(id);
        UserEntity entity = this.userMongoRepository.findById(id).orElse(null);
        this.userMongoRepository.delete(entity);
    }

    private UserDto toDto(UserEntity entity){
        return new UserDto(entity.getId(), entity.getName(), entity.getEmail());
    }
}
