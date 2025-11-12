package com.ui; 

import javax.swing.JFrame;
import java.awt.Color;

public class VentanaJuego extends JFrame {

    public VentanaJuego(PanelJuego panel) {
        setTitle("Rolgar II");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        // 1. Quita la barra de título y los bordes
        setUndecorated(true);
        
        // 2. Pone la ventana en modo "maximizada"
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        
        add(panel);

        setVisible(true);
    }
}