package com.refugio.servicio;

import com.refugio.modelo.Adoptante;

/** Maneja el arreglo estatico de Adoptantes. No requiere eliminacion. */
public class AdoptanteService {
    public static final int MAX_ADOPTANTES = 30;
    private Adoptante[] adoptantes = new Adoptante[MAX_ADOPTANTES];
    private int cantidad = 0;
    private int consecutivo = 0;

    public String generarCodigo() {
        consecutivo++;
        return String.format("AD-%03d", consecutivo);
    }

    public boolean existeDPI(String dpi) {
        for (int i = 0; i < cantidad; i++) if (adoptantes[i].getDpi().equals(dpi)) return true;
        return false;
    }

    public boolean registrar(String nombre, String dpi, String telefono) {
        if (cantidad >= MAX_ADOPTANTES) return false;
        if (nombre == null || !nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) return false;
        if (dpi == null || !dpi.matches("\\d{13}")) return false;
        if (telefono == null || !telefono.matches("\\d{8}")) return false;
        if (existeDPI(dpi)) return false;
        String codigo = generarCodigo();
        adoptantes[cantidad++] = new Adoptante(codigo, nombre.trim(), dpi, telefono);
        return true;
    }

    public Adoptante buscarPorCodigo(String codigo) {
        for (int i = 0; i < cantidad; i++) if (adoptantes[i].getCodigo().equalsIgnoreCase(codigo)) return adoptantes[i];
        return null;
    }

    public Adoptante buscarPorDPI(String dpi) {
        for (int i = 0; i < cantidad; i++) if (adoptantes[i].getDpi().equals(dpi)) return adoptantes[i];
        return null;
    }

    public boolean editar(String codigo, String nombre, String telefono) {
        Adoptante a = buscarPorCodigo(codigo);
        if (a == null) return false;
        if (nombre != null && !nombre.trim().isEmpty()) a.setNombre(nombre.trim());
        if (telefono != null && telefono.matches("\\d{8}")) a.setTelefono(telefono);
        return true;
    }

    public Adoptante[] obtenerTodos() {
        Adoptante[] res = new Adoptante[cantidad];
        System.arraycopy(adoptantes, 0, res, 0, cantidad);
        return res;
    }

    public boolean agregarDesdeArchivo(Adoptante a) {
        if (cantidad >= MAX_ADOPTANTES) return false;
        adoptantes[cantidad++] = a;
        try {
            int n = Integer.parseInt(a.getCodigo().substring(3));
            if (n > consecutivo) consecutivo = n;
        } catch (Exception e) { }
        return true;
    }

    public int getCantidad() { return cantidad; }
}
