package com.ui; 

import javax.swing.JFrame;

/**
 * Representa la ventana principal del juego.
 * Esta clase crea la parte gráfica con bitmaps.
 */
public class VentanaJuego extends JFrame {

    /**
     * Constructor.
     * @param panel El PanelJuego (lienzo) que debe mostrar.
     */
    public VentanaJuego(PanelJuego panel) {
        // 1. Título de la ventana
        setTitle("Rolgar II");

        // 2. Acción al cerrar (terminar el programa)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 3. Impedimos que el usuario cambie el tamaño
        setResizable(false);

        // 4. Añadimos el panel a la ventana
        add(panel);

        // 5. Ajustamos el tamaño de la ventana al contenido 
        pack();

        // 6. Centramos la ventana en la pantalla
        setLocationRelativeTo(null);

        // 7. Hacemos visible la ventana
        setVisible(true);
    }
}