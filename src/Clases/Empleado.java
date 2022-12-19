package Clases;

public class Empleado {

    private String id_empleado;
    private String nombre;
    private String apellidos;
    private String correo;
    private String fecha_contratado;
    private int id_puesto;
    private int salario;
    private int id_sucursal;

    public Empleado() {
        this.id_empleado = "";
        this.nombre = "";
        this.apellidos = "";
        this.correo = "";
        this.fecha_contratado = "";
        this.id_puesto = 0;
        this.salario = 0;
        this.id_sucursal = 0;
    }

    public String getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(String id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_contratado() {
        return fecha_contratado;
    }

    public void setFecha_contratado(String fecha_contratado) {
        this.fecha_contratado = fecha_contratado;
    }

    public int getId_puesto() {
        return id_puesto;
    }

    public void setId_puesto(int id_puesto) {
        this.id_puesto = id_puesto;
    }

    public int getSalario() {
        return salario;
    }

    public void setSalario(int salario) {
        this.salario = salario;
    }

    public int getId_sucursal() {
        return id_sucursal;
    }

    public void setId_sucursal(int id_sucursal) {
        this.id_sucursal = id_sucursal;
    }

}
