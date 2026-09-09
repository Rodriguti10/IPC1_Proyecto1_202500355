package com.refugio.servicio;

import com.refugio.modelo.Solicitud;

/**
 * Maneja el arreglo estatico de Solicitudes de adopcion.
 * Regla: al aprobar una, cualquier otra PENDIENTE del mismo animal pasa a RECHAZADA.
 */
public class SolicitudService {
    public static final int MAX_SOLICITUDES = 50;
    private Solicitud[] solicitudes = new Solicitud[MAX_SOLICITUDES];
    private int cantidad = 0;
    private int consecutivo = 0;

    public String generarCodigo() {
        consecutivo++;
        return String.format("S-%03d", consecutivo);
    }

    public boolean registrar(String codigoAnimal, String codigoAdoptante, String fecha) {
        if (cantidad >= MAX_SOLICITUDES) return false;
        String codigo = generarCodigo();
        solicitudes[cantidad++] = new Solicitud(codigo, codigoAnimal, codigoAdoptante, fecha, "PENDIENTE");
        return true;
    }

    public Solicitud buscarPorCodigo(String codigo) {
        for (int i = 0; i < cantidad; i++) if (solicitudes[i].getCodigo().equalsIgnoreCase(codigo)) return solicitudes[i];
        return null;
    }

    public boolean existeAprobadaParaAnimal(String codigoAnimal) {
        for (int i = 0; i < cantidad; i++)
            if (solicitudes[i].getCodigoAnimal().equalsIgnoreCase(codigoAnimal) && solicitudes[i].getEstado().equals("APROBADA"))
                return true;
        return false;
    }
    
    public boolean existeSolicitudPendiente(String codigoAnimal, String codigoAdoptante) {
    for (int i = 0; i < cantidad; i++) {
        if (solicitudes[i].getCodigoAnimal().equalsIgnoreCase(codigoAnimal)
            && solicitudes[i].getCodigoAdoptante().equalsIgnoreCase(codigoAdoptante)
            && solicitudes[i].getEstado().equals("PENDIENTE")) {
            return true;
        }
    }
    return false;
}

    public boolean aprobar(String codigo) {
        Solicitud s = buscarPorCodigo(codigo);
        if (s == null) return false;
        if (existeAprobadaParaAnimal(s.getCodigoAnimal())) return false;
        s.setEstado("APROBADA");
        for (int i = 0; i < cantidad; i++) {
            if (!solicitudes[i].getCodigo().equals(codigo)
                && solicitudes[i].getCodigoAnimal().equalsIgnoreCase(s.getCodigoAnimal())
                && solicitudes[i].getEstado().equals("PENDIENTE")) {
                solicitudes[i].setEstado("RECHAZADA");
            }
        }
        return true;
    }

    public boolean rechazar(String codigo) {
        Solicitud s = buscarPorCodigo(codigo);
        if (s == null) return false;
        s.setEstado("RECHAZADA");
        return true;
    }

    public boolean completar(String codigo) {
        Solicitud s = buscarPorCodigo(codigo);
        if (s == null || !s.getEstado().equals("APROBADA")) return false;
        s.setEstado("COMPLETADA");
        return true;
    }

    public Solicitud[] listarPendientes() {
        int n = 0;
        for (int i = 0; i < cantidad; i++) if (solicitudes[i].getEstado().equals("PENDIENTE")) n++;
        Solicitud[] res = new Solicitud[n];
        int j = 0;
        for (int i = 0; i < cantidad; i++) if (solicitudes[i].getEstado().equals("PENDIENTE")) res[j++] = solicitudes[i];
        return res;
    }

    public Solicitud[] obtenerTodos() {
        Solicitud[] res = new Solicitud[cantidad];
        System.arraycopy(solicitudes, 0, res, 0, cantidad);
        return res;
    }

    public boolean agregarDesdeArchivo(Solicitud s) {
        if (cantidad >= MAX_SOLICITUDES) return false;
        solicitudes[cantidad++] = s;
        try {
            int n = Integer.parseInt(s.getCodigo().substring(2));
            if (n > consecutivo) consecutivo = n;
        } catch (Exception e) { }
        return true;
    }

    public int getCantidad() { return cantidad; }
}
