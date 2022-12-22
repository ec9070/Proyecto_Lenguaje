
package Clases;

public class Mantenimiento {
    private int num_mantenimiento;
    private String fecha_ingreso;
    private String descripcion;
    private String id_cliente;
    private int total;
    private String fecha_salida;

    public Mantenimiento() {
        this.num_mantenimiento=0;
        this.fecha_ingreso="";
        this.descripcion="";
        this.id_cliente="";
        this.total=0;
        this.fecha_salida="";
    }

    public int getNum_mantenimiento() {
        return num_mantenimiento;
    }

    public void setNum_mantenimiento(int num_mantenimiento) {
        this.num_mantenimiento = num_mantenimiento;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }
}
