package com.refugio.reportes;

import com.refugio.modelo.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Genera reportes en formato HTML con CSS basico, con fecha/hora en el nombre de archivo. */
public class ReporteHTML {

    private static String marcaTiempo() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private static void escribir(String nombreBase, String contenidoHtml) {
        String nombreArchivo = "reportes/" + nombreBase + "_" + marcaTiempo() + ".html";
        try {
            File f = new File(nombreArchivo);
            if (f.getParentFile() != null) f.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write(contenidoHtml);
            }
        } catch (IOException e) {
            System.err.println("Error al generar reporte: " + e.getMessage());
        }
    }

    private static String estilo() {
        return "<style>body{font-family:Arial;margin:20px;}h1{color:#2554a1;}" +
               "table{border-collapse:collapse;width:100%;}th,td{border:1px solid #999;padding:6px;text-align:left;}" +
               "th{background:#2554a1;color:white;}tr:nth-child(even){background:#f2f2f2;}</style>";
    }

    public static void generarReporteAnimales(Animal[] animales) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset='UTF-8'>").append(estilo()).append("</head><body>");
        sb.append("<h1>Reporte de Animales</h1><table><tr><th>Código</th><th>Nombre</th><th>Especie</th>")
          .append("<th>Edad</th><th>Estado Clínico</th><th>Estado Adopción</th></tr>");
        for (Animal a : animales) {
            sb.append("<tr><td>").append(a.getCodigo()).append("</td><td>").append(a.getNombre())
              .append("</td><td>").append(a.getEspecie()).append("</td><td>").append(a.getEdadEstimada())
              .append("</td><td>").append(a.getEstadoClinico()).append("</td><td>").append(a.getEstadoAdopcion())
              .append("</td></tr>");
        }
        sb.append("</table></body></html>");
        escribir("reporte_animales", sb.toString());
    }

    public static void generarReporteAdopciones(Solicitud[] solicitudes) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset='UTF-8'>").append(estilo()).append("</head><body>");
        sb.append("<h1>Reporte de Solicitudes / Adopciones</h1><table><tr><th>Código</th><th>Animal</th>")
          .append("<th>Adoptante</th><th>Fecha</th><th>Estado</th></tr>");
        for (Solicitud s : solicitudes) {
            sb.append("<tr><td>").append(s.getCodigo()).append("</td><td>").append(s.getCodigoAnimal())
              .append("</td><td>").append(s.getCodigoAdoptante()).append("</td><td>").append(s.getFecha())
              .append("</td><td>").append(s.getEstado()).append("</td></tr>");
        }
        sb.append("</table></body></html>");
        escribir("reporte_adopciones", sb.toString());
    }

    public static void generarReporteOcupacion(String[][] matriz, String[] nombresZona) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset='UTF-8'>").append(estilo()).append("</head><body>");
        sb.append("<h1>Reporte de Ocupación del Refugio</h1><table><tr><th>Zona</th>");
        for (int j = 0; j < matriz[0].length; j++) sb.append("<th>Espacio ").append(j).append("</th>");
        sb.append("</tr>");
        for (int i = 0; i < matriz.length; i++) {
            sb.append("<tr><td>").append(nombresZona[i]).append("</td>");
            for (int j = 0; j < matriz[i].length; j++) {
                String v = matriz[i][j].isEmpty() ? "Libre" : matriz[i][j];
                sb.append("<td>").append(v).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table></body></html>");
        escribir("reporte_ocupacion", sb.toString());
    }

    public static void generarReporteBitacora(String[] acciones, String[] errores) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset='UTF-8'>").append(estilo()).append("</head><body>");
        sb.append("<h1>Bitácora de Acciones</h1><table><tr><th>Fecha</th><th>Usuario</th><th>Módulo</th>")
          .append("<th>Evento</th><th>Descripción</th></tr>");
        for (String l : acciones) {
            String[] p = l.split("\\|", -1);
            sb.append("<tr>");
            for (String campo : p) sb.append("<td>").append(campo).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table><h1>Bitácora de Errores</h1><table><tr><th>Fecha</th><th>Usuario</th><th>Módulo</th>")
          .append("<th>Evento</th><th>Motivo</th></tr>");
        for (String l : errores) {
            String[] p = l.split("\\|", -1);
            sb.append("<tr>");
            for (String campo : p) sb.append("<td>").append(campo).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table></body></html>");
        escribir("reporte_bitacora", sb.toString());
    }
}
