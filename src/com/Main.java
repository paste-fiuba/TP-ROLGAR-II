package com; 

import com.logica.ControladorJuego;
import com.logica.PartidaDeRolgar;
import com.ui.PanelJuego;
import com.ui.VentanaJuego; 		

/**
 * Punto de entrada principal del juego Rolgar II.   
 * Su única responsabilidad es inicializar los componentes
 * principales de la UI y el TDA PartidaDeRolgar.
 */
public class Main {

    public static void main(String[] args) {
        
        
        PartidaDeRolgar partida = new PartidaDeRolgar();

        PanelJuego miPanel = new PanelJuego();

        ControladorJuego miControlador = new ControladorJuego(partida, miPanel);
        
        miPanel.setControlador(miControlador);

        new VentanaJuego(miPanel);
    }
}
