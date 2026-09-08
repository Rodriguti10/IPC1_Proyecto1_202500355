package com.refugio.servicio;

import com.refugio.modelo.Animal;

/**
 * Maneja el arreglo estatico de Animales.
 * Regla clave: no se elimina fisicamente, solo baja logica
 */
public class AnimalService {
    public static final int MAX_ANIMALES = 50;
    private Animal[] animales = new Animal[MAX_ANIMALES];
    private int cantidad = 0;
    private int consecutivo = 0;

    public boolean existeCodigo(String codigo) {
        for (int i = 0; i < cantidad; i++) {
            if (animales[i].getCodigo().equalsIgnoreCase(codigo)) return true;
        }
        return false;
    }

    public String generarCodigo() {
        consecutivo++;
        return String.format("A-%03d", consecutivo);
    }

    private void actualizarConsecutivoDesdeCodigo(String codigo) {
        try {
            int n = Integer.parseInt(codigo.substring(2));
            if (n > consecutivo) consecutivo = n;
        } catch (Exception e) { /* codigo con formato distinto, se ignora */ }
    }

    /** Registra un animal con un codigo especifico (permite reutilizar código de un Rescate). */
    public boolean registrarConCodigo(String codigo, String nombre, String especie, int edad,
                                       String estadoClinico, String usuario) {
        if (cantidad >= MAX_ANIMALES) return false;
        if (codigo == null || existeCodigo(codigo)) return false;
        if (nombre == null || nombre.trim().isEmpty()) return false;
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) return false;
        if (!especie.equals("Perro") && !especie.equals("Gato")) return false;
        if (edad < 0 || edad > 25) return false;
        animales[cantidad] = new Animal(codigo, nombre, especie, edad, estadoClinico, "DISPONIBLE");
        cantidad++;
        actualizarConsecutivoDesdeCodigo(codigo);
        return true;
    }

    public Animal buscarPorCodigo(String codigo) {
        for (int i = 0; i < cantidad; i++) {
            if (animales[i].getCodigo().equalsIgnoreCase(codigo)) return animales[i];
        }
        return null;
    }

    public boolean editarEstadoClinico(String codigo, String nuevoEstado) {
        Animal a = buscarPorCodigo(codigo);
        if (a == null) return false;
        if (!nuevoEstado.equals("EN_OBSERVACION") && !nuevoEstado.equals("EN_TRATAMIENTO") && !nuevoEstado.equals("APTO"))
            return false;
        a.setEstadoClinico(nuevoEstado);
        return true;
    }

    public boolean marcarAdoptado(String codigo) {
        Animal a = buscarPorCodigo(codigo);
        if (a == null) return false;
        a.setEstadoAdopcion("ADOPTADO");
        return true;
    }

    public boolean eliminarLogico(String codigo) {
        Animal a = buscarPorCodigo(codigo);
        if (a == null || a.getEstadoAdopcion().equals("ELIMINADO")) return false;
        a.setEstadoAdopcion("ELIMINADO");
        return true;
    }

    public Animal[] listarActivos() {
        int n = 0;
        for (int i = 0; i < cantidad; i++) if (!animales[i].getEstadoAdopcion().equals("ELIMINADO")) n++;
        Animal[] res = new Animal[n];
        int j = 0;
        for (int i = 0; i < cantidad; i++)
            if (!animales[i].getEstadoAdopcion().equals("ELIMINADO")) res[j++] = animales[i];
        return res;
    }

    public Animal[] obtenerTodos() {
        Animal[] res = new Animal[cantidad];
        System.arraycopy(animales, 0, res, 0, cantidad);
        return res;
    }

    public boolean agregarDesdeArchivo(Animal a) {
        if (cantidad >= MAX_ANIMALES) return false;
        animales[cantidad++] = a;
        actualizarConsecutivoDesdeCodigo(a.getCodigo());
        return true;
    }

    public int getCantidad() { return cantidad; }
}
