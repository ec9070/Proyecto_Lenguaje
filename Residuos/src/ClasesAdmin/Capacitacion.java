package ClasesAdmin;



public abstract class Capacitacion {

    protected String nombre;
    protected String direccion;
    protected String hora;
    protected String fecha;
    protected int cupo;

    public Capacitacion() {
        this.nombre = "";
        this.direccion = "";
        this.hora = "";
        this.fecha = "";
        this.cupo = 0;
    }

    public abstract void registrar();

    public abstract boolean existe();

    public abstract int mostrar();

    public abstract void editar(int numero);

    public abstract String mostrar_Capacitacion();

    public abstract boolean existe_Capacitacion(int numero);

    public abstract String apuntar_Cliente(String correo, int numero);

    public abstract void consulta(int numero);
}
