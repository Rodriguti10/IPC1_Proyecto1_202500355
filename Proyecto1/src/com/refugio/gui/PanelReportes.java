package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.reportes.ReporteHTML;
import javax.swing.*;
import java.awt.*;

public class PanelReportes extends JPanel {
    public PanelReportes() {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO);
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JPanel encabezado = Estilos.crearEncabezado(
                "Reportes del Refugio",
                "Se generan como archivos HTML en la carpeta reportes/",
                new IconoSimple(IconoSimple.Tipo.GRAFICA, 34, Color.WHITE));
        add(encabezado, BorderLayout.NORTH);

        JPanel contenedorBotones = new JPanel(new GridLayout(4, 1, 15, 15));
        contenedorBotones.setBackground(Estilos.FONDO);
        contenedorBotones.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JButton btnAnimales = new JButton("Generar reporte de Animales (HTML)");
        JButton btnAdopciones = new JButton("Generar reporte de Solicitudes / Adopciones (HTML)");
        JButton btnOcupacion = new JButton("Generar reporte de Ocupacion del Refugio (HTML)");
        JButton btnBitacora = new JButton("Generar reporte de Bitacora (HTML)");

        for (JButton b : new JButton[]{btnAnimales, btnAdopciones, btnOcupacion, btnBitacora}) {
            Estilos.estilizarBotonPrimario(b);
            b.setFont(Estilos.FUENTE_BOTON.deriveFont(14f));
            b.setPreferredSize(new Dimension(400, 55));
        }

        btnAnimales.addActionListener(e -> {
            ReporteHTML.generarReporteAnimales(ContextoApp.animalService.listarActivos());
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "REPORTES", "GENERAR", "Reporte de animales generado");
            JOptionPane.showMessageDialog(this, "Reporte generado en carpeta reportes/");
        });
        btnAdopciones.addActionListener(e -> {
            ReporteHTML.generarReporteAdopciones(ContextoApp.solicitudService.obtenerTodos());
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "REPORTES", "GENERAR", "Reporte de adopciones generado");
            JOptionPane.showMessageDialog(this, "Reporte generado en carpeta reportes/");
        });
        btnOcupacion.addActionListener(e -> {
            String[][] matriz = ContextoApp.ubicacionService.obtenerMatriz();
            String[] nombres = new String[matriz.length];
            for (int i = 0; i < nombres.length; i++) nombres[i] = ContextoApp.ubicacionService.nombreZona(i);
            ReporteHTML.generarReporteOcupacion(matriz, nombres);
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "REPORTES", "GENERAR", "Reporte de ocupacion generado");
            JOptionPane.showMessageDialog(this, "Reporte generado en carpeta reportes/");
        });
        btnBitacora.addActionListener(e -> {
            ReporteHTML.generarReporteBitacora(ContextoApp.bitacora.getEntradasAcciones(), ContextoApp.bitacora.getEntradasErrores());
            JOptionPane.showMessageDialog(this, "Reporte generado en carpeta reportes/");
        });

        contenedorBotones.add(btnAnimales);
        contenedorBotones.add(btnAdopciones);
        contenedorBotones.add(btnOcupacion);
        contenedorBotones.add(btnBitacora);

        JPanel envoltura = new JPanel(new FlowLayout(FlowLayout.CENTER));
        envoltura.setBackground(Estilos.FONDO);
        envoltura.add(contenedorBotones);

        add(envoltura, BorderLayout.CENTER);
    }
}
