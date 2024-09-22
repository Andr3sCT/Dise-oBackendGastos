package apx.school.demo.Control;

import apx.school.demo.Dto.TendenciaDto;
import apx.school.demo.Dto.auth.LoginDto;
import apx.school.demo.Entity.IngresoEntity;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Servicio.TendenciasService;
import apx.school.demo.Servicio.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth/api/finanzas")
public class FinanzasController {

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("mongoDBRepository")
    private MongoDBRepository userRepository;

    @Autowired
    private TendenciasService tendenciaService;

    @GetMapping
    public ResponseEntity<Map<String, Double>> getResumen(@AuthenticationPrincipal UserDetails userDetails) {
        try{
        UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + userDetails.getUsername()));
            System.out.println("Agarrando el ID del usuario");
        String userId = userEntity.getId();
        Map<String, Double> resumen = userService.getResumenFinanciero(userId);
        return ResponseEntity.ok(resumen);
    } catch (Exception e){
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado durante la operación", e);
    }
    }

    @GetMapping("/tendencias")
    public ResponseEntity<List<TendenciaDto>> obtenerTendenciaMensual(@AuthenticationPrincipal UserDetails userDetails) {
       try {
           UserEntity userEntity = userRepository.findByEmail(userDetails.getUsername())
                   .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + userDetails.getUsername()));


           System.out.println("Agarrando el ID del usuario");
           String userId = userEntity.getId();

           List<TendenciaDto> tendencias = tendenciaService.calcularTendencias(userId);
           return ResponseEntity.ok(tendencias);
       } catch (Exception e){
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado durante la operación", e);
       }
    }
}
