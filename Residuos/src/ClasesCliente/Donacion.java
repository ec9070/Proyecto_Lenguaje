package ClasesCliente;

import java.time.LocalDate;

public class Donacion extends Usuario {

    protected String numero_tarjeta;
    protected LocalDate fecha;
    protected int codigo;
    protected double monto;

    public Donacion() {
        super();
        this.numero_tarjeta = "";
        this.codigo = 0;
        this.monto = 0;
    }

    public String getNumero_tarjeta() {
        return numero_tarjeta;
    }

    public void setNumero_tarjeta(String numero_tarjeta) {
        this.numero_tarjeta = numero_tarjeta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

}
