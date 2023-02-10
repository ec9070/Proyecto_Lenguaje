package ClasesAdmin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Gestion_Premios implements Premio {

    private static Connection con = null;
    private int numero;
    private String nombre;
    private int puntos;

    public Gestion_Premios() {
        this.numero = 0;
        this.nombre = "";
        this.puntos = 0;
    }

    @Override
    public int getNumero() {
        return numero;
    }

    @Override
    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int getPuntos() {
        return puntos;
    }

    @Override
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void conectar() {
        String URL = "jdbc:mysql://localhost:3306/Residuos?useSSL=false";
        String USUARIO = "administrador";
        String PASSWORD = "admin123";
        try {
            con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "¡Error al conectar!" + ex.getMessage());
        }
    }

    public void desconectar() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "¡Error al desconectar!");
        }
    }

    @Override
    public boolean existe(String nombre) {
        conectar();
        boolean existe = false;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Premios where nombre='" + nombre + "'");
            while (rs.next()) {
                if (nombre.equals(rs.getString(2))) {
                    existe = true;
                }
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "¡Error al buscar!" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return existe;
    }

    @Override
    public int cantidad() {
        int i = 0;
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Premios");
            while (rs.next()) {
                i++;
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        return i;
    }

    @Override
    public void insertar() {
        Statement sentencia;
        try {
            conectar();
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            sentencia.executeUpdate("insert into Premios(numero,nombre,puntos)"
                    + " values(" + getNumero() + ",'" + getNombre() + "'," + getPuntos() + ")");
            JOptionPane.showMessageDialog(null, "¡Premio agregado!");
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al insertar!" + ex.getMessage());
        }
    }

    @Override
    public String mostrar() {
        String s = "";
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Premios");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(2) + "\n";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return s;
    }

    @Override
    public boolean existe_premio(int numero) {
        conectar();
        boolean existe = false;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Premios where numero=" + numero);
            while (rs.next()) {
                if (numero == rs.getInt(1)) {
                    existe = true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "¡Error al buscar!" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return existe;
    }

    @Override
    public void consulta(int numero) {
        try {
            conectar();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Premios where numero=" + numero);
            while (rs.next()) {
                setNumero(rs.getInt(1));
                setNombre(rs.getString(2));
                setPuntos(rs.getInt(3));
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

    @Override
    public void editar(int numero) {
        Statement sentencia;
        try {
            conectar();
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sentencia.executeUpdate("update Premios"
                    + " set nombre='" + getNombre() + "',puntos=" + getPuntos()
                    + " where numero=" + numero);
            JOptionPane.showMessageDialog(null, "Cambios guardados correctamente");
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

    @Override
    public String mostrar_premios() {
        String s = "";
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Premios");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(2) + ", Puntos: " + rs.getString(3) + "\n";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return s;
    }

    @Override
    public boolean premio(int numero, int puntos) {
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Premios where numero=" + numero);
            while (rs.next()) {
                if (puntos >= rs.getInt(3)) {
                    setPuntos(rs.getInt(3));
                    desconectar();
                    return true;
                }
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        return false;
    }

}
