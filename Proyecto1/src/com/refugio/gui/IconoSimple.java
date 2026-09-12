package com.refugio.gui;

import javax.swing.Icon;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Iconos de la interfaz, cargados desde imagenes PNG reales incluidas en el
 * proyecto (carpeta recursos/). Las imagenes vienen de Material Symbols de
 * Google (licencia Apache 2.0, uso y redistribucion libres, sin necesidad de
 * atribucion). Se generaron previamente en dos colores (blanco y azul) para
 * no depender de ninguna libreria de renderizado de SVG en tiempo de ejecucion:
 * la aplicacion solo usa javax.imageio (incluido en el JDK) para leer PNG.
 */
public class IconoSimple implements Icon {

    public enum Tipo { PATA, PERSONA, DOCUMENTO, ALERTA, MAPA, GRAFICA, CANDADO }

    private final int size;
    private Image imagenEscalada;

    public IconoSimple(Tipo tipo, int size, Color color) {
        this.size = size;
        cargarImagen(tipo, color);
    }

    private String nombreBase(Tipo tipo) {
        switch (tipo) {
            case PATA: return "pata";
            case PERSONA: return "persona";
            case DOCUMENTO: return "documento";
            case ALERTA: return "alerta";
            case MAPA: return "mapa";
            case GRAFICA: return "grafica";
            case CANDADO: return "candado";
            default: return "pata";
        }
    }

    private void cargarImagen(Tipo tipo, Color color) {
        boolean esBlanco = color != null && color.equals(Color.WHITE);
        String archivo = "recursos/" + nombreBase(tipo) + (esBlanco ? "_blanco" : "_azul") + ".png";
        try (InputStream in = getClass().getResourceAsStream(archivo)) {
            if (in != null) {
                BufferedImage original = ImageIO.read(in);
                imagenEscalada = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            System.err.println("No se pudo cargar el icono: " + archivo);
            imagenEscalada = null;
        }
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (imagenEscalada != null) {
            g.drawImage(imagenEscalada, x, y, null);
        }
    }

    /** Devuelve la imagen ya cargada (util para setIconImage() de una ventana). */
    public Image getImage() {
        return imagenEscalada;
    }
}
