package com.refugio.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Paleta de colores, fuentes y metodos de ayuda para dar una apariencia
 * consistente a toda la aplicacion. Todo se aplica por codigo (nada de
 * editores visuales), usando solo componentes estandar de Swing.
 */
public class Estilos {
    public static final Color PRIMARIO = new Color(0x1F5C99);
    public static final Color PRIMARIO_OSCURO = new Color(0x123A66);
    public static final Color ACENTO = new Color(0xF2994A);
    public static final Color FONDO = new Color(0xF5F7FA);
    public static final Color FONDO_TABLA_ALT = new Color(0xEAF1F8);
    public static final Color TEXTO_CLARO = Color.WHITE;
    public static final Color ROJO_ELIMINAR = new Color(0xC0392B);

    public static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FUENTE_SUBTITULO = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FUENTE_NORMAL = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FUENTE_BOTON = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FUENTE_ETIQUETA = new Font("SansSerif", Font.BOLD, 13);

    /** Aplica el Look and Feel Nimbus (incluido en el JDK) si esta disponible. */
    public static void aplicarNimbus() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("control", FONDO);
                    UIManager.put("nimbusBase", PRIMARIO);
                    UIManager.put("nimbusBlueGrey", new Color(0xD7E3EF));
                    UIManager.put("info", Color.WHITE);
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no esta disponible se usa el look and feel por defecto.
        }
    }

    public static void estilizarBotonPrimario(JButton b) {
        b.setBackground(PRIMARIO);
        b.setForeground(Color.WHITE);
        b.setFont(FUENTE_BOTON);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /** Le da estilo a todos los JButton hijos de un panel de botones. */
    public static void estilizarPanelBotones(JPanel panelBotones) {
        panelBotones.setOpaque(false);
        for (Component c : panelBotones.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                String texto = b.getText() == null ? "" : b.getText().toLowerCase();
                estilizarBotonPrimario(b);
                if (texto.contains("eliminar")) {
                    b.setBackground(ROJO_ELIMINAR);
                } else if (texto.contains("aprobar") || texto.contains("atender") || texto.contains("asignar")) {
                    b.setBackground(new Color(0x2E8B57));
                } else if (texto.contains("rechazar") || texto.contains("liberar")) {
                    b.setBackground(ACENTO);
                }
            }
        }
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(26);
        tabla.setFont(FUENTE_NORMAL);
        tabla.setGridColor(new Color(0xDDE3EA));
        tabla.setSelectionBackground(PRIMARIO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);

        // Nimbus ignora setBackground/setForeground normales en el encabezado de tabla,
        // por eso se fuerza con un renderer propio (un JLabel pintado a mano).
        javax.swing.table.TableCellRenderer renderEncabezado = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                lbl.setOpaque(true);
                lbl.setBackground(PRIMARIO);
                lbl.setForeground(Color.WHITE);
                lbl.setFont(FUENTE_BOTON);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
                return lbl;
            }
        };
        tabla.getTableHeader().setDefaultRenderer(renderEncabezado);
        tabla.getTableHeader().setPreferredSize(new Dimension(100, 32));
        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? Color.WHITE : FONDO_TABLA_ALT);
                return c;
            }
        });
    }

    /** Crea una franja superior de color con titulo, subtitulo e icono. Se usa en Login y en cada panel. */
    public static JPanel crearEncabezado(String titulo, String subtitulo, Icon icono) {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setBackground(PRIMARIO);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel lblIcono = new JLabel(icono);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);
        textos.add(lblTitulo);
        if (subtitulo != null) {
            JLabel lblSub = new JLabel(subtitulo);
            lblSub.setFont(FUENTE_SUBTITULO);
            lblSub.setForeground(new Color(0xD7E3EF));
            textos.add(lblSub);
        }

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izquierda.setOpaque(false);
        izquierda.add(lblIcono);
        izquierda.add(textos);

        panel.add(izquierda, BorderLayout.WEST);
        return panel;
    }
}
