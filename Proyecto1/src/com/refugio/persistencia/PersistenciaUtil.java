package com.refugio.persistencia;

import com.refugio.ContextoApp;
import com.refugio.modelo.*;
import java.io.*;

/**
 * Lee y escribe los archivos .txt (separador "|").
 * No usa ArrayList: para leer un archivo se cuenta el numero de lineas primero,
 * se reserva un arreglo de ese tamano exacto y luego se llena en una segunda pasada.
 */
public class PersistenciaUtil {
    private static final String DIR = "data/";

    private static String[] leerLineas(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return new String[0];
        int conteo = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            while (br.readLine() != null) conteo++;
        } catch (IOException e) {
            return new String[0];
        }
        String[] lineas = new String[conteo];
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            int i = 0;
            while ((linea = br.readLine()) != null) lineas[i++] = linea;
        } catch (IOException e) { }
        return lineas;
    }

    private static BufferedWriter crearWriter(String ruta) throws IOException {
        File f = new File(ruta);
        if (f.getParentFile() != null) f.getParentFile().mkdirs();
        return new BufferedWriter(new FileWriter(f));
    }

    public static void guardarTodo() {
        try {
            try (BufferedWriter bw = crearWriter(DIR + "animales.txt")) {
                for (Animal a : ContextoApp.animalService.obtenerTodos()) { bw.write(a.toLineaArchivo()); bw.newLine(); }
            }
            try (BufferedWriter bw = crearWriter(DIR + "adoptantes.txt")) {
                for (Adoptante a : ContextoApp.adoptanteService.obtenerTodos()) { bw.write(a.toLineaArchivo()); bw.newLine(); }
            }
            try (BufferedWriter bw = crearWriter(DIR + "solicitudes.txt")) {
                for (Solicitud s : ContextoApp.solicitudService.obtenerTodos()) { bw.write(s.toLineaArchivo()); bw.newLine(); }
            }
            try (BufferedWriter bw = crearWriter(DIR + "rescates.txt")) {
                for (Rescate r : ContextoApp.rescateService.obtenerTodos()) { bw.write(r.toLineaArchivo()); bw.newLine(); }
            }
            try (BufferedWriter bw = crearWriter(DIR + "ubicaciones.txt")) {
                String[][] m = ContextoApp.ubicacionService.obtenerMatriz();
                for (int i = 0; i < m.length; i++)
                    for (int j = 0; j < m[i].length; j++)
                        if (m[i][j] != null && !m[i][j].isEmpty()) { bw.write(i + "|" + j + "|" + m[i][j]); bw.newLine(); }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar datos: " + e.getMessage());
        }
    }

    public static void cargarTodo() {
        for (String l : leerLineas(DIR + "animales.txt"))
            if (!l.trim().isEmpty()) ContextoApp.animalService.agregarDesdeArchivo(Animal.desdeLinea(l));
        for (String l : leerLineas(DIR + "adoptantes.txt"))
            if (!l.trim().isEmpty()) ContextoApp.adoptanteService.agregarDesdeArchivo(Adoptante.desdeLinea(l));
        for (String l : leerLineas(DIR + "solicitudes.txt"))
            if (!l.trim().isEmpty()) ContextoApp.solicitudService.agregarDesdeArchivo(Solicitud.desdeLinea(l));
        for (String l : leerLineas(DIR + "rescates.txt"))
            if (!l.trim().isEmpty()) ContextoApp.rescateService.agregarDesdeArchivo(Rescate.desdeLinea(l));
        for (String l : leerLineas(DIR + "ubicaciones.txt")) {
            if (l.trim().isEmpty()) continue;
            String[] p = l.split("\\|");
            ContextoApp.ubicacionService.cargarCelda(Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2]);
        }
    }
}
