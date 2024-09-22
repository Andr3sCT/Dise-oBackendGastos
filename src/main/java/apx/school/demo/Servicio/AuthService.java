package apx.school.demo.Servicio;


import apx.school.demo.Dto.auth.LoginResponseDto;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Config.JwtService;
import apx.school.demo.Util.Role;
import apx.school.demo.Dto.auth.AuthDto;
import apx.school.demo.Dto.auth.LoginDto;
import apx.school.demo.Dto.auth.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Qualifier("mongoDBRepository")
    private MongoDBRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    @Autowired
    private GastoService gastoService;

    @Autowired
    private IngresoService ingresoService;

    @Autowired
    public AuthService(MongoDBRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponseDto login(final LoginDto request){
        try {
            System.out.println("Inicio de sesión para usuario: " + request.getEmail());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

// Aquí hacemos el cast correcto a UserEntity para obtener el ID
            UserEntity userEntity = (UserEntity) userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + request.getEmail()));

            System.out.println("Usuario autenticado, generando token.");

// Genera el token
            String token = jwtService.getToken(userEntity);

            System.out.println("Agarrando el ID del usuario");

// Obtiene el userId desde la entidad del usuario autenticado
            String userId = userEntity.getId();  // Ahora obtienes el ID correctamente

            System.out.println("Generando el total de los gastos");
            Double totalGastos = gastoService.sumaGastoporUserId(userId);  // Asegúrate de usar el servicio correcto
            System.out.println("Total gastos: " + totalGastos);

            System.out.println("Generando el total de los ingresos");
            Double totalIngresos = ingresoService.sumaIngresoporUserId(userId);
            System.out.println("Total ingresos: " + totalIngresos);

            System.out.println("Usuario autenticado: " + request.getEmail());
            System.out.println("ID del usuario: " + userId);

// Retorna el response con las sumas calculadas
            return new LoginResponseDto(token, "Ha iniciado sesión",
                    String.valueOf(totalGastos != null ? totalGastos : 0),  // Si es null, retorna 0
                    String.valueOf(totalIngresos != null ? totalIngresos : 0));  // Si es null, retorna 0


        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado", e);

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas", e);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado durante el inicio de sesión", e);
        }
    }

    public AuthDto register(final RegisterDto request){
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return new AuthDto(this.jwtService.getToken(user));
    }
}
