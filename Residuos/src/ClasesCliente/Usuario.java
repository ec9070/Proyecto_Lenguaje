package ClasesCliente;

import java.util.HashSet;
import java.util.Set;

public class Usuario extends Persona {

    protected String contraseña;
    protected int puntos;
    protected int donaciones;
    protected Set listaZonas;
    protected Set listaCapacitaciones;

    public Usuario() {
        super();
        this.contraseña = "";
        this.puntos = 0;
        this.donaciones = 0;
        this.listaZonas = new HashSet();
        this.listaCapacitaciones=new HashSet();
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(int donaciones) {
        this.donaciones = donaciones;
    }

    public Set getListaZonas() {
        return listaZonas;
    }

    public void setListaZonas(Set listaZonas) {
        this.listaZonas = listaZonas;
    }

    public Set getListaCapacitaciones() {
        return listaCapacitaciones;
    }

    public void setListaCapacitaciones(Set listaCapacitaciones) {
        this.listaCapacitaciones = listaCapacitaciones;
    } 

    public String getPrimer_apellido() {
        return primer_apellido;
    }

    public void setPrimer_apellido(String primer_apellido) {
        this.primer_apellido = primer_apellido;
    }

    public String getSegundo_apellido() {
        return segundo_apellido;
    }

    public void setSegundo_apellido(String segundo_apellido) {
        this.segundo_apellido = segundo_apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
