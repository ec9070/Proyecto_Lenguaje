package ClasesAdmin;

import java.util.HashSet;
import java.util.Set;

public class Rutina implements Zona {

    private int numeroZona;
    private String nombreZona;
    private String descripcion;
    private String direccion;
    private int cantidad_maxima;
    private Set listaUsuarios;

    public Rutina() {
        this.numeroZona = 0;
        this.nombreZona = "";
        this.descripcion = "";
        this.direccion = "";
        this.cantidad_maxima = 0;
        this.listaUsuarios = new HashSet();
    }

    @Override
    public void setNumeroZona(int numeroZona) {
        this.numeroZona = numeroZona;
    }

    @Override
    public int getNumeroZona() {
        return numeroZona;
    }

    @Override
    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    @Override
    public String getNombreZona() {
        return nombreZona;
    }

    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String getDireccion() {
        return direccion;
    }

    @Override
    public void setCantidad_maxima(int cantidad_maxima) {
        this.cantidad_maxima = cantidad_maxima;
    }

    @Override
    public int getCantidad_maxima() {
        return cantidad_maxima;
    }

    @Override
    public Zona crear(int numeroZona, String nombreZona, String descripcion, String direccion, int cantidad_maxima) {
        Zona z = new Rutina();
        z.setNumeroZona(numeroZona);
        z.setNombreZona(nombreZona);
        z.setDescripcion(descripcion);
        z.setDireccion(direccion);
        z.setCantidad_maxima(cantidad_maxima);
        return z;
    }

}
