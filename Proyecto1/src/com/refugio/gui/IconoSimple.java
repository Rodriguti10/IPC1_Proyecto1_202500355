package com.refugio.gui;

import javax.swing.Icon;
import java.awt.*;

/**
 * Iconos vectoriales dibujados por codigo con Graphics2D (sin archivos de imagen
 * externos, sin descargas de internet, sin problemas de derechos de autor).
 * Cada tipo dibuja una figura simple reconocible para identificar cada modulo.
 */
public class IconoSimple implements Icon {

    public enum Tipo { PATA, PERSONA, DOCUMENTO, ALERTA, MAPA, GRAFICA, CANDADO }

    private final Tipo tipo;
    private final int size;
    private final Color color;

    public IconoSimple(Tipo tipo, int size, Color color) {
        this.tipo = tipo;
        this.size = size;
        this.color = color;
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.setColor(color);
        int s = size;

        switch (tipo) {
            case PATA:
                double sd = s;
                int padW = (int) (sd * 0.50);
                int padH = (int) (sd * 0.38);
                int padX = (int) ((sd - padW) / 2);
                int padY = (int) (sd * 0.55);
                g2.fillOval(padX, padY, padW, padH);

                int toeD = (int) (sd * 0.26);
                g2.fillOval((int) (sd * 0.00), (int) (sd * 0.28), toeD, toeD);
                g2.fillOval((int) (sd * 0.24), (int) (sd * 0.02), toeD, toeD);
                g2.fillOval((int) (sd * 0.50), (int) (sd * 0.02), toeD, toeD);
                g2.fillOval((int) (sd * 0.74), (int) (sd * 0.28), toeD, toeD);
                break;

            case PERSONA:
                g2.fillOval(s * 3 / 8, 0, s / 4, s / 4);
                g2.fillArc(s / 6, s * 2 / 5, s * 2 / 3, s * 3 / 5, 0, 180);
                break;

            case DOCUMENTO:
                g2.drawRoundRect(s / 6, 0, s * 2 / 3, s - 2, 4, 4);
                g2.setStroke(new BasicStroke(2f));
                for (int i = 1; i <= 3; i++) {
                    int yy = s * i / 4;
                    g2.drawLine(s / 6 + 4, yy, s * 5 / 6 - 4, yy);
                }
                break;

            case ALERTA:
                Polygon tri = new Polygon();
                tri.addPoint(s / 2, 0);
                tri.addPoint(0, s - 2);
                tri.addPoint(s - 2, s - 2);
                g2.fillPolygon(tri);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(s / 2, s * 2 / 5, s / 2, s * 2 / 3);
                g2.fillOval(s / 2 - 2, s * 3 / 4, 4, 4);
                break;

            case MAPA:
                g2.fillArc(s / 6, 0, s * 2 / 3, s * 2 / 3, 0, 360);
                Polygon pin = new Polygon();
                pin.addPoint(s * 2 / 5, s / 2);
                pin.addPoint(s * 3 / 5, s / 2);
                pin.addPoint(s / 2, s - 2);
                g2.fillPolygon(pin);
                g2.setColor(Color.WHITE);
                g2.fillOval(s * 3 / 8, s / 8, s / 4, s / 4);
                break;

            case GRAFICA:
                g2.fillRect(s / 8, s / 2, s / 6, s / 2 - 2);
                g2.fillRect(s * 3 / 8, s / 4, s / 6, s * 3 / 4 - 2);
                g2.fillRect(s * 6 / 8 - 2, 0, s / 6, s - 2);
                break;

            case CANDADO:
                g2.setStroke(new BasicStroke(3f));
                g2.drawArc(s / 4, 0, s / 2, s / 2, 0, 180);
                g2.fillRoundRect(s / 8, s * 2 / 5, s * 3 / 4, s * 3 / 5, 6, 6);
                break;
        }
        g2.dispose();
    }
}
