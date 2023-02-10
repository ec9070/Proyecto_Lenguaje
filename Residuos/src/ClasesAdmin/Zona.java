package ClasesAdmin;

public interface Zona {

    public void setNumeroZona(int numeroZona);

    public int getNumeroZona();

    public void setNombreZona(String nombreZona);

    public String getNombreZona();

    public void setDescripcion(String descripcion);

    public String getDescripcion();

    public void setDireccion(String direccion);

    public String getDireccion();

    public void setCantidad_maxima(int cantidad_maxima);

    public int getCantidad_maxima();

    public Zona crear(int numeroZona, String nombreZona, String descripcion, String direccion, int cantidad_maxima);

}
