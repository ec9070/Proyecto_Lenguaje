
package Clases;

public class Factura {
   private int num_linea;
   private int num_factura;
   private String codigo;
   private int cantidad;
   private int total;

    public Factura() {
        this.num_linea=0;
        this.num_factura=0;
        this.codigo="";
        this.cantidad=0;
        this.total=0;
    }

    public int getNum_linea() {
        return num_linea;
    }

    public void setNum_linea(int num_linea) {
        this.num_linea = num_linea;
    }

    public int getNum_factura() {
        return num_factura;
    }

    public void setNum_factura(int num_factura) {
        this.num_factura = num_factura;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
