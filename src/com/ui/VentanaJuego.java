package com.ui; 

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class VentanaJuego extends JFrame {

    public VentanaJuego(PanelJuego panel) {
        setTitle("Rolgar II");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Quitar la barra de título y los bordes
        setUndecorated(true);
        
        // Agregar el panel ANTES de hacer nada más
        add(panel);
        
        // Usar el modo maximizado
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        // Hacer visible la ventana DESPUÉS de agregar los componentes
        setVisible(true);
        
        // Pedir el foco para el teclado al final
        panel.iniciarOyenteTeclado();
    }
}