package com.refugio;

import com.refugio.gui.Estilos;
import com.refugio.gui.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Estilos.aplicarNimbus();
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
