package com.refugio.gui;

import com.refugio.ContextoApp;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Panel de ubicaciones: representa la matriz [FILAS][COLUMNAS] del refugio. */
public class PanelUbicaciones extends JPanel {
    private DefaultTableModel modelo;
    private JTable tabla;
    private JTextField txtFila, txtColumna, txtCodigo;

    public PanelUbicaciones() {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(1, 6, 5, 5));
        txtFila = new JTextField();
        txtColumna = new JTextField();
        txtCodigo = new JTextField();
        form.add(new JLabel("Fila (0=Perros,1=Gatos):")); form.add(txtFila);
        form.add(new JLabel("Columna (0-4):")); form.add(txtColumna);
        form.add(new JLabel("Código animal:")); form.add(txtCodigo);

        JPanel botones = new JPanel();
        JButton btnAsignar = new JButton("Asignar");
        JButton btnLiberar = new JButton("Liberar");
        JButton btnActualizar = new JButton("Actualizar vista");
        botones.add(btnAsignar); botones.add(btnLiberar); botones.add(btnActualizar);

        JPanel encabezado = Estilos.crearEncabezado("Ubicaciones del Refugio",
                "Matriz de zonas y espacios disponibles", new IconoSimple(IconoSimple.Tipo.MAPA, 30, Color.WHITE));

        JPanel norteInterno = new JPanel(new BorderLayout());
        norteInterno.add(form, BorderLayout.NORTH);
        norteInterno.add(botones, BorderLayout.SOUTH);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(encabezado, BorderLayout.NORTH);
        norte.add(norteInterno, BorderLayout.CENTER);

        modelo = new DefaultTableModel();
        tabla = new JTable(modelo);
        Estilos.estilizarTabla(tabla);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnAsignar.addActionListener(e -> asignar());
        btnLiberar.addActionListener(e -> liberar());
        btnActualizar.addActionListener(e -> actualizarVista());

        actualizarVista();
    }

    private void asignar() {
        try {
            int fila = Integer.parseInt(txtFila.getText().trim());
            int col = Integer.parseInt(txtColumna.getText().trim());
            String codigo = txtCodigo.getText().trim();
            if (ContextoApp.animalService.buscarPorCodigo(codigo) == null) {
                JOptionPane.showMessageDialog(this, "El animal no existe.");
                return;
            }
            boolean ok = ContextoApp.ubicacionService.asignar(fila, col, codigo);
            if (ok) {
                ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "UBICACIONES", "ASIGNAR",
                        codigo + " asignado a [" + fila + "][" + col + "]");
            } else {
                ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "UBICACIONES", "CAPACIDAD",
                        "Celda [" + fila + "][" + col + "] ya ocupada o inválida");
                JOptionPane.showMessageDialog(this, "Espacio inválido u ocupado.");
            }
            actualizarVista();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fila y columna deben ser números.");
        }
    }

    private void liberar() {
        try {
            int fila = Integer.parseInt(txtFila.getText().trim());
            int col = Integer.parseInt(txtColumna.getText().trim());
            String codigoAnterior = ContextoApp.ubicacionService.obtener(fila, col);
            boolean ok = ContextoApp.ubicacionService.liberar(fila, col);
            if (ok) {
                ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "UBICACIONES", "LIBERAR",
                        "Celda [" + fila + "][" + col + "] liberada (antes: " + codigoAnterior + ")");
            }
            actualizarVista();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fila y columna deben ser números.");
        }
    }

    private void actualizarVista() {
        String[][] m = ContextoApp.ubicacionService.obtenerMatriz();
        String[] columnas = new String[m[0].length + 1];
        columnas[0] = "Zona";
        for (int j = 0; j < m[0].length; j++) columnas[j + 1] = "Espacio " + j;
        modelo.setColumnIdentifiers(columnas);
        modelo.setRowCount(0);
        for (int i = 0; i < m.length; i++) {
            Object[] fila = new Object[m[i].length + 1];
            fila[0] = ContextoApp.ubicacionService.nombreZona(i);
            for (int j = 0; j < m[i].length; j++) fila[j + 1] = m[i][j].isEmpty() ? "Libre" : m[i][j];
            modelo.addRow(fila);
        }
    }
}
