package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.persistencia.PersistenciaUtil;
import com.refugio.servicio.AutenticacionService;
import javax.swing.*;
import java.awt.*;

/** Ventana de inicio de sesion. Máximo 3 intentos antes de bloquear. */
public class LoginFrame extends JFrame {
    private AutenticacionService autenticacionService = new AutenticacionService();
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JCheckBox chkMostrar;
    private char echoOriginal;
    private JButton btnIngresar;
    private JLabel lblMensaje;

    public LoginFrame() {
        setTitle("Centro de Rescate Animal - Inicio de Sesión");
        setSize(460, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Estilos.FONDO);

        JPanel encabezado = Estilos.crearEncabezado(
                "Centro de Rescate Animal",
                "Gestión de Refugio y Adopciones",
                new IconoSimple(IconoSimple.Tipo.PATA, 46, Color.WHITE));
        add(encabezado, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Estilos.FONDO);
        formulario.setBorder(BorderFactory.createEmptyBorder(30, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lblIconoCandado = new JLabel(new IconoSimple(IconoSimple.Tipo.CANDADO, 40, Estilos.PRIMARIO));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        formulario.add(lblIconoCandado, gbc);

        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(Estilos.FUENTE_ETIQUETA);
        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(lblUsuario, gbc);
        txtUsuario = new JTextField(15);
        txtUsuario.setFont(Estilos.FUENTE_NORMAL);
        gbc.gridx = 1;
        formulario.add(txtUsuario, gbc);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(Estilos.FUENTE_ETIQUETA);
        gbc.gridx = 0; gbc.gridy = 2;
        formulario.add(lblContrasena, gbc);
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(Estilos.FUENTE_NORMAL);
        gbc.gridx = 1;
        formulario.add(txtContrasena, gbc);
        echoOriginal = txtContrasena.getEchoChar(); 
        chkMostrar = new JCheckBox("Mostrar contraseña"); 
        chkMostrar.setOpaque(false); 
        chkMostrar.addActionListener(e -> { 
            if (chkMostrar.isSelected()) { 
                txtContrasena.setEchoChar((char) 0); } 
            else { 
                txtContrasena.setEchoChar(echoOriginal); 
            } 
        }); 
        gbc.gridx = 1; gbc.gridy = 3; formulario.add(chkMostrar, gbc); 

        btnIngresar = new JButton("Ingresar");
        Estilos.estilizarBotonPrimario(btnIngresar);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(16, 8, 8, 8);
        formulario.add(btnIngresar, gbc);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        lblMensaje.setForeground(Estilos.ROJO_ELIMINAR);
        lblMensaje.setFont(Estilos.FUENTE_NORMAL);
        gbc.gridy = 5; gbc.insets = new Insets(4, 8, 4, 8);
        formulario.add(lblMensaje, gbc);

        JLabel lblAyuda = new JLabel(
                "<html><center><i>admin1 / Refugio2026 (ADMIN)<br>auxiliar1 / Auxiliar2026 (AUXILIAR)</i></center></html>",
                SwingConstants.CENTER);
        lblAyuda.setForeground(Color.GRAY);
        lblAyuda.setFont(Estilos.FUENTE_SUBTITULO);
        gbc.gridy = 6;
        formulario.add(lblAyuda, gbc);

        add(formulario, BorderLayout.CENTER);

        btnIngresar.addActionListener(e -> intentarLogin());
        getRootPane().setDefaultButton(btnIngresar);
        setVisible(true);
    }

    private void intentarLogin() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        String resultado = autenticacionService.iniciarSesion(usuario, contrasena);

        if (resultado.equals("OK")) {
            ContextoApp.usuarioActual = usuario;
            ContextoApp.rolActual = autenticacionService.getRolActual();
            ContextoApp.bitacora.registrarAccion(usuario, "AUTENTICACION", "LOGIN_OK", "Inicio de sesión correcto");
            PersistenciaUtil.cargarTodo();
            dispose();
            new MainFrame();
        } else if (resultado.equals("BLOQUEADO")) {
            ContextoApp.bitacora.registrarError(usuario.isEmpty() ? "desconocido" : usuario, "AUTENTICACION",
                    "LOGIN_FALLIDO", "Sesión bloqueada tras 3 intentos");
            lblMensaje.setText("Sesión bloqueada, reinicie la aplicación");
            btnIngresar.setEnabled(false);
        } else {
            ContextoApp.bitacora.registrarError(usuario, "AUTENTICACION", "LOGIN_FALLIDO",
                    "Contraseña incorrecta (intento " + autenticacionService.getIntentosFallidos() + " de 3)");
            lblMensaje.setText("Usuario o contraseña incorrectos. Intentos restantes: " + autenticacionService.getIntentosRestantes());
        }
    }
}
