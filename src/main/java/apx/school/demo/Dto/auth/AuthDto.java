package apx.school.demo.Dto.auth;

import apx.school.demo.Entity.GastoEntity;
import apx.school.demo.Entity.IngresoEntity;

import java.util.List;

public class AuthDto {

    private String token;
    private List<GastoEntity> gastos;
    private List<IngresoEntity> ingresos;

    public AuthDto(String token) {
        this.token = token;
        this.gastos = gastos;
        this.ingresos = ingresos;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<GastoEntity> getGastos() {
        return gastos;
    }

    public void setGastos(List<GastoEntity> gastos) {
        this.gastos = gastos;
    }

    public List<IngresoEntity> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<IngresoEntity> ingresos) {
        this.ingresos = ingresos;
    }
}
