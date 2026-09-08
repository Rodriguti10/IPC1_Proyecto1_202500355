package com.refugio.modelo;

/**
 * Representa un animal rescatado.
 * codigo: prefijo "A-" + consecutivo (ej. A-014)
 * especie: "Perro" o "Gato"
 * estadoClinico: EN_OBSERVACION, EN_TRATAMIENTO, APTO
 * estadoAdopcion: DISPONIBLE, ADOPTADO, ELIMINADO (baja logica)
 */
public class Animal {
    private String codigo;
    private String nombre;
    private String especie;
    private int edadEstimada;
    private String estadoClinico;
    private String estadoAdopcion;

    public Animal(String codigo, String nombre, String especie, int edadEstimada,
                   String estadoClinico, String estadoAdopcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.especie = especie;
        this.edadEstimada = edadEstimada;
        this.estadoClinico = estadoClinico;
        this.estadoAdopcion = estadoAdopcion;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public int getEdadEstimada() { return edadEstimada; }
    public void setEdadEstimada(int edadEstimada) { this.edadEstimada = edadEstimada; }
    public String getEstadoClinico() { return estadoClinico; }
    public void setEstadoClinico(String estadoClinico) { this.estadoClinico = estadoClinico; }
    public String getEstadoAdopcion() { return estadoAdopcion; }
    public void setEstadoAdopcion(String estadoAdopcion) { this.estadoAdopcion = estadoAdopcion; }

    // Formato de persistencia: codigo|nombre|especie|edad|estadoClinico|estadoAdopcion
    public String toLineaArchivo() {
        return codigo + "|" + nombre + "|" + especie + "|" + edadEstimada + "|" +
               estadoClinico + "|" + estadoAdopcion;
    }

    public static Animal desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Animal(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]);
    }
}
