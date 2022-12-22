
package Clases;

public class Pedido {
    private int num_pedido;
    private String fecha;
    private String codigo;
    private int unidades;

    public Pedido() {
        this.num_pedido=0;
        this.fecha="";
        this.codigo="";
        this.unidades=0;
    }

    public int getNum_pedido() {
        return num_pedido;
    }

    public void setNum_pedido(int num_pedido) {
        this.num_pedido = num_pedido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }
}
