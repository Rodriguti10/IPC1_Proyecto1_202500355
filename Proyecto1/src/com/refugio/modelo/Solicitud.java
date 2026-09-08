package com.refugio.modelo;

/**
 * codigo: prefijo "S-" + consecutivo
 * estado: PENDIENTE, APROBADA, RECHAZADA, COMPLETADA
 */
public class Solicitud {
    private String codigo;
    private String codigoAnimal;
    private String codigoAdoptante;
    private String fecha;
    private String estado;

    public Solicitud(String codigo, String codigoAnimal, String codigoAdoptante, String fecha, String estado) {
        this.codigo = codigo;
        this.codigoAnimal = codigoAnimal;
        this.codigoAdoptante = codigoAdoptante;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public String getCodigoAnimal() { return codigoAnimal; }
    public String getCodigoAdoptante() { return codigoAdoptante; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toLineaArchivo() {
        return codigo + "|" + codigoAnimal + "|" + codigoAdoptante + "|" + fecha + "|" + estado;
    }

    public static Solicitud desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Solicitud(p[0], p[1], p[2], p[3], p[4]);
    }
}
