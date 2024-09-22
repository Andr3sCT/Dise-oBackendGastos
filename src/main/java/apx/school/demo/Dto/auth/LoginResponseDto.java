package apx.school.demo.Dto.auth;

import apx.school.demo.Entity.GastoEntity;

import java.util.List;

public class LoginResponseDto {

    private String token;
    private String response;
    private String gastos;
    private String ingresos;

    public LoginResponseDto(String token, String response, String gastos, String ingresos) {
        this.token = token;
        this.response = response;
        this.gastos = gastos;
        this.ingresos = ingresos;
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


    public String getGastos() {
        return gastos;
    }

    public void setGastos(String gastos) {
        this.gastos = gastos;
    }

    public String getIngresos() {
        return ingresos;
    }

    public void setIngresos(String ingresos) {
        this.ingresos = ingresos;
    }
}
