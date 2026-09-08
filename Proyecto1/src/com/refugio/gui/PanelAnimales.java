package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.modelo.Animal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelAnimales extends JPanel {
    private JTextField txtNombre, txtEdad;
    private JComboBox<String> cmbEspecie, cmbEstadoClinico;
    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelAnimales() {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(2, 6, 5, 5));
        txtNombre = new JTextField();
        cmbEspecie = new JComboBox<>(new String[]{"Perro", "Gato"});
        txtEdad = new JTextField();
        cmbEstadoClinico = new JComboBox<>(new String[]{"EN_OBSERVACION", "EN_TRATAMIENTO", "APTO"});
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Especie:")); form.add(cmbEspecie);
        form.add(new JLabel("Edad estimada (0-25):")); form.add(txtEdad);
        form.add(new JLabel("Estado clínico:")); form.add(cmbEstadoClinico);

        JPanel botones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar (código automático)");
        JButton btnBuscar = new JButton("Buscar por código");
        JButton btnEditarEstado = new JButton("Actualizar estado clínico");
        JButton btnEliminar = new JButton("Eliminar (baja lógica)");
        JButton btnListar = new JButton("Listar activos");
        botones.add(btnRegistrar); botones.add(btnBuscar);
        botones.add(btnEditarEstado); botones.add(btnEliminar); botones.add(btnListar);

        Estilos.estilizarPanelBotones(botones);

        JPanel encabezado = Estilos.crearEncabezado("Animales Rescatados", "Registro, búsqueda y estado de cada animal",
                new IconoSimple(IconoSimple.Tipo.PATA, 30, Color.WHITE));

        JPanel norte = new JPanel(new BorderLayout());
        JPanel norteInterno = new JPanel(new BorderLayout());
        norteInterno.add(form, BorderLayout.NORTH);
        norteInterno.add(botones, BorderLayout.SOUTH);
        norte.add(encabezado, BorderLayout.NORTH);
        norte.add(norteInterno, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new String[]{"Código", "Nombre", "Especie", "Edad", "Estado Clínico", "Estado Adopción"}, 0);
        tabla = new JTable(modelo);
        Estilos.estilizarTabla(tabla);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnBuscar.addActionListener(e -> buscar());
        btnEditarEstado.addActionListener(e -> actualizarEstado());
        btnEliminar.addActionListener(e -> eliminar());
        btnListar.addActionListener(e -> listar());

        listar();
    }

    private void registrar() {
        String nombre = txtNombre.getText().trim();
        String especie = (String) cmbEspecie.getSelectedItem();
        String estadoClinico = (String) cmbEstadoClinico.getSelectedItem();
        String edadTexto = txtEdad.getText().trim();

        if (nombre.isEmpty() || edadTexto.isEmpty()) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "VALIDACION", "Campos vacíos al registrar animal");
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
        } catch (NumberFormatException ex) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "VALIDACION", "Edad no numérica: " + edadTexto);
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entero.");
            return;
        }
        if (edad < 0 || edad > 25) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "VALIDACION", "Edad fuera de rango: " + edad);
            JOptionPane.showMessageDialog(this, "La edad debe estar entre 0 y 25.");
            return;
        }
        String codigo = ContextoApp.animalService.generarCodigo();
        boolean ok = ContextoApp.animalService.registrarConCodigo(codigo, nombre, especie, edad, estadoClinico, ContextoApp.usuarioActual);
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "ANIMALES", "ALTA", "Animal " + codigo + " registrado");
            JOptionPane.showMessageDialog(this, "Animal registrado con código " + codigo);
            txtNombre.setText(""); txtEdad.setText("");
            listar();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "VALIDACION", "No se pudo registrar animal " + codigo);
            JOptionPane.showMessageDialog(this, "No se pudo registrar el animal.");
        }
    }

    private void buscar() {
        String codigo = JOptionPane.showInputDialog(this, "Ingrese código a buscar (ej. A-001):");
        if (codigo == null) return;
        Animal a = ContextoApp.animalService.buscarPorCodigo(codigo.trim());
        if (a == null) {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "BUSQUEDA", "Código no encontrado: " + codigo);
            JOptionPane.showMessageDialog(this, "No se encontro el animal.");
            return;
        }
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getEspecie(), a.getEdadEstimada(), a.getEstadoClinico(), a.getEstadoAdopcion()});
    }

    private void actualizarEstado() {
        String codigo = JOptionPane.showInputDialog(this, "Código del animal:");
        if (codigo == null) return;
        String nuevo = (String) JOptionPane.showInputDialog(this, "Nuevo estado clínico:", "Actualizar",
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"EN_OBSERVACION", "EN_TRATAMIENTO", "APTO"}, "APTO");
        if (nuevo == null) return;
        boolean ok = ContextoApp.animalService.editarEstadoClinico(codigo.trim(), nuevo);
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "ANIMALES", "EDITAR", "Animal " + codigo + " cambia estado clínico a " + nuevo);
            JOptionPane.showMessageDialog(this, "Estado actualizado.");
            listar();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "VALIDACION", "No se pudo actualizar estado del animal " + codigo);
            JOptionPane.showMessageDialog(this, "No se pudo actualizar.");
        }
    }

    private void eliminar() {
        if (!ContextoApp.rolActual.equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Solo el administrador puede eliminar registros.");
            return;
        }
        String codigo = JOptionPane.showInputDialog(this, "Código del animal a eliminar:");
        if (codigo == null) return;
        boolean ok = ContextoApp.animalService.eliminarLogico(codigo.trim());
        if (ok) {
            ContextoApp.ubicacionService.liberarPorCodigo(codigo.trim());
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "ANIMALES", "BAJA", "Animal " + codigo + " eliminado lógicamente y espacio liberado");
            JOptionPane.showMessageDialog(this, "Animal eliminado.");
            listar();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "ANIMALES", "VALIDACION", "No se pudo eliminar animal " + codigo);
            JOptionPane.showMessageDialog(this, "No se pudo eliminar.");
        }
    }

    private void listar() {
        modelo.setRowCount(0);
        for (Animal a : ContextoApp.animalService.listarActivos()) {
            modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getEspecie(), a.getEdadEstimada(), a.getEstadoClinico(), a.getEstadoAdopcion()});
        }
    }
}
