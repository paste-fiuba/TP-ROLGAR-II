package com.logica; // O el paquete de tu controlador

import java.awt.event.KeyEvent;

// --- ¡REVISA ESTAS RUTAS! ---
// Asegúrate de que coincidan con tu estructura de paquetes real.
// Si tu paquete es "com.nombredelgrupo.tp2.modelo.tablero",
// entonces el import debe ser "com.nombredelgrupo.tp2.modelo.tablero.Tablero;"

// Imports del Modelo
import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;

// Imports de la Vista
import com.ui.PanelJuego;

/**
 * El "Controlador". Esta clase es el cerebro.
 * Conecta el input (teclado) con el Modelo (jugador)
 * y le avisa a la Vista (panel) cuándo debe redibujar.
 */
public class ControladorJuego {

    // Referencias al Modelo
    private Tablero tablero;
    private Personaje jugador;
    
    // Referencia a la Vista
    private PanelJuego panelJuego;

    /**
     * Constructor. Necesita saber sobre el Modelo y la Vista.
     */
    public ControladorJuego(Tablero tablero, Personaje jugador, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.panelJuego = panel;
    }

    /**
     * El "cerebro" que procesa la entrada del usuario.
     */
    public void manejarInput(int keyCode) {
        
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();

        // W = Arriba (Y-1)
        if (keyCode == KeyEvent.VK_W) { 
            if (esMovimientoValido(x, y - 1, z)) {
                jugador.mover(0, -1, 0); 
            }
        }
        // S = Abajo (Y+1)
        else if (keyCode == KeyEvent.VK_S) {
            if (esMovimientoValido(x, y + 1, z)) {
                jugador.mover(0, 1, 0);
            }
        }
        // A = Izquierda (X-1)
        else if (keyCode == KeyEvent.VK_A) {
            if (esMovimientoValido(x - 1, y, z)) {
                jugador.mover(-1, 0, 0);
            }
        }
        // D = Derecha (X+1)
        else if (keyCode == KeyEvent.VK_D) {
            if (esMovimientoValido(x + 1, y, z)) {
                jugador.mover(1, 0, 0);
            }
        }
        
        this.panelJuego.repaint();
    }

    /**
     * Lógica de movimiento.
     */
    private boolean esMovimientoValido(int targetX, int targetY, int targetZ) {
        // 1. Verificar límites del tablero
        if (!tablero.esCoordenadaValida(targetX, targetY, targetZ)) {
            return false;
        }
        
        // 2. Verificar terreno no transitable
        Casillero casilleroDestino = tablero.getCasillero(targetX, targetY, targetZ);
        if (casilleroDestino.getTipo() == TipoCasillero.ROCA || 
            casilleroDestino.getTipo() == TipoCasillero.AGUA) {
            return false;
        }
        
        return true; 
    }
}