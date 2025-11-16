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
        
        // 1. Crear el TDA PartidaDeRolgar (que ahora está vacío)
        PartidaDeRolgar partida = new PartidaDeRolgar();

        // 2. Crear el PanelJuego (la vista)
        PanelJuego miPanel = new PanelJuego();

        // 3. Crear el ControladorJuego (que maneja los estados)
        ControladorJuego miControlador = new ControladorJuego(partida, miPanel);
        
        miPanel.setControlador(miControlador);

        // 4. Crear la Ventana y arrancar
        new VentanaJuego(miPanel);
        // La ventana se encarga de iniciar el oyente
    }
}
