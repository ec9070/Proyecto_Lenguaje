package JframesCliente;

import ClasesAdmin.Gestion_Capacitaciones;
import ClasesAdmin.Gestion_Premios;
import ClasesAdmin.Zona;
import ClasesAdmin.Rutina;
import ClasesCliente.Usuario;
import java.awt.Image;
import java.io.*;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.JFrame;

public class Menu extends javax.swing.JFrame {

    Usuario user = new Usuario();
    private Set listaUsuarios = new HashSet();
    private static Connection con = null;
    private static Rutina r = new Rutina();

    public Menu() {
        initComponents();
        setTitle("Menu principal");
        Image icono = new ImageIcon(getClass().getResource("/iconosCliente/Menu.png")).getImage();
        setIconImage(icono);
        leer();
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
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

    public void leer() {
        Usuario user = new Usuario();
        try {
            DataInputStream entrada = new DataInputStream(new FileInputStream("Usuarios.dat"));
            try {
                while (true) {
                    user = new Usuario();
                    user.setPrimer_apellido(entrada.readUTF());
                    user.setSegundo_apellido(entrada.readUTF());
                    user.setNombre(entrada.readUTF());
                    user.setCorreo(entrada.readUTF());
                    user.setContraseña(entrada.readUTF());
                    user.setDireccion(entrada.readUTF());
                    user.setPuntos(entrada.readInt());
                    user.setDonaciones(entrada.readInt());
                    listaUsuarios.add(user);
                }
            } catch (EOFException ex) {
                entrada.close();
            }
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "¡No se encontró el archivo!");
        } catch (IOException eioe) {
            JOptionPane.showMessageDialog(null, "¡Error en el dispositivo!");
        }
    }

    public void guardar() {
        Iterator<Usuario> itU = listaUsuarios.iterator();
        Usuario user = new Usuario();
        try {
            DataOutputStream salida = new DataOutputStream(new FileOutputStream("Usuarios.dat"));
            try {
                while (itU.hasNext()) {
                    user = itU.next();
                    salida.writeUTF(user.getPrimer_apellido());
                    salida.writeUTF(user.getSegundo_apellido());
                    salida.writeUTF(user.getNombre());
                    salida.writeUTF(user.getCorreo());
                    salida.writeUTF(user.getContraseña());
                    salida.writeUTF(user.getDireccion());
                    salida.writeInt(user.getPuntos());
                    salida.writeInt(user.getDonaciones());
                    user = new Usuario();
                }
            } catch (IOException e) {
                salida.close();
            }
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog(null, "¡No se encontró el archivo!");
        } catch (IOException eioe) {
            JOptionPane.showMessageDialog(null, "¡Error en el dispositivo!");
        }
    }

