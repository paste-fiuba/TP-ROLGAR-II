package com.logica;

import java.awt.event.KeyEvent;
import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;
import com.items.Carta; 
import com.ui.PanelJuego;

public class ControladorJuego {

    private Tablero tablero;
    private Personaje jugador;
    private PanelJuego panelJuego;

    public ControladorJuego(Tablero tablero, Personaje jugador, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.panelJuego = panel;
    }

    public void manejarInput(int keyCode) {
        
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();
        int dx = 0;
        int dy = 0;

        if (keyCode == KeyEvent.VK_W) { dy = -1; }
        else if (keyCode == KeyEvent.VK_S) { dy = 1; }
        else if (keyCode == KeyEvent.VK_A) { dx = -1; }
        else if (keyCode == KeyEvent.VK_D) { dx = 1; }
        else { return; }

        int newX = x + dx;
        int newY = y + dy;

        if (esMovimientoValido(newX, newY, z)) {
            jugador.setPosicion(newX, newY, z);
            revisarCasilleroActual(newX, newY, z); // Revisa el casillero NUEVO
        }
        
        this.panelJuego.repaint();
    }

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
     * Revisa el casillero donde el jugador acaba de aterrizar.
     */
    private void revisarCasilleroActual(int x, int y, int z) {
        
        Casillero casilleroActual = tablero.getCasillero(x, y, z);

        // --- Lógica para recoger cartas ---
        if (casilleroActual.getCarta() != null) {
            Carta cartaRecogida = casilleroActual.getCarta();
            
            // Asumiendo que el inventario tiene un límite de 10
            if (jugador.getInventario().cantidadDeCartas() < 10) {
                jugador.agregarCarta(cartaRecogida);
                casilleroActual.setCarta(null); // ¡Quita la carta del suelo!
                System.out.println("¡Recogiste una carta!"); 
            } else {
                System.out.println("Inventario lleno. No puedes recoger la carta.");
            }
        }
        
        // --- Lógica de Rampa ---
        if (casilleroActual.getTipo() == TipoCasillero.RAMPA) {
            int zArriba = z + 1;
            if (tablero.esCoordenadaValida(x, y, zArriba) && 
                tablero.getCasillero(x, y, zArriba).getTipo() == TipoCasillero.RAMPA) {
                
                System.out.println("¡Subiste al Nivel " + zArriba + "!");
                jugador.setPosicion(x, y, zArriba);
        
                panelJuego.setNivelZActual(zArriba);
                return;
            }
            
            int zAbajo = z - 1;
            if (tablero.esCoordenadaValida(x, y, zAbajo) && 
                tablero.getCasillero(x, y, zAbajo).getTipo() == TipoCasillero.RAMPA) {
                
                System.out.println("¡Bajaste al Nivel " + zAbajo + "!");
                jugador.setPosicion(x, y, zAbajo);
                panelJuego.setNivelZActual(zAbajo);
            }
        }
    }
}