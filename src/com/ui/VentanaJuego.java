package com.ui; 

import javax.swing.JFrame;

/**
 * Representa la ventana principal del juego.
 * Esta clase crea la parte gráfica con bitmaps.
 */
public class VentanaJuego extends JFrame {

    /**
     * Constructor.
     * @param panel El PanelJuego que debe mostrar.
     */
    public VentanaJuego(PanelJuego panel) {
        setTitle("Rolgar II");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        add(panel);

        pack();

        setLocationRelativeTo(null);

        setVisible(true);
    }
}