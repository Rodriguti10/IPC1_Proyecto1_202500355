package com.refugio.servicio;

import com.refugio.modelo.Rescate;

/**
 * Maneja el arreglo estatico de Rescates urgentes.
 * Al atender un caso se genera/vincula su registro en Animales.
 */
public class RescateService {
    public static final int MAX_RESCATES = 30;
    private Rescate[] rescates = new Rescate[MAX_RESCATES];
    private int cantidad = 0;
    private int consecutivo = 0;

    public String generarCodigo() {
        consecutivo++;
        return String.format("R-%03d", consecutivo);
    }

    public boolean registrar(String prioridad, String fechaReporte) {
        if (cantidad >= MAX_RESCATES) return false;
        if (!prioridad.equals("ALTA") && !prioridad.equals("MEDIA") && !prioridad.equals("BAJA")) return false;
        String codigo = generarCodigo();
        rescates[cantidad++] = new Rescate(codigo, prioridad, "PENDIENTE", fechaReporte, "");
        return true;
    }

    public Rescate buscarPorCodigo(String codigo) {
        for (int i = 0; i < cantidad; i++) if (rescates[i].getCodigo().equalsIgnoreCase(codigo)) return rescates[i];
        return null;
    }

    public boolean vincularAnimal(String codigoRescate, String codigoAnimal) {
        Rescate r = buscarPorCodigo(codigoRescate);
        if (r == null) return false;
        r.setCodigoAnimalVinculado(codigoAnimal);
        r.setEstado("ATENDIDO");
        return true;
    }

    /** Codigo de animal sugerido reutilizando el mismo consecutivo del rescate (R-009 -> A-009). */
    public String codigoAnimalSugerido(String codigoRescate) {
        return "A-" + codigoRescate.substring(2);
    }

    public Rescate[] listarPendientes() {
        int n = 0;
        for (int i = 0; i < cantidad; i++) if (rescates[i].getEstado().equals("PENDIENTE")) n++;
        Rescate[] res = new Rescate[n];
        int j = 0;
        for (int i = 0; i < cantidad; i++) if (rescates[i].getEstado().equals("PENDIENTE")) res[j++] = rescates[i];
        return res;
    }

    /** Ordena copia del arreglo por prioridad (ALTA primero) usando burbuja manual. */
    public Rescate[] listarOrdenadosPorPrioridad() {
        Rescate[] copia = obtenerTodos();
        for (int i = 0; i < copia.length - 1; i++) {
            for (int j = 0; j < copia.length - 1 - i; j++) {
                if (prioridadValor(copia[j].getPrioridad()) > prioridadValor(copia[j + 1].getPrioridad())) {
                    Rescate tmp = copia[j];
                    copia[j] = copia[j + 1];
                    copia[j + 1] = tmp;
                }
            }
        }
        return copia;
    }

    private int prioridadValor(String p) {
        if (p.equals("ALTA")) return 0;
        if (p.equals("MEDIA")) return 1;
        return 2;
    }

    public Rescate[] obtenerTodos() {
        Rescate[] res = new Rescate[cantidad];
        System.arraycopy(rescates, 0, res, 0, cantidad);
        return res;
    }

    public boolean agregarDesdeArchivo(Rescate r) {
        if (cantidad >= MAX_RESCATES) return false;
        rescates[cantidad++] = r;
        try {
            int n = Integer.parseInt(r.getCodigo().substring(2));
            if (n > consecutivo) consecutivo = n;
        } catch (Exception e) { }
        return true;
    }

    public int getCantidad() { return cantidad; }
}
