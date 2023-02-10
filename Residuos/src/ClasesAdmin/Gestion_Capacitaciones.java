package ClasesAdmin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class Gestion_Capacitaciones extends Capacitacion {

    private static Connection con = null;

    public Gestion_Capacitaciones() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
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

    public int cantidad_Capacitaciones() {
        int i = 0;
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Capacitaciones");
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
    public boolean existe() {
        conectar();
        boolean existe = false;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Capacitaciones where "
                    + "fecha='" + getFecha() + "' and hora='" + getHora()
                    + "' and direccion='" + getDireccion() + "'");
            while (rs.next()) {
                if ((getFecha().equals(rs.getString(5)))
                        && (getHora().equals(rs.getString(4)))
                        && (getDireccion().equals(rs.getString(3)))) {
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
    public void registrar() {
        Statement sentencia;
        int numero = cantidad_Capacitaciones() + 1;
        try {
            conectar();
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            sentencia.executeUpdate("insert into Capacitaciones(numero,nombre,direccion,hora,fecha,cupo)"
                    + "values(" + numero + ",'" + getNombre() + "','" + getDireccion() + "','" + getHora() + "','" + getFecha() + "'," + getCupo() + ")");
            JOptionPane.showMessageDialog(null, "¡Capacitacion agregada!");
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al insertar!" + ex.getMessage());
        }
    }

    @Override
    public int mostrar() {
        String s = "";
        int numero = 0;
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Capacitaciones");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(2) + ", " + rs.getString(5) + ", " + rs.getString(4) + "\n";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        numero = Integer.parseInt(JOptionPane.showInputDialog(null, s + "\nDigite el numero de la capacitacion que desea Registrar: ", "Registrar Capacitacion", JOptionPane.QUESTION_MESSAGE));
        return numero;
    }

    @Override
    public void editar(int numero) {
        Statement sentencia;
        try {
            conectar();
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sentencia.executeUpdate("update Capacitaciones"
                    + " set nombre='" + getNombre() + "',direccion='" + getDireccion() + "',hora='" + getHora() + "',fecha='" + getFecha() + "',cupo=" + getCupo()
                    + " where numero=" + numero);
            JOptionPane.showMessageDialog(null, "Cambios guardados correctamente");
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }

    }

    @Override
    public String mostrar_Capacitacion() {
        String s = "";
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Capacitaciones where cupo>0");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(2) + ", " + rs.getString(5) + ", " + rs.getString(4) + "\n";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return s;
    }

    @Override
    public boolean existe_Capacitacion(int numero) {
        conectar();
        boolean existe = false;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Capacitaciones where numero=" + numero + " and cupo>0");
            while (rs.next()) {
                if (numero == rs.getInt(1)) {
                    existe = true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "¡Error al buscar!" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        desconectar();
        return existe;
    }

    @Override
    public String apuntar_Cliente(String correo, int numero) {
        Statement sentencia;
        String Capacitacion = "";
        int cupo = 0;
        try {
            conectar();
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = sentencia.executeQuery("select * from Capacitaciones where numero=" + numero + " and cupo>0");
            while (rs.next()) {
                setNombre(rs.getString(2));
                setDireccion(rs.getString(3));
                setHora(rs.getString(4));
                setFecha(rs.getString(5));
                setCupo(rs.getInt(6));
            }
            Capacitacion = getNombre() + ", " + getFecha() + ", " + getHora();
            cupo = getCupo() - 1;
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sentencia.executeUpdate("update Capacitaciones" + " set cupo=" + cupo + " where numero=" + numero);
            sentencia.executeUpdate("insert into Users_Capacitaciones(correo,nombre,fecha,hora)"
                    + "values('" + correo + "','" + getNombre() + "','" + getFecha() + "','" + getHora() + "')");
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        return Capacitacion;
    }

    @Override
    public void consulta(int numero) {
        try {
            conectar();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Capacitaciones where numero=" + numero + " and cupo>0");
            while (rs.next()) {
                setNombre(rs.getString(2));
                setDireccion(rs.getString(3));
                setHora(rs.getString(4));
                setFecha(rs.getString(5));
                setCupo(rs.getInt(6));
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

}
