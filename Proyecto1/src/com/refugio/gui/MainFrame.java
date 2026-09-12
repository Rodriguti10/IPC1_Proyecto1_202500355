package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.persistencia.PersistenciaUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Ventana principal con pestanas para cada modulo. */
public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Centro de Rescate Animal - Gestión de Refugio y Adopciones  ["
                + ContextoApp.usuarioActual + " / " + ContextoApp.rolActual + "]");
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIconImage(new IconoSimple(IconoSimple.Tipo.PATA, 32, Estilos.PRIMARIO).getImage());
        setLayout(new BorderLayout());

        JPanel encabezado = Estilos.crearEncabezado(
                "Centro de Rescate Animal",
                "Sesión activa: " + ContextoApp.usuarioActual + " (" + ContextoApp.rolActual + ")",
                new IconoSimple(IconoSimple.Tipo.PATA, 36, Color.WHITE));
        add(encabezado, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Estilos.FUENTE_BOTON);
        int ic = 18;
        tabs.addTab("Animales", new IconoSimple(IconoSimple.Tipo.PATA, ic, Estilos.PRIMARIO), new PanelAnimales());
        tabs.addTab("Adoptantes", new IconoSimple(IconoSimple.Tipo.PERSONA, ic, Estilos.PRIMARIO), new PanelAdoptantes());
        tabs.addTab("Solicitudes", new IconoSimple(IconoSimple.Tipo.DOCUMENTO, ic, Estilos.PRIMARIO), new PanelSolicitudes());
        tabs.addTab("Rescates", new IconoSimple(IconoSimple.Tipo.ALERTA, ic, Estilos.PRIMARIO), new PanelRescates());
        tabs.addTab("Ubicaciones", new IconoSimple(IconoSimple.Tipo.MAPA, ic, Estilos.PRIMARIO), new PanelUbicaciones());
        tabs.addTab("Reportes", new IconoSimple(IconoSimple.Tipo.GRAFICA, ic, Estilos.PRIMARIO), new PanelReportes());
        add(tabs, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int op = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Desea guardar los datos antes de salir?", "Salir",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (op == JOptionPane.YES_OPTION) {
                    PersistenciaUtil.guardarTodo();
                    System.exit(0);
                } else if (op == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });

        setVisible(true);
    }
}
