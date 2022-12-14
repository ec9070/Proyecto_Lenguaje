
package Clases;

public class Puesto {
    private int id_puesto;
    private String nombre_puesto;
    private int min_salario;
    private int max_salario;

    public Puesto() {
        this.id_puesto = 0;
        this.nombre_puesto = "";
        this.min_salario = 0;
        this.max_salario = 0;
    }

    public int getId_puesto() {
        return id_puesto;
    }

    public void setId_puesto(int id_puesto) {
        this.id_puesto = id_puesto;
    }

    public String getNombre_puesto() {
        return nombre_puesto;
    }

    public void setNombre_puesto(String nombre_puesto) {
        this.nombre_puesto = nombre_puesto;
    }

    public int getMin_salario() {
        return min_salario;
    }

    public void setMin_salario(int min_salario) {
        this.min_salario = min_salario;
    }

    public int getMax_salario() {
        return max_salario;
    }

    public void setMax_salario(int max_salario) {
        this.max_salario = max_salario;
    }
    
}
