package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelSolicitudes extends JPanel {
    private JTextField txtCodigoAnimal, txtCodigoAdoptante;
    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelSolicitudes() {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel form = new JPanel(new GridLayout(1, 4, 5, 5));
        txtCodigoAnimal = new JTextField();
        txtCodigoAdoptante = new JTextField();
        form.add(new JLabel("Código animal:")); form.add(txtCodigoAnimal);
        form.add(new JLabel("Código adoptante:")); form.add(txtCodigoAdoptante);

        JPanel botones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar solicitud");
        JButton btnAprobar = new JButton("Aprobar");
        JButton btnRechazar = new JButton("Rechazar");
        JButton btnCompletar = new JButton("Completar");
        JButton btnPendientes = new JButton("Ver pendientes");
        JButton btnTodas = new JButton("Ver todas");
        botones.add(btnRegistrar); botones.add(btnAprobar); botones.add(btnRechazar);
        botones.add(btnCompletar); botones.add(btnPendientes); botones.add(btnTodas);

        Estilos.estilizarPanelBotones(botones);

        JPanel encabezado = Estilos.crearEncabezado("Solicitudes de Adopción", "Aprobar, rechazar y dar seguimiento",
                new IconoSimple(IconoSimple.Tipo.DOCUMENTO, 30, Color.WHITE));

        JPanel norte = new JPanel(new BorderLayout());
        JPanel norteInterno = new JPanel(new BorderLayout());
        norteInterno.add(form, BorderLayout.NORTH);
        norteInterno.add(botones, BorderLayout.SOUTH);
        norte.add(encabezado, BorderLayout.NORTH);
        norte.add(norteInterno, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new String[]{"Código", "Animal", "Adoptante", "Fecha", "Estado"}, 0);
        tabla = new JTable(modelo);
        Estilos.estilizarTabla(tabla);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnAprobar.addActionListener(e -> aprobar());
        btnRechazar.addActionListener(e -> rechazar());
        btnCompletar.addActionListener(e -> completar());
        btnPendientes.addActionListener(e -> listarPendientes());
        btnTodas.addActionListener(e -> listarTodas());

        listarTodas();
    }

    private void registrar() {
        String codAnimal = txtCodigoAnimal.getText().trim();
        String codAdoptante = txtCodigoAdoptante.getText().trim();
        Animal a = ContextoApp.animalService.buscarPorCodigo(codAnimal);
        Adoptante ad = ContextoApp.adoptanteService.buscarPorCodigo(codAdoptante);
        if (a == null || ad == null) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "SOLICITUDES", "VALIDACION", "Animal o adoptante no existe (A:" + codAnimal + " AD:" + codAdoptante + ")");
            JOptionPane.showMessageDialog(this, "Verifique que el código de animal y adoptante existan.");
            return;
        }
        if (!a.getEstadoAdopcion().equals("DISPONIBLE")) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "SOLICITUDES", "VALIDACION", "Animal " + codAnimal + " no está DISPONIBLE");
            JOptionPane.showMessageDialog(this, "El animal no está disponible para adopción.");
            return;
        }
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        boolean ok = ContextoApp.solicitudService.registrar(codAnimal, codAdoptante, fecha);
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "SOLICITUDES", "ALTA", "Solicitud registrada para animal " + codAnimal);
            JOptionPane.showMessageDialog(this, "Solicitud registrada.");
            listarTodas();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "SOLICITUDES", "VALIDACION", "No se pudo registrar solicitud");
        }
    }

    private void aprobar() {
        String codigo = JOptionPane.showInputDialog(this, "Código de solicitud a aprobar:");
        if (codigo == null) return;
        Solicitud s = ContextoApp.solicitudService.buscarPorCodigo(codigo.trim());
        if (s == null) { JOptionPane.showMessageDialog(this, "No existe."); return; }
        boolean ok = ContextoApp.solicitudService.aprobar(codigo.trim());
        if (ok) {
            ContextoApp.animalService.marcarAdoptado(s.getCodigoAnimal());
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "SOLICITUDES", "APROBAR",
                    "Solicitud " + codigo + " aprobada, " + s.getCodigoAnimal() + " pasa a ADOPTADO");
            listarTodas();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "SOLICITUDES", "VALIDACION", "No se pudo aprobar " + codigo);
            JOptionPane.showMessageDialog(this, "No se pudo aprobar (ya existe una aprobada para ese animal).");
        }
    }

    private void rechazar() {
        String codigo = JOptionPane.showInputDialog(this, "Código de solicitud a rechazar:");
        if (codigo == null) return;
        boolean ok = ContextoApp.solicitudService.rechazar(codigo.trim());
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "SOLICITUDES", "RECHAZAR", "Solicitud " + codigo + " rechazada");
            listarTodas();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "SOLICITUDES", "VALIDACION", "No se pudo rechazar " + codigo);
        }
    }

    private void completar() {
        String codigo = JOptionPane.showInputDialog(this, "Código de solicitud a completar:");
        if (codigo == null) return;
        boolean ok = ContextoApp.solicitudService.completar(codigo.trim());
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "SOLICITUDES", "COMPLETAR", "Solicitud " + codigo + " completada");
            listarTodas();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "SOLICITUDES", "VALIDACION", "No se pudo completar " + codigo);
            JOptionPane.showMessageDialog(this, "Solo se pueden completar solicitudes APROBADAS.");
        }
    }

    private void listarPendientes() {
        modelo.setRowCount(0);
        for (Solicitud s : ContextoApp.solicitudService.listarPendientes())
            modelo.addRow(new Object[]{s.getCodigo(), s.getCodigoAnimal(), s.getCodigoAdoptante(), s.getFecha(), s.getEstado()});
    }

    private void listarTodas() {
        modelo.setRowCount(0);
        for (Solicitud s : ContextoApp.solicitudService.obtenerTodos())
            modelo.addRow(new Object[]{s.getCodigo(), s.getCodigoAnimal(), s.getCodigoAdoptante(), s.getFecha(), s.getEstado()});
    }
}
