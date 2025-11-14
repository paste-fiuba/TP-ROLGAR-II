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
        // Se inicializa sin un tablero o jugadores, se cargarán desde el menú.
        PanelJuego miPanel = new PanelJuego();

        // 3. Crear el ControladorJuego (que maneja los estados)
        // Le pasamos la instancia de partida (para cargarla) y el panel (para dibujarlo)
        ControladorJuego miControlador = new ControladorJuego(partida, miPanel);
        
        miPanel.setControlador(miControlador);

        // 4. Crear la Ventana y arrancar
        new VentanaJuego(miPanel);
    }
}