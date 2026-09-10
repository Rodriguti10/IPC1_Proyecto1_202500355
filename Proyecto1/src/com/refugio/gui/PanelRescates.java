package com.refugio.gui;

import com.refugio.ContextoApp;
import com.refugio.modelo.Rescate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelRescates extends JPanel {
    private JComboBox<String> cmbPrioridad;
    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelRescates() {
        setLayout(new BorderLayout());
        setBackground(Estilos.FONDO);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
        cmbPrioridad = new JComboBox<>(new String[]{"ALTA", "MEDIA", "BAJA"});
        form.add(new JLabel("Prioridad:")); form.add(cmbPrioridad);

        JPanel botones = new JPanel();
        JButton btnRegistrar = new JButton("Registrar caso");
        JButton btnAtender = new JButton("Atender caso");
        JButton btnReporte = new JButton("Ver reporte (por prioridad)");
        botones.add(btnRegistrar); botones.add(btnAtender); botones.add(btnReporte);

        Estilos.estilizarPanelBotones(botones);

        JPanel encabezado = Estilos.crearEncabezado("Rescates Urgentes", "Casos reportados y su atención",
                new IconoSimple(IconoSimple.Tipo.ALERTA, 30, Color.WHITE));

        JPanel norte = new JPanel(new BorderLayout());
        JPanel norteInterno = new JPanel(new BorderLayout());
        norteInterno.add(form, BorderLayout.NORTH);
        norteInterno.add(botones, BorderLayout.SOUTH);
        norte.add(encabezado, BorderLayout.NORTH);
        norte.add(norteInterno, BorderLayout.CENTER);

        modelo = new DefaultTableModel(new String[]{"Código", "Prioridad", "Estado", "Fecha", "Animal vinculado"}, 0);
        tabla = new JTable(modelo);
        Estilos.estilizarTabla(tabla);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnAtender.addActionListener(e -> atender());
        btnReporte.addActionListener(e -> reporte());

        reporte();
    }

    private void registrar() {
        String prioridad = (String) cmbPrioridad.getSelectedItem();
        String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        boolean ok = ContextoApp.rescateService.registrar(prioridad, fecha);
        if (ok) {
            ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "RESCATES", "ALTA", "Caso de rescate registrado con prioridad " + prioridad);
            JOptionPane.showMessageDialog(this, "Caso registrado.");
            reporte();
        } else {
            ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "RESCATES", "VALIDACION", "No se pudo registrar caso de rescate");
        }
    }

    private void atender() {
        String codigo = JOptionPane.showInputDialog(this, "Código de rescate a atender (ej. R-001):");
        if (codigo == null) return;
        Rescate r = ContextoApp.rescateService.buscarPorCodigo(codigo.trim());
        if (r == null) { JOptionPane.showMessageDialog(this, "No existe."); 
        return; 
        }
        if (r.getEstado().equals("ATENDIDO")) {
        JOptionPane.showMessageDialog(this, "Este caso ya fue atendido anteriormente (vinculado a " + r.getCodigoAnimalVinculado() + ").");
        return;
        }
        String codigoAnimalExistente = JOptionPane.showInputDialog(this,
                "Si ya existe un animal registrado para este caso, indique su código.\nSi no existe, deje vacío para crear uno nuevo:");
        if (codigoAnimalExistente == null) return;

        String codigoFinal;
        if (!codigoAnimalExistente.trim().isEmpty()) {
            codigoFinal = codigoAnimalExistente.trim();
            if (ContextoApp.animalService.buscarPorCodigo(codigoFinal) == null) {
                ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "RESCATES", "VALIDACION", "Código de animal vinculado no existe: " + codigoFinal);
                JOptionPane.showMessageDialog(this, "Ese código de animal no existe.");
                return;
            }
        } else {
            codigoFinal = ContextoApp.rescateService.codigoAnimalSugerido(r.getCodigo());
            String especie = (String) JOptionPane.showInputDialog(this, "Especie del animal rescatado:",
                    "Nuevo animal", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Perro", "Gato"}, "Perro");
            if (especie == null) return;
            boolean creado = ContextoApp.animalService.registrarConCodigo(codigoFinal, "Rescate " + r.getCodigo(),
                    especie, 0, "EN_TRATAMIENTO", ContextoApp.usuarioActual);
            if (!creado) {
                ContextoApp.bitacora.registrarError(ContextoApp.usuarioActual, "RESCATES", "VALIDACION", "No se pudo crear animal vinculado a " + codigo);
                JOptionPane.showMessageDialog(this, "No se pudo crear el registro del animal.");
                return;
            }
        }
        ContextoApp.rescateService.vincularAnimal(codigo.trim(), codigoFinal);
        ContextoApp.bitacora.registrarAccion(ContextoApp.usuarioActual, "RESCATES", "ATENDER",
                "Rescate " + codigo + " atendido, vinculado a animal " + codigoFinal);
        JOptionPane.showMessageDialog(this, "Caso atendido y vinculado a " + codigoFinal);
        reporte();
    }

    private void reporte() {
        modelo.setRowCount(0);
        for (Rescate r : ContextoApp.rescateService.listarOrdenadosPorPrioridad())
            modelo.addRow(new Object[]{r.getCodigo(), r.getPrioridad(), r.getEstado(), r.getFechaReporte(), r.getCodigoAnimalVinculado()});
    }
}
