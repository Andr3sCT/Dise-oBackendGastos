package apx.school.demo.Dto.auth;

import apx.school.demo.Entity.GastoEntity;

import java.util.List;

public class LoginResponseDto {

    private String token;
    private String response;
    private List<GastoEntity> gastos;

    public LoginResponseDto(String token, String response) {
        this.token = token;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
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
