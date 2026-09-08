package com.refugio.servicio;

/**
 * Autenticacion local con usuarios cargados desde memoria.
 * Bloquea la sesion tras 3 intentos fallidos.
 */
public class AutenticacionService {
    private String[] usuarios = {"admin1", "auxiliar1"};
    private String[] contrasenas = {"Refugio2026", "Auxiliar2026"};
    private String[] roles = {"ADMIN", "AUXILIAR"};

    private static final int MAX_INTENTOS = 3;
    private int intentosFallidos = 0;
    private boolean bloqueado = false;
    private String rolActual = null;

    public boolean isBloqueado() { return bloqueado; }

    /** Retorna "OK", "FALLIDO" o "BLOQUEADO". */
    public String iniciarSesion(String usuario, String contrasena) {
        if (bloqueado) return "BLOQUEADO";
        for (int i = 0; i < usuarios.length; i++) {
            if (usuarios[i].equals(usuario) && contrasenas[i].equals(contrasena)) {
                rolActual = roles[i];
                intentosFallidos = 0;
                return "OK";
            }
        }
        intentosFallidos++;
        if (intentosFallidos >= MAX_INTENTOS) {
            bloqueado = true;
            return "BLOQUEADO";
        }
        return "FALLIDO";
    }

    public int getIntentosFallidos() { return intentosFallidos; }
    public int getIntentosRestantes() { return MAX_INTENTOS - intentosFallidos; }
    public String getRolActual() { return rolActual; }
}
