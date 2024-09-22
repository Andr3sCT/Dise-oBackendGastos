package apx.school.demo.Control;

import apx.school.demo.Dto.TendenciaDto;
import apx.school.demo.Dto.UserDto;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Servicio.TendenciasService;
import apx.school.demo.Servicio.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TendenciasService tendenciaService;


    @GetMapping("/")
    public ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> lista = this.userService.getAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") String id) {
        UserDto usuario = this.userService.getById(id);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto){
        UserDto saved = this.userService.save(userDto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto, @PathVariable("id") String id){
        UserDto updated = this.userService.update(userDto, id);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id){
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tendencias/{userId}")
    public ResponseEntity<List<TendenciaDto>> obtenerTendenciaMensual(@PathVariable String userId) {
        List<TendenciaDto> tendencias = tendenciaService.calcularTendencias(userId);
        return ResponseEntity.ok(tendencias);
    }
}
