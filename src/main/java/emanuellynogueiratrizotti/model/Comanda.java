package emanuellynogueiratrizotti.model;
import java.sql.Timestamp;

public class Comanda {
    private int idComanda;
    private String status;
    private Timestamp data;
    private int idMesa;
    private int idFuncionarioCaixa;
    private String formaPgto;
    private int idFuncionarioGarcom;
    private double gorjeta;

    public int getIdComanda() {
        return idComanda;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getData() {
        return data;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public int getIdFuncionarioCaixa() {
        return idFuncionarioCaixa;
    }

    public String getFormaPgto() {
        return formaPgto;
    }

    public int getIdFuncionarioGarcom() {
        return idFuncionarioGarcom;
    }

    public double getGorjeta() {
        return gorjeta;
    }

    public void setIdComanda(int idComanda) {
        this.idComanda = idComanda;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public void setIdFuncionarioCaixa(int idFuncionarioCaixa) {
        this.idFuncionarioCaixa = idFuncionarioCaixa;
    }

    public void setFormaPgto(String formaPgto) {
        this.formaPgto = formaPgto;
    }

    public void setIdFuncionarioGarcom(int idFuncionarioGarcom) {
        this.idFuncionarioGarcom = idFuncionarioGarcom;
    }

    public void setGorjeta(double gorjeta) {
        this.gorjeta = gorjeta;
    }
    
}