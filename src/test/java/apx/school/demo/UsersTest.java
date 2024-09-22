package apx.school.demo;

import apx.school.demo.Control.UserController;
import apx.school.demo.Dto.UserDto;
import apx.school.demo.Servicio.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsersTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllUsers_ReturnsListOfUsers() {
        List<UserDto> userList = Arrays.asList(new UserDto("14242","User1", "user1@example.com"), new UserDto("2", "John", "john@example.com"));
        when(userService.getAll()).thenReturn(userList);

        ResponseEntity<List<UserDto>> response = userController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void findUserById_UserExists() {
        String userId = "123";
        UserDto userDto = new UserDto(userId, "José", "jose@example.com");
        when(userService.getById(userId)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jose@example.com", response.getBody().getEmail());
    }

    @Test
    void findUserById_UserDoesNotExist() {
        String userId = "12345";
        when(userService.getById(userId)).thenReturn(null); // Aquí retornas null para simular que no se encontró el usuario

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.findById(userId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createUser_NewUser() {
        UserDto userDto = new UserDto("14242", "Pedro", "pedro@example.com");
        when(userService.save(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.save(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("pedro@example.com", response.getBody().getEmail());
    }

    @Test
    void updateUser_UserExists() {
        String userId = "123";
        UserDto userDto = new UserDto(userId,"Joe", "joe@example.com");
        when(userService.update(userDto, userId)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.update(userDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("joe@example.com", response.getBody().getEmail());
    }

    @Test
    void deleteUser_UserExists() {
        String userId = "123";
        doNothing().when(userService).delete(userId);

        ResponseEntity<Void> response = userController.delete(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).delete(userId);
    }
}
