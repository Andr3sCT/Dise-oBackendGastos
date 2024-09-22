package apx.school.demo.Control;

import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Servicio.GastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/auth/api/gastos")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @Autowired
    @Qualifier("mongoDBRepository")
    private MongoDBRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createGasto(@RequestBody GastoEntity gasto, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + userDetails.getUsername()));

        gasto.setUserId(userEntity.getId());

        GastoEntity savedGasto = gastoService.saveGasto(gasto);

        return ResponseEntity.ok(savedGasto);
    }

    @GetMapping
    public ResponseEntity<List<GastoEntity>> getGastos(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        List<GastoEntity> gastos = gastoService.findGastosByUserId(userId);
        if (gastos.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto no encontrado");
        }
        return ResponseEntity.ok(gastos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoEntity> getGastoById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        GastoEntity gasto = gastoService.findGastoByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto no encontrado"));
        return ResponseEntity.ok(gasto);
    }

    // Actualiza un gasto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGasto(@PathVariable Long id, @RequestBody GastoEntity updatedGasto, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        GastoEntity gasto = gastoService.findGastoByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto no encontrado"));

        // Solo permite actualizar los campos necesarios
        gasto.setMonto(updatedGasto.getMonto());
        gasto.setCategoria(updatedGasto.getCategoria());
        gasto.setDescripcion(updatedGasto.getDescripcion());

        GastoEntity savedGasto = gastoService.saveGasto(gasto);
        return ResponseEntity.ok(savedGasto);
    }

    // Elimina un gasto existente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGasto(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = getUserId(userDetails);
        GastoEntity gasto = gastoService.findGastoByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gasto no encontrado"));

        gastoService.deleteGasto(gasto);
        return ResponseEntity.noContent().build();
    }

    // Obtiene el ID del usuario autenticado
    private String getUserId(UserDetails userDetails) {
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + userDetails.getUsername()));
        return userEntity.getId();
    }

}
