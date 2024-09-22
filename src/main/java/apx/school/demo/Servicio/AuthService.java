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
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + request.getEmail()));

            String token = jwtService.getToken(user);

            String userId = ((UserEntity) user).getId();

            String total = String.valueOf(gastoService.sumaGastoporUserId(userId));


            return new LoginResponseDto(token, "Ha iniciado sesión", total);


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
