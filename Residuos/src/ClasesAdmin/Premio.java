package ClasesAdmin;

public interface Premio {

    public void setNumero(int numero);

    public int getNumero();

    public void setNombre(String nombre);

    public String getNombre();

    public void setPuntos(int puntos);

    public int getPuntos();
    
    public boolean existe(String nombre);
    
    public int cantidad();
    
    public void insertar();
    
    public String mostrar();
    
    public boolean existe_premio(int numero);
    
    public void consulta(int numero);
    
    public void editar(int numero);
    
    public String mostrar_premios();
    
    public boolean premio(int numero,int puntos);
}
