package com.refugio.modelo;

/**
 * codigo: prefijo "R-" + consecutivo
 * prioridad: ALTA, MEDIA, BAJA
 * estado: PENDIENTE, ATENDIDO
 */
public class Rescate {
    private String codigo;
    private String prioridad;
    private String estado;
    private String fechaReporte;
    private String codigoAnimalVinculado;

    public Rescate(String codigo, String prioridad, String estado, String fechaReporte, String codigoAnimalVinculado) {
        this.codigo = codigo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaReporte = fechaReporte;
        this.codigoAnimalVinculado = codigoAnimalVinculado;
    }

    public String getCodigo() { return codigo; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaReporte() { return fechaReporte; }
    public String getCodigoAnimalVinculado() { return codigoAnimalVinculado; }
    public void setCodigoAnimalVinculado(String c) { this.codigoAnimalVinculado = c; }

    public String toLineaArchivo() {
        return codigo + "|" + prioridad + "|" + estado + "|" + fechaReporte + "|" + codigoAnimalVinculado;
    }

    public static Rescate desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Rescate(p[0], p[1], p[2], p[3], p.length > 4 ? p[4] : "");
    }
}
