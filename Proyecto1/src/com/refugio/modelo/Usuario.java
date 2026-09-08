package com.refugio.modelo;

/** Representa un usuario del sistema (Administrador o Auxiliar). */
public class Usuario {
    private String usuario;
    private String contrasena;
    private String rol;

    public Usuario(String usuario, String contrasena, String rol) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getUsuario() { return usuario; }
    public String getContrasena() { return contrasena; }
    public String getRol() { return rol; }
}
