package JframesCliente;

import ClasesCliente.Donacion;
import ClasesCliente.Usuario;
import java.awt.Image;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Donaciones extends javax.swing.JFrame {

    private Usuario user = new Usuario();
    private Set listaUsuarios = new HashSet();
    private static Connection con = null;

    public Donaciones() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Donacion");
        Image icono = new ImageIcon(getClass().getResource("/iconosCliente/piggy.png")).getImage();
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

    public boolean crear() {
        Donacion d = new Donacion();
        Date fecha = new Date();
        ZoneId timeZone = ZoneId.systemDefault();
        LocalDate getLocalDate = fecha.toInstant().atZone(timeZone).toLocalDate();
        if ((jTextField4.getText().equals("")) || (jTextField2.getText().equals(""))
                || (jTextField3.getText().equals(""))) {
            JOptionPane.showMessageDialog(null, "¡Todos los campos deben estar llenos!");
            return false;
        } else if (getLocalDate.getYear() <= jYearChooser1.getYear()) {
            if ((jYearChooser1.getYear() == getLocalDate.getYear()) && (getLocalDate.getMonthValue() >= (jMonthChooser2.getMonth() + 1))) {
                JOptionPane.showMessageDialog(null, "¡Su tarjeta esta vencida!");
                return false;
            } else {
                d.setNumero_tarjeta(jTextField4.getText());
                d.setFecha(getLocalDate);
                d.setCodigo(Integer.parseInt(jTextField2.getText()));
                d.setMonto(Double.parseDouble(jTextField3.getText()));
                enviar(d);
                JOptionPane.showMessageDialog(null, "Donacion realizada con exito");
                return true;
            }
        } else {
            JOptionPane.showMessageDialog(null, "¡Su tarjeta esta vencida!");
        }
        return false;
    }

    public void enviar(Donacion d) {
        Statement sentencia;
        int numeroDonacion = cantidad_donaciones() + 1;
        try {
            conectar();
            sentencia = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sentencia.executeUpdate("insert into Donaciones(numeroDonacion,correo,numeroTarjeta,codigo,monto,fecha)"
                    + "values(" + numeroDonacion + ",'" + user.getCorreo() + "','" + d.getNumero_tarjeta() + "'," + d.getCodigo() + "," + d.getMonto() + ",'" + d.getFecha().toString() + "')");
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
    }

    public int cantidad_donaciones() {
        int i = 0;
        try {
            conectar();
            Statement sentencia = con.createStatement();
            ResultSet rs = sentencia.executeQuery("select * from Donaciones");
            while (rs.next()) {
                i++;
            }
            desconectar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al consultar!" + ex.getMessage());
        }
        return i;
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

    public void aumentar() {
        sacar();
        getUser().setDonaciones(getUser().getDonaciones() + 1);
        getUser().setPuntos(getUser().getPuntos() + 3);
        listaUsuarios.add(getUser());
        guardar();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        label1 = new java.awt.Label();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jMonthChooser2 = new com.toedter.calendar.JMonthChooser();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jLabel2.setBackground(new java.awt.Color(1, 65, 92));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Correo:");

        jTextField1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        label1.setText("label1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(1, 65, 92));

        jLabel3.setBackground(new java.awt.Color(1, 65, 92));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Monto a donar:");

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(1, 65, 92));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Fecha de vencimiento:");

        jYearChooser1.setDayChooser(null);

        jLabel5.setBackground(new java.awt.Color(1, 65, 92));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Numero de tarjeta:");

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setBackground(new java.awt.Color(1, 65, 92));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Código de seguridad:");

        jTextField4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextField4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton2.setBackground(new java.awt.Color(110, 111, 115));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Donar");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(110, 111, 115));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cancelar");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jMonthChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jYearChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMonthChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jYearChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (crear()) {
            aumentar();
            this.dispose();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Donaciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private com.toedter.calendar.JMonthChooser jMonthChooser2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    private java.awt.Label label1;
    // End of variables declaration//GEN-END:variables
}
