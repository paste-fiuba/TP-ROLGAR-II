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

/**
 * El "Controlador". Conecta el input (teclado) con el Modelo (jugador, tablero)
 * y le avisa a la Vista (panel) cuándo debe redibujar.
 */
public class ControladorJuego {

    private Tablero tablero;
    private Personaje jugador;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;

    /**
     * Constructor que recibe el Modelo (tablero, jugador, enemigos) y la Vista (panel).
     */
    public ControladorJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.panelJuego = panel;
    }

    /**
     * Procesa todo el input (movimiento y uso de cartas).
     */
    public void manejarInput(int keyCode) {
        
        // Lógica de Movimiento
        if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_S || 
            keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
            
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
        // Lógica de Uso de Cartas
        else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
            
            // VK_1 es 0, VK_2 es 1, ..., VK_0 es 9
            int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1;
            
            usarCarta(slotIndex);
        }
        
        // Redibuja la pantalla después de cualquier acción
        this.panelJuego.repaint();
    }

    /**
     * Lógica para usar una carta del inventario y luego eliminarla.
     */
    private void usarCarta(int slotIndex) {
        if (slotIndex < jugador.getInventario().cantidadDeCartas()) {
            
            Enemigo objetivo = encontrarEnemigoMasCercano();
            
            System.out.println("Usando carta del slot " + (slotIndex + 1) + "...");
            
            // 1. Usa la carta (aplicando su efecto)
            jugador.usarCarta(slotIndex, objetivo);
            
            // 2. Elimina la carta del inventario
            jugador.eliminarCarta(slotIndex); 

        } else {
            System.out.println("Slot " + (slotIndex + 1) + " está vacío.");
        }
    }

    /**
     * Busca al enemigo vivo más cercano EN EL MISMO PISO que el jugador.
     */
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

    /**
     * Verifica si el personaje puede moverse a la casilla destino.
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
     * Revisa el casillero donde el jugador acaba de aterrizar.
     */
    private void revisarCasilleroActual(int x, int y, int z) {
        Casillero casilleroActual = tablero.getCasillero(x, y, z);

        // Lógica para recoger cartas
        if (casilleroActual.getCarta() != null) {
            Carta cartaRecogida = casilleroActual.getCarta();
            if (jugador.getInventario().cantidadDeCartas() < 10) {
                jugador.agregarCarta(cartaRecogida);
                casilleroActual.setCarta(null);
                System.out.println("¡Recogiste una carta!"); 
            } else {
                System.out.println("Inventario lleno.");
            }
        }
        
        // Lógica de Rampa
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