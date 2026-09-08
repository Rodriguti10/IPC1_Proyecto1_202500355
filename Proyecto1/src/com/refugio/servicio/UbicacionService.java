package com.refugio.servicio;

/**
 * Panel de ubicaciones del refugio, representado con una MATRIZ.
 * Fila 0 = Zona Perros, Fila 1 = Zona Gatos.
 * Cada fila tiene COLUMNAS espacios; cada espacio guarda el codigo del animal o "" si esta libre.
 * Dimensiones documentadas: 2 filas x 5 columnas => capacidad total 10 animales (5 por zona).
 */
public class UbicacionService {
    public static final int FILAS = 2;
    public static final int COLUMNAS = 5;
    private String[][] matriz = new String[FILAS][COLUMNAS];

    public UbicacionService() {
        for (int i = 0; i < FILAS; i++)
            for (int j = 0; j < COLUMNAS; j++)
                matriz[i][j] = "";
    }

    public String nombreZona(int fila) {
        if (fila == 0) return "Zona Perros";
        if (fila == 1) return "Zona Gatos";
        return "Zona " + fila;
    }

    public boolean asignar(int fila, int columna, String codigoAnimal) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) return false;
        if (!matriz[fila][columna].isEmpty()) return false;
        matriz[fila][columna] = codigoAnimal;
        return true;
    }

    public boolean liberar(int fila, int columna) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) return false;
        matriz[fila][columna] = "";
        return true;
    }

    /** Libera automaticamente la celda ocupada por un animal (usado al hacer baja logica). */
    public boolean liberarPorCodigo(String codigoAnimal) {
        for (int i = 0; i < FILAS; i++)
            for (int j = 0; j < COLUMNAS; j++)
                if (matriz[i][j].equals(codigoAnimal)) { matriz[i][j] = ""; return true; }
        return false;
    }

    public String obtener(int fila, int columna) {
        if (fila < 0 || fila >= FILAS || columna < 0 || columna >= COLUMNAS) return "";
        return matriz[fila][columna];
    }

    public int espaciosDisponiblesEnZona(int fila) {
        int c = 0;
        for (int j = 0; j < COLUMNAS; j++) if (matriz[fila][j].isEmpty()) c++;
        return c;
    }

    public String[][] obtenerMatriz() { return matriz; }

    public void cargarCelda(int fila, int columna, String codigo) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) matriz[fila][columna] = codigo;
    }
}
