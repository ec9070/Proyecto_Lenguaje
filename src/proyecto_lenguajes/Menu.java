package proyecto_lenguajes;

import Clases.Factura;
import proyecto_lenguajes.Nuevos.*;
import proyecto_lenguajes.Ediciones.*;
import proyecto_lenguajes.Busquedas.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

public class Menu extends javax.swing.JFrame {

    private static Connection con = null;

    public Menu() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Menu principal");
        setResizable(false);
    }

    public void conectar() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@localhost:1521:orcl";
            String user = "tienda";
            String password = "tienda01";
            try {
                con = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean existe(String id) {
        int aux = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_clientes.repetido('" + id + "') from dual");
            while (rs.next()) {
                aux = rs.getInt(1);
            }
            con.close();
            if (aux == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void eliminar() {
        String id;
        id = JOptionPane.showInputDialog(null, "Digite el id del cliente que desea eliminar: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!existe(id)) {
                JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
            } else {
                try {
                    conectar();
                    Statement st = con.createStatement();
                    st.executeQuery("delete from Cliente where id_cliente='" + id + "'");
                    st.executeQuery("commit");
                    con.close();
                    JOptionPane.showMessageDialog(null, "¡Cliente eliminado!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "¡Hay referencia la cliente en otra tablas eliminelas primero!");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public void consultar_credito() {
        String id;
        String s;
        id = JOptionPane.showInputDialog(null, "Digite el id del cliente para consultar el credito: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!existe(id)) {
                JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
            } else {
                s = buscar(id);
                if (!(s.equals(""))) {
                    JOptionPane.showMessageDialog(null, s);
                } else {
                    JOptionPane.showMessageDialog(null, "El cliente no tiene credito");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public void eliminar_credito() {
        String id;
        int comprobante;
        id = JOptionPane.showInputDialog(null, "Digite la cedula del cliente para eliminar el credito: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!existe(id)) {
                JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
            } else {
                comprobante = comprobar(id);
                if (comprobante != 0) {
                    try {
                        conectar();
                        Statement st = con.createStatement();
                        st.executeQuery("delete from credito where id_cliente='" + id + "'");
                        st.executeQuery("commit");
                        con.close();
                        JOptionPane.showMessageDialog(null, "¡Credito eliminado!");
                    } catch (SQLException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No tiene credito");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public int comprobar(String id) {
        int comprobante = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id_credito from Cliente where id_cliente='" + id + "'");
            while (rs.next()) {
                comprobante = rs.getInt(1);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comprobante;
    }

    public String buscar(String id) {
        String s = "";
        try {
            conectar();
            CallableStatement cst = con.prepareCall("{call paquete_credito.buscar_credito(?,?,?,?,?)}");
            cst.setString(1, id);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.registerOutParameter(3, java.sql.Types.INTEGER);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.registerOutParameter(5, java.sql.Types.VARCHAR);
            cst.execute();
            String nombre = cst.getString(4);
            String apellidos = cst.getString(5);
            int id_credito = cst.getInt(2);
            int limite = cst.getInt(3);
            s = s + "Credito de " + nombre + " " + apellidos + "\nId_credito: " + id_credito + "\nLimite: " + limite;
            con.close();
        } catch (SQLException ex) {
        }
        return s;
    }

    public void eliminar_puesto() {
        String s = puestos();
        int id;
        try {
            if (!(s.equals(""))) {
                id = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de puesto que desea eliminar: "));
                try {
                    conectar();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select id_puesto from puesto where id_puesto=" + id);
                    rs.next();
                    id = rs.getInt(1);
                    con.close();
                    try {
                        conectar();
                        st = con.createStatement();
                        st.executeQuery("delete from Puesto where id_puesto=" + id);
                        st.executeQuery("commit");
                        con.close();
                        JOptionPane.showMessageDialog(null, "¡Puesto eliminado!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Hay referencias al puesto en otras tablas eliminelas primero");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "El puesto no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay puestos");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public String puestos() {
        String s = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id_puesto,nombre_puesto from puesto");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(2) + "\n";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void eliminar_sucursal() {
        String s = sucursales();
        int id;
        try {
            if (!(s.equals("null"))) {
                id = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de sucursal que desea eliminar: "));
                try {
                    conectar();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select id_sucursal from sucursal where id_sucursal=" + id);
                    rs.next();
                    id = rs.getInt(1);
                    con.close();
                    try {
                        conectar();
                        st = con.createStatement();
                        st.executeQuery("delete from sucursal where id_sucursal=" + id);
                        st.executeQuery("commit");
                        con.close();
                        JOptionPane.showMessageDialog(null, "¡Sucursal eliminado!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Hay referencias en otras tablas eliminelas primero");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "La sucursal no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay sucursales");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public String sucursales() {
        String s = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_sucursal.lista_sucursales from dual");
            rs.next();
            s = s + rs.getString(1);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void eliminar_empleado() {
        String id;
        id = JOptionPane.showInputDialog(null, "Digite la cedula que desea eliminar: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!existe_empleado(id)) {
                JOptionPane.showMessageDialog(null, "¡El empleado no existe!");
            } else {
                try {
                    conectar();
                    Statement st = con.createStatement();
                    st.executeQuery("delete from Empleado where id_empleado='" + id + "'");
                    st.executeQuery("commit");
                    con.close();
                    JOptionPane.showMessageDialog(null, "¡Empleado eliminado!");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Hay pagos resgistrados de este empleado");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public boolean existe_empleado(String id) {
        int aux = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_empleado.repetido_empleado('" + id + "') from dual");
            while (rs.next()) {
                aux = rs.getInt(1);
            }
            con.close();
            if (aux == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void eliminar_pago() {
        String s;
        int opcion;
        String id = JOptionPane.showInputDialog(null, "Digite la cedula del empleado para eliminar el pago: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!existe_empleado(id)) {
                JOptionPane.showMessageDialog(null, "¡El empleado no existe!");
            } else {
                s = pagos(id);
                try {
                    if (!(s.equals(""))) {
                        opcion = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de pago que desea eliminar: "));
                        try {
                            conectar();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery("select num_pago from pago_salario where num_pago=" + opcion);
                            rs.next();
                            opcion = rs.getInt(1);
                            con.close();
                            if (opcion == 0) {
                                JOptionPane.showMessageDialog(null, "El pago no existe");
                            } else {
                                try {
                                    conectar();
                                    st = con.createStatement();
                                    st.executeQuery("delete from pago_salario where num_pago=" + opcion);
                                    st.executeQuery("commit");
                                    con.close();
                                    JOptionPane.showMessageDialog(null, "¡pago eliminado!");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay pagos");
                    }
                } catch (NumberFormatException ex) {
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public String pagos(String id) {
        String s = "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select num_pago,fecha from pago_salario where id_empleado='" + id + "'");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + dateFormat.format(rs.getDate(2)) + "\n";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void eliminar_proveedor() {
        String s = proveedores();
        int opcion;
        try {
            if (!(s.equals(""))) {
                opcion = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de proveedor que desea eliminar: "));
                try {
                    conectar();
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select id_proveedor from proveedor where id_proveedor=" + opcion);
                    rs.next();
                    opcion = rs.getInt(1);
                    con.close();
                    try {
                        conectar();
                        st = con.createStatement();
                        st.executeQuery("delete from Proveedor where id_proveedor=" + opcion);
                        st.executeQuery("commit");
                        con.close();
                        JOptionPane.showMessageDialog(null, "¡Proveedor eliminado!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Hay productos a nombre de este proveedor");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "El proveedor no existe");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay proveedores");
            }
        } catch (NumberFormatException ex) {
        }
    }

    public String proveedores() {
        String s = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select id_proveedor,nom_proveedor from proveedor");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(2) + "\n";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void eliminar_producto() {
        String s = productos();
        String codigo;
        try {
            if (!(s.equals(""))) {
                codigo = JOptionPane.showInputDialog(null, s + "Digite el codigo del producto que desea eliminar: ");
                try {
                    while (!(codigo.length() == 5)) {
                        codigo = JOptionPane.showInputDialog(null, "El codigo debe ser de 5 digitos vuelva a ingresarlo: ");
                    }
                    if (existe_producto(codigo)) {
                        try {
                            conectar();
                            Statement st = con.createStatement();
                            st.executeQuery("delete from Producto where codigo='" + codigo + "'");
                            st.executeQuery("commit");
                            con.close();
                            JOptionPane.showMessageDialog(null, "¡Producto eliminado!");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Hay referencias en otras tablas eliminelas primero");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El producto no existe");
                    }
                } catch (NullPointerException ex) {
                }
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "No hay productos");
        }
    }

    public boolean existe_producto(String id) {
        int aux = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_producto.repetido_producto('" + id + "') from dual");
            while (rs.next()) {
                aux = rs.getInt(1);
            }
            con.close();
            if (aux == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String productos() {
        String s = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_producto.lista_productos from dual");
            rs.next();
            s = rs.getString(1);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void nueva_factura() {
        String id;
        String s = productos();
        String codigo = "";
        int cantidad;
        String nombre;
        int linea = 1;
        int numero;
        Factura f = new Factura();
        try {
            if (!(s.equals(""))) {
                id = JOptionPane.showInputDialog(null, "Digite la cedula del cliente: ");
                try {
                    while (!(id.length() == 9)) {
                        id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
                    }
                    if (!existe(id)) {
                        JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
                    } else {
                        numero = insertar_factura(id);
                        while (!(codigo.equals("salir"))) {
                            try {
                                codigo = JOptionPane.showInputDialog(null, s + "Digite el codigo del producto para agregar a la factura, digite salir para guardar: ");
                                if (existe_producto(codigo)) {
                                    nombre = traer_nombre(codigo);
                                    cantidad = Integer.parseInt(JOptionPane.showInputDialog("Digite la cantidad de :" + nombre + " a llevar"));
                                    if (cantidad > 0) {
                                        if (cantidad < traer_cantidad(codigo)) {
                                            f.setNum_linea(linea);
                                            f.setNum_factura(numero);
                                            f.setCodigo(codigo);
                                            f.setCantidad(cantidad);
                                            f.setTotal(traer_precio(codigo) * cantidad);
                                            insertar_linea(f);
                                            linea++;
                                        } else {
                                            JOptionPane.showMessageDialog(null, "No hay sufucientes existencias");
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0");
                                    }
                                } else {
                                    if (!(codigo.equals("salir"))) {
                                        JOptionPane.showMessageDialog(null, "El producto no existe");
                                    }
                                }
                            } catch (NullPointerException ex) {
                                try {
                                    conectar();
                                    Statement st = con.createStatement();
                                    st.executeQuery("delete from factura where num_factura=" + numero);
                                    con.close();
                                } catch (SQLException ex1) {
                                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(null, "factura guardada");
                    }
                } catch (NullPointerException ex) {
                }
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "No hay productos");
        }
    }

    public void insertar_linea(Factura f) {
        try {
            conectar();
            CallableStatement cst = con.prepareCall("{call paquete_factura.insertar_linea_factura(?,?,?,?,?)}");
            cst.setInt(1, f.getNum_linea());
            cst.setInt(2, f.getNum_factura());
            cst.setString(3, f.getCodigo());
            cst.setInt(4, f.getCantidad());
            cst.setInt(5, f.getTotal());
            cst.execute();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int traer_precio(String codigo) {
        int precio = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select precio from producto where codigo='" + codigo + "'");
            rs.next();
            precio = rs.getInt(1);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return precio;
    }

    public int traer_cantidad(String codigo) {
        int existencias = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select existencias from producto where codigo='" + codigo + "'");
            rs.next();
            existencias = rs.getInt(1);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existencias;
    }

    public int insertar_factura(String id) {
        int numero = 0;
        try {
            conectar();
            CallableStatement cst = con.prepareCall("{call paquete_factura.insertar_factura(?,?)}");
            cst.setString(1, id);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.execute();
            numero = cst.getInt(2);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numero;
    }

    public String traer_nombre(String codigo) {
        String nombre = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_factura.nombre_producto('" + codigo + "') from dual");
            rs.next();
            nombre = rs.getString(1);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombre;
    }

    public void eliminar_pedido() {
        String s = pedidos();
        int id;
        try {
            if (!(s.equals(""))) {
                try {
                    id = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero del pedido que desea consultar: "));
                    if (existe_pedido(id)) {
                        try {
                            conectar();
                            Statement st = con.createStatement();
                            st.executeQuery("delete from Pedidos where num_pedido=" + id);
                            st.executeQuery("commit");
                            con.close();
                            JOptionPane.showMessageDialog(null, "¡Pedido eliminado!");
                        } catch (SQLException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "El pedido no existe");
                    }
                } catch (NumberFormatException ex) {
                }
            } else {
                JOptionPane.showMessageDialog(null, "El pedido no existe");
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "No hay pedidos");
        }
    }

    public boolean existe_pedido(int id) {
        int aux;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_pedido.existe_pedido(" + id + ") from dual");
            rs.next();
            aux = rs.getInt(1);
            con.close();
            if (aux == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String pedidos() {
        String s = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_pedido.lista_pedidos from dual");
            while (rs.next()) {
                s = rs.getString(1);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public void eliminar_mant() {
        String id;
        String s;
        int opcion;
        id = JOptionPane.showInputDialog(null, "Digite la cedula del cliente para ver sus mantenimientos: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!(existe(id))) {
                JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
            } else {
                if (existe_mant(id)) {
                    s = lista_mant(id);
                    try {
                        opcion = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de mantenimiento que desea eliminar: "));
                        try {
                            conectar();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery("select num_mantenimiento from mantenimiento where num_mantenimiento=" + opcion + " and id_cliente='" + id + "'");
                            rs.next();
                            opcion = rs.getInt(1);
                            con.close();
                            if (opcion == 0) {
                                JOptionPane.showMessageDialog(null, "El mantenimiento no existe");
                            } else {
                                try {
                                    conectar();
                                    st = con.createStatement();
                                    st.executeQuery("delete from Mantenimiento where num_mantenimiento=" + opcion);
                                    st.executeQuery("commit");
                                    con.close();
                                    JOptionPane.showMessageDialog(null, "¡Mantenimiento eliminado!");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (NumberFormatException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No hay mantenimientos");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public String lista_mant(String id) {
        String s = "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select num_mantenimiento,fecha_salida from mantenimiento where id_cliente='" + id + "'");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + dateFormat.format(rs.getDate(2)) + "\n";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public boolean existe_mant(String id) {
        int aux = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_mantenimiento.existe_mant('" + id + "') from dual");
            while (rs.next()) {
                aux = rs.getInt(1);
            }
            con.close();
            if (aux == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void buscar_factura() {
        String id;
        String detalles = "";
        String s;
        int opcion;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha_f;
        int total;
        id = JOptionPane.showInputDialog(null, "Digite la cedula del cliente para ver sus facturas: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!(existe(id))) {
                JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
            } else {
                if (existe_fact(id)) {
                    s = lista_fact(id);
                    try {
                        opcion = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de factura que desea consultar: "));
                        try {
                            conectar();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery("select * from factura where num_factura=" + opcion + " and id_cliente='" + id + "'");
                            rs.next();
                            opcion = rs.getInt(1);
                            fecha_f = dateFormat.format(rs.getDate(2));
                            id = rs.getString(3);
                            total = rs.getInt(4);
                            con.close();
                            if (opcion == 0) {
                                JOptionPane.showMessageDialog(null, "Factura no existe");
                            } else {
                                try {
                                    conectar();
                                    st = con.createStatement();
                                    rs = st.executeQuery("select * from detalle_factura where num_factura=" + opcion);
                                    while (rs.next()) {
                                        detalles = detalles + rs.getInt(4) + " " + traer_nombre(rs.getString(3)) + "..... " + rs.getInt(5) + "\n";
                                    }
                                    con.close();
                                } catch (SQLException ex) {
                                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                llenar(opcion, fecha_f, id, detalles, total);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (NumberFormatException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No existen facturas");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public void llenar(int numero, String fecha, String id, String detalles, int total) {
        String nombre = traer_cliente(id);
        String s = "Numero: " + numero + "\nFecha: " + fecha + "\nCedula: " + id + "\nCliente:" + nombre + "\n" + detalles + "Total: " + total;
        JOptionPane.showMessageDialog(null, "Factura\n" + s);
    }

    public String traer_cliente(String id) {
        String nombre = "";
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select nombre||' '||apellidos from cliente where id_cliente='" + id + "'");
            rs.next();
            nombre = rs.getString(1);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombre;
    }

    public String lista_fact(String id) {
        String s = "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select num_factura,fecha,total from factura where id_cliente='" + id + "'");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + dateFormat.format(rs.getDate(2)) + ", " + rs.getInt(3) + "\n";
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    public boolean existe_fact(String id) {
        int aux = 0;
        try {
            conectar();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select paquete_factura.existe_fact('" + id + "') from dual");
            while (rs.next()) {
                aux = rs.getInt(1);
            }
            con.close();
            if (aux == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void eliminar_factura() {
        String id;
        String s;
        int opcion;
        id = JOptionPane.showInputDialog(null, "Digite la cedula del cliente para ver sus facturas: ");
        try {
            while (!(id.length() == 9)) {
                id = JOptionPane.showInputDialog(null, "La cedula debe tener 9 digitos vuelva a ingresarla: ");
            }
            if (!(existe(id))) {
                JOptionPane.showMessageDialog(null, "¡El cliente no existe!");
            } else {
                if (existe_fact(id)) {
                    s = lista_fact(id);
                    try {
                        opcion = Integer.parseInt(JOptionPane.showInputDialog(null, s + "Digite el numero de factura que desea consultar: "));
                        try {
                            conectar();
                            Statement st = con.createStatement();
                            ResultSet rs = st.executeQuery("select num_factura from factura where num_factura=" + opcion + " and id_cliente='" + id + "'");
                            rs.next();
                            opcion = rs.getInt(1);
                            con.close();
                            if (opcion == 0) {
                                JOptionPane.showMessageDialog(null, "Factura no existe");
                            } else {
                                try {
                                    conectar();
                                    st = con.createStatement();
                                    st.executeQuery("delete from detalle_factura where num_factura=" + opcion);
                                    st.executeQuery("delete from factura where num_factura=" + opcion);
                                    con.close();
                                    JOptionPane.showMessageDialog(null, "¡Factura eliminado!");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (NumberFormatException ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No existen facturas");
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu2.setText("Clientes");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem1.setText("Nuevo Cliente");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Editar Cliente");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem4.setText("Consultar Cliente");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem3.setText("Eliminar Cliente");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Credito");

        jMenuItem5.setText("Nuevo Credito");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem21.setText("Editar Credito");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem21);

        jMenuItem6.setText("Consultar Credito");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem11.setText("Eliminar Credito");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Puestos");

        jMenuItem7.setText("Nuevo Puesto");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem9.setText("Editar Puesto");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuItem8.setText("Consultar Puesto");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem15.setText("Eliminar Puesto");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem15);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Sucursales");

        jMenuItem10.setText("Nueva Sucursal");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuItem12.setText("Editar Sucursal");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);

        jMenuItem13.setText("Consultar Sucursal");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        jMenuItem16.setText("Eliminar Sucursal");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem16);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Empleados");

        jMenuItem14.setText("Nuevo Empleado");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem14);

        jMenuItem17.setText("Editar Empleado");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem17);

        jMenuItem18.setText("Consultar Empleado");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem18);

        jMenuItem19.setText("Eliminar Empleado");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem19);

        jMenuBar1.add(jMenu6);

        jMenu7.setText("Pagos");

        jMenuItem20.setText("Nuevo Pago");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem20);

        jMenuItem22.setText("Consultar Pago");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem22);

        jMenuItem23.setText("Eliminar Pago");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem23);

        jMenuBar1.add(jMenu7);

        jMenu8.setText("Proveedores");

        jMenuItem24.setText("Nuevo Proveedor");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem24);

        jMenuItem25.setText("Editar Proveedor");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem25);

        jMenuItem26.setText("Consultar Proveedor");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem26);

        jMenuItem27.setText("Eliminar Proveedor");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem27);

        jMenuBar1.add(jMenu8);

        jMenu9.setText("Productos");

        jMenuItem28.setText("Nuevo Producto");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem28);

        jMenuItem29.setText("Editar Producto");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem29);

        jMenuItem30.setText("Consultar Producto");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem30);

        jMenuItem31.setText("Eliminar Producto");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem31);

        jMenuBar1.add(jMenu9);
        jMenuBar1.add(jMenu10);

        jMenu11.setText("Facturas");

        jMenuItem32.setText("Nueva Factura");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem32);

        jMenuItem36.setText("Consultar Factura");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem36);

        jMenuItem37.setText("Eliminar Factura");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem37);

        jMenuBar1.add(jMenu11);

        jMenu12.setText("Pedidos");

        jMenuItem33.setText("Nuevo Pedido");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem33);

        jMenuItem34.setText("Consultar Pedido");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem34);

        jMenuItem35.setText("Eliminar Pedido");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem35);

        jMenuBar1.add(jMenu12);

        jMenu14.setText("Mantenimiento");

        jMenuItem38.setText("Nuevo Mantenimiento");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem38);

        jMenuItem39.setText("Consultar Mantenimiento");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem39);

        jMenuItem40.setText("Eliminar Mantenimiento");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem40);

        jMenuBar1.add(jMenu14);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 746, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 277, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        NuevoCliente n = new NuevoCliente();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        EditarCliente e = new EditarCliente();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        eliminar();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        BuscarCliente s = new BuscarCliente();
        s.setVisible(true);
        s.pack();
        s.setLocationRelativeTo(null);
        s.buscar();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        NuevoCredito n = new NuevoCredito();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        consultar_credito();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        NuevoPuesto p = new NuevoPuesto();
        p.setVisible(true);
        p.pack();
        p.setLocationRelativeTo(null);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        BuscarPuesto b = new BuscarPuesto();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        EditarPuesto e = new EditarPuesto();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        NuevaSucursal n = new NuevaSucursal();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
        n.provincias();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        eliminar_credito();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        EditarSucursal e = new EditarSucursal();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        BuscarSucursal b = new BuscarSucursal();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        NuevoEmpleado n = new NuevoEmpleado();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
        n.llenado();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        eliminar_puesto();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        eliminar_sucursal();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        EditarEmpleado e = new EditarEmpleado();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        BuscarEmpleado e = new BuscarEmpleado();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.buscar();
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        eliminar_empleado();
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        NuevoPago n = new NuevoPago();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        EditarCredito e = new EditarCredito();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        BuscarPago b = new BuscarPago();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        eliminar_pago();
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        NuevoProveedor n = new NuevoProveedor();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        EditarProveedor e = new EditarProveedor();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        BuscarProveedor b = new BuscarProveedor();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        eliminar_proveedor();
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        NuevoProducto n = new NuevoProducto();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
        n.llenado();
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        EditarProducto e = new EditarProducto();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.datos();
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        BuscarProducto b = new BuscarProducto();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        eliminar_producto();
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        nueva_factura();
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        BuscarPedido b = new BuscarPedido();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        NuevoPedido n = new NuevoPedido();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
        n.llenar();
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        eliminar_pedido();
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        NuevoMantenimiento n = new NuevoMantenimiento();
        n.setVisible(true);
        n.pack();
        n.setLocationRelativeTo(null);
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        BuscarMantenimiento b = new BuscarMantenimiento();
        b.setVisible(true);
        b.pack();
        b.setLocationRelativeTo(null);
        b.buscar();
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        eliminar_mant();
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        buscar_factura();
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        eliminar_factura();
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    // End of variables declaration//GEN-END:variables
}
