package com.logica;

import java.awt.event.KeyEvent;
import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.ui.PanelJuego;
import java.util.List;

public class ControladorJuego {

    // Enum para manejar el estado del juego
    public enum GameState { RUNNING, PAUSED }
    private GameState estadoJuego;

    private Tablero tablero;
    private Personaje jugador;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;

    public ControladorJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.panelJuego = panel;
        this.estadoJuego = GameState.RUNNING; // El juego empieza corriendo
    }

    /**
     * Procesa todo el input (movimiento, cartas, y ahora pausa).
     */
    public void manejarInput(int keyCode) {
        
        // La tecla ESCAPE siempre funciona
        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (estadoJuego == GameState.RUNNING) {
                estadoJuego = GameState.PAUSED;
            } else if (estadoJuego == GameState.PAUSED) {
                estadoJuego = GameState.RUNNING;
            }
        } 
        
        // Lógica de juego solo si NO está pausado
        else if (estadoJuego == GameState.RUNNING) {
            if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S || 
                keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
                
                procesarMovimiento(keyCode);

            } else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1;
                usarCarta(slotIndex);
            }
        } 
        
        // Lógica del menú de pausa
        else if (estadoJuego == GameState.PAUSED) {
            if (keyCode == KeyEvent.VK_R) { // Reanudar
                estadoJuego = GameState.RUNNING;
            } else if (keyCode == KeyEvent.VK_Q) { // Salir
                System.exit(0);
            }
        }
        
        this.panelJuego.repaint();
    }
    
    /**
     * Devuelve el estado actual del juego para que la Vista sepa qué dibujar.
     */
    public GameState getEstadoJuego() {
        return this.estadoJuego;
    }
    
    private void procesarMovimiento(int keyCode) {
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();
        int dx = 0, dy = 0;

        if (keyCode == KeyEvent.VK_W) { dy = -1; }
        else if (keyCode == KeyEvent.VK_S) { dy = 1; }
        else if (keyCode == KeyEvent.VK_A) { dx = -1; }
        else if (keyCode == KeyEvent.VK_D) { dx = 1; }

        int newX = x + dx;
        int newY = y + dy;

        if (esMovimientoValido(newX, newY, z)) {
            jugador.setPosicion(newX, newY, z);
            revisarCasilleroActual(newX, newY, z);
        }
    }

    private void usarCarta(int slotIndex) {
        if (slotIndex < jugador.getInventario().cantidadDeCartas()) {
            Enemigo objetivo = encontrarEnemigoMasCercano();
            jugador.usarCarta(slotIndex, objetivo);
            jugador.eliminarCarta(slotIndex); 
        } else {
            System.out.println("Slot " + (slotIndex + 1) + " está vacío.");
        }
    }

    private Enemigo encontrarEnemigoMasCercano() {
        Enemigo masCercano = null;
        int menorDistancia = Integer.MAX_VALUE;
        int pX = jugador.getPosX();
        int pY = jugador.getPosY();
        int pZ = jugador.getPosZ();

        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && enemigo.getPosZ() == pZ) {
                int dist = Math.abs(enemigo.getPosX() - pX) + Math.abs(enemigo.getPosY() - pY);
                if (dist < menorDistancia) {
                    menorDistancia = dist;
                    masCercano = enemigo;
                }
            }
        }
        return masCercano;
    }

    private boolean esMovimientoValido(int targetX, int targetY, int targetZ) {
        if (!tablero.esCoordenadaValida(targetX, targetY, targetZ)) return false;
        Casillero c = tablero.getCasillero(targetX, targetY, targetZ);
        if (c.getTipo() == TipoCasillero.ROCA || c.getTipo() == TipoCasillero.AGUA) return false;
        return true; 
    }
    
    private void revisarCasilleroActual(int x, int y, int z) {
        Casillero casilleroActual = tablero.getCasillero(x, y, z);

        if (casilleroActual.getCarta() != null) {
            if (jugador.getInventario().cantidadDeCartas() < 10) {
                jugador.agregarCarta(casilleroActual.getCarta());
                casilleroActual.setCarta(null);
            }
        }
        
        if (casilleroActual.getTipo() == TipoCasillero.RAMPA) {
            int zArriba = z + 1;
            if (tablero.esCoordenadaValida(x, y, zArriba) && 
                tablero.getCasillero(x, y, zArriba).getTipo() == TipoCasillero.RAMPA) {
                jugador.setPosicion(x, y, zArriba);
                panelJuego.setNivelZActual(zArriba);
                return;
            }
            int zAbajo = z - 1;
            if (tablero.esCoordenadaValida(x, y, zAbajo) && 
                tablero.getCasillero(x, y, zAbajo).getTipo() == TipoCasillero.RAMPA) {
                jugador.setPosicion(x, y, zAbajo);
                panelJuego.setNivelZActual(zAbajo);
            }
        }
    }
}