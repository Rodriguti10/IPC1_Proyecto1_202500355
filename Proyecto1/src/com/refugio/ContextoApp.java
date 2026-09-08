package com.refugio;

import com.refugio.servicio.*;
import com.refugio.bitacora.Bitacora;

/**
 * Contexto compartido de la aplicacion: mantiene una unica instancia de cada
 * servicio/arreglo mientras el programa esta en ejecucion (datos en memoria).
 * Se simplifica asi el paso de referencias entre las ventanas Swing.
 */
public class ContextoApp {
    public static AnimalService animalService = new AnimalService();
    public static AdoptanteService adoptanteService = new AdoptanteService();
    public static SolicitudService solicitudService = new SolicitudService();
    public static RescateService rescateService = new RescateService();
    public static UbicacionService ubicacionService = new UbicacionService();
    public static Bitacora bitacora = new Bitacora();

    public static String usuarioActual = "invitado";
    public static String rolActual = "AUXILIAR";
}
