package apx.school.demo.Control;

import apx.school.demo.Entity.IngresoEntity;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Servicio.IngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/auth/api/ingresos")
public class IngresosController {

    @Autowired
    private IngresoService ingresoService;

    @Autowired
    @Qualifier("mongoDBRepository")
    private MongoDBRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createIngreso(@RequestBody IngresoEntity ingreso, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + userDetails.getUsername()));

        ingreso.setUserId(userEntity.getId());

        IngresoEntity savedIngreso = ingresoService.saveIngreso(ingreso);

        return ResponseEntity.ok(savedIngreso);
    }

    @GetMapping
    public ResponseEntity<List<IngresoEntity>> getIngresos(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        List<IngresoEntity> ingresos = ingresoService.findIngresosByUserId(userId);
        if (ingresos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingreso no encontrado");
        }
        return ResponseEntity.ok(ingresos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoEntity> getIngresoById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        IngresoEntity ingreso = ingresoService.findIngresoByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingreso no encontrado"));
        return ResponseEntity.ok(ingreso);
    }

    // Actualiza un ingreso existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateIngreso(@PathVariable Long id, @RequestBody IngresoEntity updatedIngreso, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        IngresoEntity ingreso = ingresoService.findIngresoByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingreso no encontrado"));

        // Solo permite actualizar los campos necesarios
        ingreso.setMonto(updatedIngreso.getMonto());
        ingreso.setCategoria(updatedIngreso.getCategoria());
        ingreso.setDescripcion(updatedIngreso.getDescripcion());

        IngresoEntity savedIngreso = ingresoService.saveIngreso(ingreso);
        return ResponseEntity.ok(savedIngreso);
    }

    // Elimina un ingreso existente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIngreso(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        IngresoEntity ingreso = ingresoService.findIngresoByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingreso no encontrado"));

        ingresoService.deleteIngreso(ingreso);
        return ResponseEntity.noContent().build();
    }

    // Obtiene el ID del usuario autenticado
    private String getUserId(UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + userDetails.getUsername()));
        return userEntity.getId();
    }

}
