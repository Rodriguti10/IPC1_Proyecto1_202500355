package com.refugio.modelo;

/**
 * codigo: prefijo "AD-" + consecutivo (ej. AD-007)
 * dpi: criterio de duplicado (13 digitos)
 */
public class Adoptante {
    private String codigo;
    private String nombre;
    private String dpi;
    private String telefono;

    public Adoptante(String codigo, String nombre, String dpi, String telefono) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.dpi = dpi;
        this.telefono = telefono;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDpi() { return dpi; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String toLineaArchivo() {
        return codigo + "|" + nombre + "|" + dpi + "|" + telefono;
    }

    public static Adoptante desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Adoptante(p[0], p[1], p[2], p[3]);
    }
}
