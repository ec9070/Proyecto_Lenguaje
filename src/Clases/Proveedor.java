
package Clases;

public class Proveedor {
    private int id_proveedor;
    private String nom_proveedor;
    private String telefono;
    private String correo;

    public Proveedor() {
        this.id_proveedor=0;
        this.nom_proveedor="";
        this.telefono="";
        this.correo="";
    }

    public int getId_proveedor() {
        return id_proveedor;
    }

    public void setId_proveedor(int id_proveedor) {
        this.id_proveedor = id_proveedor;
    }

    public String getNom_proveedor() {
        return nom_proveedor;
    }

    public void setNom_proveedor(String nom_proveedor) {
        this.nom_proveedor = nom_proveedor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
