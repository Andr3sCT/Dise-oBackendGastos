package apx.school.demo;

import apx.school.demo.Control.GastoController;
import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.Mongo.MongoDBRepository;
import apx.school.demo.Repository.SQL.SQLRepository;
import apx.school.demo.Servicio.GastoService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class GastosTest {

    @Mock
    private SQLRepository sqlRepository;

    @InjectMocks
    private GastoController gastoController;

    @Mock
    private GastoService gastoService;

    @Mock
    private MongoDBRepository userRepository;

    @Mock
    private UserDetails userDetails;

    private UserEntity userEntity;
    private GastoEntity gastoEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        userEntity.setId("20");
        userEntity.setEmail("test@ejemplo.com");

        gastoEntity = new GastoEntity();
        gastoEntity.setId(1L);
        gastoEntity.setUserId(userEntity.getId());
        gastoEntity.setMonto(100.0);
        gastoEntity.setCategoria("Alimentos");
        gastoEntity.setDescripcion("Compras de frutas");

        when(userDetails.getUsername()).thenReturn(userEntity.getEmail());
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    }

    @Test
    public void testSaveGasto() {
        when(gastoService.saveGasto(gastoEntity)).thenReturn(gastoEntity);
        ResponseEntity<?> response = gastoController.createGasto(gastoEntity, userDetails);

        verify(gastoService, times(1)).saveGasto(gastoEntity);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gastoEntity, response.getBody());
    }

    @Test
    public void testGetGastos() {
        when(gastoService.findGastosByUserId(userEntity.getId())).thenReturn(List.of(gastoEntity));

        ResponseEntity<List<GastoEntity>> response = gastoController.getGastos(userDetails);

        verify(gastoService, times(1)).findGastosByUserId(userEntity.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetGastoById() {
        when(gastoService.findGastoByIdAndUserId(gastoEntity.getId(), userEntity.getId())).thenReturn(Optional.of(gastoEntity));
        ResponseEntity<GastoEntity> response = gastoController.getGastoById(gastoEntity.getId(), userDetails);

        verify(gastoService, times(1)).findGastoByIdAndUserId(gastoEntity.getId(), userEntity.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gastoEntity, response.getBody());
    }

    @Test
    public void testUpdateGasto() {
        when(gastoService.findGastoByIdAndUserId(gastoEntity.getId(), userEntity.getId())).thenReturn(Optional.of(gastoEntity));
        when(gastoService.saveGasto(gastoEntity)).thenReturn(gastoEntity);

        GastoEntity updatedGasto = new GastoEntity();
        updatedGasto.setMonto(200.0);
        updatedGasto.setCategoria("Transporte");
        updatedGasto.setDescripcion("Taxi");

        ResponseEntity<?> response = gastoController.updateGasto(gastoEntity.getId(), updatedGasto, userDetails);

        verify(gastoService, times(1)).saveGasto(gastoEntity);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200.0, gastoEntity.getMonto());
        assertEquals("Transporte", gastoEntity.getCategoria());
    }

    @Test
    public void testDeleteGasto() {
        when(gastoService.findGastoByIdAndUserId(gastoEntity.getId(), userEntity.getId())).thenReturn(Optional.of(gastoEntity));

        ResponseEntity<?> response = gastoController.deleteGasto(gastoEntity.getId(), userDetails);

        verify(gastoService, times(1)).deleteGasto(gastoEntity);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
