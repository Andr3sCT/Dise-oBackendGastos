package apx.school.demo;

import apx.school.demo.Control.IngresosController;
import apx.school.demo.Entity.IngresoEntity;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Repository.SQL.SQLRepository;
import apx.school.demo.Servicio.IngresoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class IngresosTest {

    @Mock
    private SQLRepository sqlRepository;

    @InjectMocks
    private IngresosController ingresoController;

    @Mock
    private IngresoService ingresoService;

    @Mock
    private MongoDBRepository userRepository;

    @Mock
    private UserDetails userDetails;

    private UserEntity userEntity;
    private IngresoEntity ingresoEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId("20");
        userEntity.setEmail("test@ejemplo.com");

        ingresoEntity = new IngresoEntity();
        ingresoEntity.setId(1L);
        ingresoEntity.setUserId(userEntity.getId());
        ingresoEntity.setMonto(100.0);
        ingresoEntity.setCategoria("Insumos");
        ingresoEntity.setDescripcion("Gasolina y otros");

        when(userDetails.getUsername()).thenReturn(userEntity.getEmail());
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    }

    @Test
    public void testSaveIngreso() {
        when(ingresoService.saveIngreso(ingresoEntity)).thenReturn(ingresoEntity);
        ResponseEntity<?> response = ingresoController.createIngreso(ingresoEntity, userDetails);

        verify(ingresoService, times(1)).saveIngreso(ingresoEntity);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingresoEntity, response.getBody());
    }

    @Test
    public void testGetIngresos() {
        when(ingresoService.findIngresosByUserId(userEntity.getId())).thenReturn(List.of(ingresoEntity));

        ResponseEntity<List<IngresoEntity>> response = ingresoController.getIngresos(userDetails);

        verify(ingresoService, times(1)).findIngresosByUserId(userEntity.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetIngresoById() {
        when(ingresoService.findIngresoByIdAndUserId(ingresoEntity.getId(), userEntity.getId())).thenReturn(Optional.of(ingresoEntity));
        ResponseEntity<IngresoEntity> response = ingresoController.getIngresoById(ingresoEntity.getId(), userDetails);

        verify(ingresoService, times(1)).findIngresoByIdAndUserId(ingresoEntity.getId(), userEntity.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ingresoEntity, response.getBody());
    }

    @Test
    public void testUpdateIngreso() {
        when(ingresoService.findIngresoByIdAndUserId(ingresoEntity.getId(), userEntity.getId())).thenReturn(Optional.of(ingresoEntity));
        when(ingresoService.saveIngreso(ingresoEntity)).thenReturn(ingresoEntity);

        IngresoEntity updatedIngreso = new IngresoEntity();
        updatedIngreso.setMonto(200.0);
        updatedIngreso.setCategoria("Comida");
        updatedIngreso.setDescripcion("Compra de comida para gatos");

        ResponseEntity<?> response = ingresoController.updateIngreso(ingresoEntity.getId(), updatedIngreso, userDetails);

        verify(ingresoService, times(1)).saveIngreso(ingresoEntity);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200.0, ingresoEntity.getMonto());
        assertEquals("Comida", ingresoEntity.getCategoria());
    }

    @Test
    public void testDeleteIngreso() {
        when(ingresoService.findIngresoByIdAndUserId(ingresoEntity.getId(), userEntity.getId())).thenReturn(Optional.of(ingresoEntity));

        ResponseEntity<?> response = ingresoController.deleteIngreso(ingresoEntity.getId(), userDetails);

        verify(ingresoService, times(1)).deleteIngreso(ingresoEntity);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
