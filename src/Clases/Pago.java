
package Clases;

public class Pago {
    private int num_pago;
    private String fecha;
    private String descripcion;
    private String id_empleado;
    private int total;

    public Pago() {
        this.num_pago=0;
        this.fecha="";
        this.descripcion="";
        this.id_empleado="";
        this.total=0;
    }

    public int getNum_pago() {
        return num_pago;
    }

    public void setNum_pago(int num_pago) {
        this.num_pago = num_pago;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(String id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
}
