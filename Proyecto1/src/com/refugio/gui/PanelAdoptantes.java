package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.modelo.Adoptante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAdoptantes extends JPanel {
    private JTextField txtNombre, txtDpi, txtTelefono;
    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelAdoptantes() {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel form = new JPanel(new GridLayout(1, 6, 5, 5));
        txtNombre = new JTextField();
        txtDpi = new JTextField();
        txtTelefono = new JTextField();
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("DPI (13 digitos):")); form.add(txtDpi);
        form.add(new JLabel("Teléfono (8 dígitos):")); form.add(txtTelefono);

        JPanel botones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnBuscar = new JButton("Buscar por DPI");
        JButton btnEditar = new JButton("Editar");
        JButton btnListar = new JButton("Listar todos");
        botones.add(btnRegistrar); botones.add(btnBuscar); botones.add(btnEditar); botones.add(btnListar);

        Estilos.estilizarPanelBotones(botones);

        JPanel encabezado = Estilos.crearEncabezado("Adoptantes", "Personas registradas para adopción",
                new IconoSimple(IconoSimple.Tipo.PERSONA, 30, Color.WHITE));

        JPanel norte = new JPanel(new BorderLayout());
        JPanel norteInterno = new JPanel(new BorderLayout());
        norteInterno.add(form, BorderLayout.NORTH);
        norteInterno.add(botones, BorderLayout.SOUTH);
        norte.add(encabezado, BorderLayout.NORTH);
        norte.add(norteInterno, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new String[]{"Código", "Nombre", "DPI", "Teléfono"}, 0);
        tabla = new JTable(modelo);
        Estilos.estilizarTabla(tabla);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnBuscar.addActionListener(e -> buscar());
        btnEditar.addActionListener(e -> editar());
        btnListar.addActionListener(e -> listar());

        listar();
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String dpi = txtDpi.getText().trim();
        String telefono = txtTelefono.getText().trim();
        boolean ok = ContextoApp.adoptanteService.registrar(nombre, dpi, telefono);
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "ADOPTANTES", "ALTA", "Adoptante registrado: " + nombre);
            JOptionPane.showMessageDialog(this, "Adoptante registrado.");
            txtNombre.setText(""); txtDpi.setText(""); txtTelefono.setText("");
            listar();
        } else {
            String motivo = ContextoApp.adoptanteService.existeDPI(dpi) ? "DPI duplicado" : "Datos inválidos (nombre, DPI o teléfono)";
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ADOPTANTES", "VALIDACION", motivo);
            JOptionPane.showMessageDialog(this, "No se pudo registrar: " + motivo);
        }
    }

    private void buscar() {
        String dpi = JOptionPane.showInputDialog(this, "DPI a buscar:");
        if (dpi == null) return;
        Adoptante a = ContextoApp.adoptanteService.buscarPorDPI(dpi.trim());
        if (a == null) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ADOPTANTES", "BUSQUEDA", "DPI no encontrado: " + dpi);
            JOptionPane.showMessageDialog(this, "No encontrado.");
            return;
        }
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getDpi(), a.getTelefono()});
    }

    private void editar() {
        String codigo = JOptionPane.showInputDialog(this, "Código del adoptante (ej. AD-001):");
        if (codigo == null) return;
        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre (vacío = no cambiar):");
        String nuevoTel = JOptionPane.showInputDialog(this, "Nuevo teléfono (vacío = no cambiar):");
        boolean ok = ContextoApp.adoptanteService.editar(codigo.trim(), nuevoNombre, nuevoTel);
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "ADOPTANTES", "EDITAR", "Adoptante " + codigo + " actualizado");
            listar();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ADOPTANTES", "VALIDACION", "No se pudo editar adoptante " + codigo);
            JOptionPane.showMessageDialog(this, "No se pudo editar (verifique el código).");
        }
    }

    private void listar() {
        modelo.setRowCount(0);
        for (Adoptante a : ContextoApp.adoptanteService.obtenerTodos()) {
            modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getDpi(), a.getTelefono()});
        }
    }
}
