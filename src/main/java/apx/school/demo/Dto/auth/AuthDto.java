package apx.school.demo.Dto.auth;

import apx.school.demo.Entity.GastoEntity;

import java.util.List;

public class AuthDto {

    private String token;
    private List<GastoEntity> gastos;

    public AuthDto(String token) {
        this.token = token;
        this.gastos = gastos;
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
}
