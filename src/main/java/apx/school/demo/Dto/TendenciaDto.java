package apx.school.demo.Dto;

public class TendenciaDto {

    private int mes;  // 1 a 12
    private double totalIngresos;
    private double totalGastos;
    private double balance;

    public TendenciaDto(int mes, double totalIngresos, double totalGastos, double balance) {
        this.mes = mes;
        this.totalIngresos = totalIngresos;
        this.totalGastos = totalGastos;
        this.balance = balance;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public double getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(double totalGastos) {
        this.totalGastos = totalGastos;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