    public void apuntarse_zona() {
        Set listaZonas = user.getListaZonas();
        int numero = 0;
        String zona = "";
        Zona z = new Rutina();
        Statement sentencia;
        String s = mostrarZonas();
        try {
            numero = Integer.parseInt(JOptionPane.showInputDialog(null, s + "\nDigite el numero de la zona en la que desea participar: ", "Registrarse en una zona", JOptionPane.QUESTION_MESSAGE));
        } catch (NumberFormatException ex01) {
        }
        if (!existeZona(numero)) {
            JOptionPane.showMessageDialog(null, "¡El registro buscado no existe!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                conectar();
                sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = sentencia.executeQuery("select * from Zonas where numeroZona=" + numero + " and cantidad_maxima>0");
                while (rs.next()) {
                    z = r.crear(numero, rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                }
                zona = z.getNombreZona();
                numero = z.getCantidad_maxima() - 1;
                if (!existe(zona)) {
                    listaZonas.add(zona);
                    user.setListaZonas(listaZonas);
                    sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    sentencia.executeUpdate("update Zonas" + " set cantidad_maxima=" + numero + " where numeroZona=" + z.getNumeroZona());
                    sentencia.executeUpdate("insert into Usuarios(correo,zona)"
                            + "values('" + user.getCorreo() + "','" + z.getNombreZona() + "')");
                    JOptionPane.showMessageDialog(null, "¡Registrado en la zona correctamente!");
                } else {
                    JOptionPane.showMessageDialog(null, "¡Ya es esta registrado en la zona!");
                }
                desconectar();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
            }
        }
    }

    public boolean existeZona(int numero) {
        conectar();
        boolean existe = false;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select * from Zonas where numeroZona=" + numero + " and cantidad_maxima>0");
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

    public String mostrarZonas() {
        Zona z = new Rutina();
        String s = "";
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Zonas where cantidad_maxima>0");
            while (rs.next()) {
                z = r.crear(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
                s = s + z.getNumeroZona() + ". " + z.getNombreZona() + "\n";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return s;
    }

    public void ver_listaZonas() {
        String s = "";
        Iterator<String> itU;
        itU = user.getListaZonas().iterator();
        while (itU.hasNext()) {
            s = s + itU.next() + "\n";
        }
        JOptionPane.showMessageDialog(null, s);
    }

    public void ver_ListaCapacitaciones() {
        String s = "";
        Iterator<String> itU;
        itU = user.getListaCapacitaciones().iterator();
        while (itU.hasNext()) {
            s = s + itU.next() + "\n";
        }
        JOptionPane.showMessageDialog(null, s);
    }

    public boolean existe(String nombre) {
        Iterator<String> itU = user.getListaZonas().iterator();
        while (itU.hasNext()) {
            if (itU.next().equals(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void llenar_zonas() {
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Usuarios where correo='" + user.getCorreo() + "'");
            while (rs.next()) {
                user.getListaZonas().add(rs.getString(2));
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

    public void consultar_donaciones() {
        int numero = 0;
        String s = mostrarDonaciones(user.getCorreo());
        try {
            numero = Integer.parseInt(JOptionPane.showInputDialog(null, s + "\nDigite el numero de la Donacion que desea consultar: ", "Consulta donacion", JOptionPane.QUESTION_MESSAGE));
        } catch (NumberFormatException ex01) {
        }
        if (!existe_donacion(user.getCorreo(), numero)) {
            JOptionPane.showMessageDialog(null, "¡El registro buscado no existe!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, donacion(numero));
        }
    }

    public String donacion(int numero) {
        String s = "";
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Donaciones where numeroDonacion=" + numero);
            while (rs.next()) {
                s = s + "\nNumero de donacion: " + rs.getInt(1) + "\nMonto: " + rs.getDouble(5) + "\nFecha: " + rs.getString(6);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return s;
    }

    public boolean existe_donacion(String correo, int numero) {
        boolean existe = false;
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Donaciones where correo='" + correo + "' and numeroDonacion=" + numero);
            while (rs.next()) {
                if (numero == rs.getInt(1)) {
                    existe = true;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return existe;
    }

    public String mostrarDonaciones(String correo) {
        String s = "";
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Donaciones where correo='" + correo + "'");
            while (rs.next()) {
                s = s + rs.getInt(1) + ". " + rs.getString(6) + "\n";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        desconectar();
        return s;
    }

    public void Consejo() {
        int cantidad = cantidad_Consejos();
        int numero = 0;
        numero = (int) (Math.random() * cantidad + 1);

        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Consejos where numeroConsejo=" + numero);
            while (rs.next()) {
                if (rs.getInt(1) == numero) {
                    JOptionPane.showMessageDialog(null, rs.getString(2));
                }
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

    public int cantidad_Consejos() {
        int i = 0;
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Consejos");
            while (rs.next()) {
                i++;
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        return i;
    }

    public boolean existe_Capacitacion(String Capacitacion) {
        Iterator<String> itU = user.getListaCapacitaciones().iterator();
        while (itU.hasNext()) {
            if (itU.next().equals(Capacitacion)) {
                return true;
            }
        }
        return false;
    }

    public void llenar_Capacitaciones() {
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Users_Capacitaciones where correo='" + user.getCorreo() + "'");
            while (rs.next()) {
                user.getListaCapacitaciones().add(rs.getString(2) + ", " + rs.getString(3) + ", " + rs.getString(4));
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

    public void apuntarse_Capacitacion() {
        Set lista = user.getListaCapacitaciones();
        int numero = 0;
        String capacitacion = "";
        Gestion_Capacitaciones gc = new Gestion_Capacitaciones();
        try {
            numero = Integer.parseInt(JOptionPane.showInputDialog(null, gc.mostrar_Capacitacion() + "\nDigite el numero de la Capacitacion en la que desea participar: ", "Registrarse en una Capacitacion", JOptionPane.QUESTION_MESSAGE));
        } catch (NumberFormatException ex01) {
        }
        if (!gc.existe_Capacitacion(numero)) {
            JOptionPane.showMessageDialog(null, "¡El registro buscado no existe!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            capacitacion = gc.apuntar_Cliente(user.getCorreo(), numero);
            if (!existe_Capacitacion(capacitacion)) {
                lista.add(capacitacion);
                user.setListaCapacitaciones(lista);
                JOptionPane.showMessageDialog(null, "¡Registrado en la Capacitacion correctamente!");
            } else {
                JOptionPane.showMessageDialog(null, "¡Ya es esta registrado en la Capacitacion!");
            }
        }
    }

    public void reclamar_premio() {
        int numero = 0;
        Gestion_Premios gp = new Gestion_Premios();
        try {
            numero = Integer.parseInt(JOptionPane.showInputDialog(null, gp.mostrar_premios() + "\nDigite el numero del premio a reclamar: ", "Premios", JOptionPane.QUESTION_MESSAGE));
        } catch (NumberFormatException ex01) {
        }
        if (!gp.existe_premio(numero)) {
            JOptionPane.showMessageDialog(null, "¡El registro buscado no existe!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (gp.premio(numero, user.getPuntos())) {
                sacar();
                user.setPuntos(user.getPuntos() - gp.getPuntos());
                listaUsuarios.add(user);
                guardar();
                JOptionPane.showMessageDialog(null, "¡Premio reclamado!, se enviara a su direccion en un dia");
            } else {
                JOptionPane.showMessageDialog(null, "No tiene sifucientes puntos");
            }
        }
    }

    public void sacar() {
        Iterator<Usuario> itU = listaUsuarios.iterator();
        Usuario user = new Usuario();
        while (itU.hasNext()) {
            user = itU.next();
            if (user.getCorreo().equals(getUser().getCorreo())) {
                itU.remove();
            }
            user = new Usuario();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/Fondo.jpg"))); // NOI18N

        jMenu1.setMnemonic('c');
        jMenu1.setText("Gestionar Perfil");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/blue-user-icon.png"))); // NOI18N
        jMenuItem1.setText("Ver Perfil");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/edit.png"))); // NOI18N
        jMenuItem2.setText("Editar Perfil");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Zonas");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/add.png"))); // NOI18N
        jMenuItem4.setText("Registrase en un zona");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/asking.png"))); // NOI18N
        jMenuItem5.setText("Consultar zona");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/eye.png"))); // NOI18N
        jMenuItem6.setText("Ver tus zonas ");
        jMenuItem6.setToolTipText("Ver zonas en las que el cliente esta registrado");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Donaciones");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/coin-add-icon.png"))); // NOI18N
        jMenuItem7.setText("Realizar donacion");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/asking.png"))); // NOI18N
        jMenuItem8.setText("Consultar donacion");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuBar1.add(jMenu2);

        jMenu5.setText("Capacitaciones");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/add.png"))); // NOI18N
        jMenuItem3.setText("Registrarse en una capacitacion");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem3);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/asking.png"))); // NOI18N
        jMenuItem9.setText("Consultar una capacitacion");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IconosCliente/eye.png"))); // NOI18N
        jMenuItem10.setText("Ver tus Capacitaciones");
        jMenuItem10.setToolTipText("capacitaciones en las que el cliente esta registrado");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuBar1.add(jMenu5);

        jMenu4.setText("Ver un consejo");
        jMenu4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jMenu4MouseEntered(evt);
            }
        });
        jMenuBar1.add(jMenu4);

        jMenu6.setText("Reclamar premio");
        jMenu6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu6MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu6);

        jMenu7.setText("Cerrar sesion");
        jMenu7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu7MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Perfil p = new Perfil();
        p.setVisible(true);
        p.pack();
        p.setLocationRelativeTo(null);
        p.ver_perfil(getUser());
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Editar e = new Editar();
        e.setVisible(true);
        e.pack();
        e.setLocationRelativeTo(null);
        e.setUser(getUser());
        e.datos();
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed

    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked

    }//GEN-LAST:event_jMenu2MouseClicked

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        apuntarse_zona();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked

    }//GEN-LAST:event_jMenu3MouseClicked

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Consulta c = new Consulta();
        c.setVisible(true);
        c.pack();
        c.setLocationRelativeTo(null);
        c.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        c.consultar();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        ver_listaZonas();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Donaciones d = new Donaciones();
        d.setVisible(true);
        d.pack();
        d.setLocationRelativeTo(null);
        d.setUser(getUser());
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        consultar_donaciones();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenu4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseClicked
        Consejo();
    }//GEN-LAST:event_jMenu4MouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        apuntarse_Capacitacion();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        ver_ListaCapacitaciones();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        ConsultaCapacitacion cc = new ConsultaCapacitacion();
        cc.setVisible(true);
        cc.pack();
        cc.setLocationRelativeTo(null);
        cc.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cc.consultar();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenu6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu6MouseClicked
        reclamar_premio();
    }//GEN-LAST:event_jMenu6MouseClicked

    private void jMenu7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu7MouseClicked
        Login l = new Login();
        l.setVisible(true);
        l.pack();
        l.setLocationRelativeTo(null);
        this.dispose();
    }//GEN-LAST:event_jMenu7MouseClicked

    private void jMenu4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu4MouseEntered

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    // End of variables declaration//GEN-END:variables
}
