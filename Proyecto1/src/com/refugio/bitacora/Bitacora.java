package com.refugio.bitacora;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bitacora de Acciones (exitosas) y de Errores (fallidas/rechazadas).
 * Se guarda en memoria (arreglos) y ademas se persiste linea por linea en archivos de texto.
 */
public class Bitacora {
    private static final String ARCHIVO_ACCIONES = "data/bitacora_acciones.txt";
    private static final String ARCHIVO_ERRORES = "data/bitacora_errores.txt";
    private static final int MAX_ENTRADAS = 500;

    private String[] entradasAcciones = new String[MAX_ENTRADAS];
    private int cantAcciones = 0;
    private String[] entradasErrores = new String[MAX_ENTRADAS];
    private int cantErrores = 0;

    private String fechaHoraActual() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    public void registrarAccion(String usuario, String modulo, String evento, String descripcion) {
        String linea = fechaHoraActual() + "|" + usuario + "|" + modulo + "|" + evento + "|" + descripcion;
        if (cantAcciones < MAX_ENTRADAS) entradasAcciones[cantAcciones++] = linea;
        guardarLinea(ARCHIVO_ACCIONES, linea);
    }

    public void registrarError(String usuario, String modulo, String evento, String motivo) {
        String linea = fechaHoraActual() + "|" + usuario + "|" + modulo + "|" + evento + "|" + motivo;
        if (cantErrores < MAX_ENTRADAS) entradasErrores[cantErrores++] = linea;
        guardarLinea(ARCHIVO_ERRORES, linea);
    }

    private void guardarLinea(String archivo, String linea) {
        try {
            File f = new File(archivo);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("No se pudo escribir bitácora: " + e.getMessage());
        }
    }

    public String[] getEntradasAcciones() {
        String[] r = new String[cantAcciones];
        System.arraycopy(entradasAcciones, 0, r, 0, cantAcciones);
        return r;
    }

    public String[] getEntradasErrores() {
        String[] r = new String[cantErrores];
        System.arraycopy(entradasErrores, 0, r, 0, cantErrores);
        return r;
    }
}
