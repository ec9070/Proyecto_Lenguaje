package Clases;

public class Sucursal {
    private int id_sucursal;
    private String nombre_sucursal;
    private String direccion;
    private String telefono;
    private int cant_empleados;
    private int id_provincia;

    public Sucursal() {
        this.id_sucursal=0;
        this.nombre_sucursal="";
        this.direccion="";
        this.telefono="";
        this.cant_empleados=0;
        this.id_provincia=0;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

    public String getNombre_sucursal() {
        return nombre_sucursal;
    }

    public void setNombre_sucursal(String nombre_sucursal) {
        this.nombre_sucursal = nombre_sucursal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getCant_empleados() {
        return cant_empleados;
    }

    public void setCant_empleados(int cant_empleados) {
        this.cant_empleados = cant_empleados;
    }

    public int getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(int id_provincia) {
        this.id_provincia = id_provincia;
    }
    
}
