package com.logica;

import java.awt.event.KeyEvent;

import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;
import com.ui.PanelJuego;

/**
 * El "Controlador". Esta clase es el cerebro.
 * Conecta el input, el Modelo y la Vista.
 */
public class ControladorJuego {

    private Tablero tablero;
    private Personaje jugador;
    private PanelJuego panelJuego;

    /**
     * Constructor.
     */
    public ControladorJuego(Tablero tablero, Personaje jugador, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.panelJuego = panel;
    }

    /**
     * El "cerebro" que procesa la entrada del usuario.
     * ¡LÓGICA MEJORADA!
     */
    public void manejarInput(int keyCode) {
        
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();

        int dx = 0; // Delta X
        int dy = 0; // Delta Y

        // Convertimos el KeyCode en un delta (dx, dy)
        if (keyCode == KeyEvent.VK_W) { 
            dy = -1;
        } else if (keyCode == KeyEvent.VK_S) {
            dy = 1;
        } else if (keyCode == KeyEvent.VK_A) {
            dx = -1;
        } else if (keyCode == KeyEvent.VK_D) {
            dx = 1;
        } else {
            return; // Si no es una tecla de movimiento, no hacemos nada
        }

        int newX = x + dx;
        int newY = y + dy;

        // 1. Verificamos si el movimiento es válido
        if (esMovimientoValido(newX, newY, z)) {
            
            // 2. Movemos el Modelo (al jugador)
            jugador.setPosicion(newX, newY, z);
            
            // 3. Revisamos el casillero donde aterrizamos
            revisarCasilleroActual(newX, newY, z);
        }
        
        // 4. Le decimos a la Vista que se redibuje
        this.panelJuego.repaint();
    }

    /**
     * Lógica de movimiento.
     */
    private boolean esMovimientoValido(int targetX, int targetY, int targetZ) {
        if (!tablero.esCoordenadaValida(targetX, targetY, targetZ)) {
            return false;
        }
        
        Casillero casilleroDestino = tablero.getCasillero(targetX, targetY, targetZ);
        if (casilleroDestino.getTipo() == TipoCasillero.ROCA || 
            casilleroDestino.getTipo() == TipoCasillero.AGUA) {
            return false;
        }
        
        return true; 
    }
    
    /**
     * ¡NUEVO! Revisa el casillero donde el jugador acaba de aterrizar
     * y activa cualquier "evento" (como rampas o trampas).
     */
    private void revisarCasilleroActual(int x, int y, int z) {
        
        Casillero casilleroActual = tablero.getCasillero(x, y, z);

        // --- Lógica de Rampa ---
        if (casilleroActual.getTipo() == TipoCasillero.RAMPA) {
            
            // 1. Intentar SUBIR
            int zArriba = z + 1;
            if (tablero.esCoordenadaValida(x, y, zArriba) && 
                tablero.getCasillero(x, y, zArriba).getTipo() == TipoCasillero.RAMPA) {
                
                System.out.println("¡Subiste al Nivel " + zArriba + "!");
                jugador.setPosicion(x, y, zArriba); // Mueve el modelo
                panelJuego.setNivelZActual(zArriba); // Actualiza la vista
                return; // Importante: termina la función aquí
            }
            
            // 2. Si no pudo subir, intentar BAJAR
            int zAbajo = z - 1;
            if (tablero.esCoordenadaValida(x, y, zAbajo) && 
                tablero.getCasillero(x, y, zAbajo).getTipo() == TipoCasillero.RAMPA) {
                
                System.out.println("¡Bajaste al Nivel " + zAbajo + "!");
                jugador.setPosicion(x, y, zAbajo); // Mueve el modelo
                panelJuego.setNivelZActual(zAbajo); // Actualiza la vista
            }
        }
        
        // (Aquí podríamos agregar else if para TRAMPAS, CARTAS, etc.)
    }
}