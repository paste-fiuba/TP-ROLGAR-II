package com.logica;

import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.ui.PanelJuego;
import java.util.List;

/**
 * El "Motor de Lógica". Este TDA conoce todas las reglas del juego.
 */
public class AdministradorDeJuego {

    private Tablero tablero;
    private Personaje jugador;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;

    public AdministradorDeJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.panelJuego = panel;
    }

    public void procesarMovimiento(int dx, int dy) {
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();
        int newX = x + dx;
        int newY = y + dy;

        if (esMovimientoValido(newX, newY, z)) {
            jugador.setPosicion(newX, newY, z);
            revisarCasilleroActual(newX, newY, z);
        }
    }

    public void usarCarta(int slotIndex) {
        if (slotIndex < jugador.getInventario().cantidadDeCartas()) {
            Enemigo objetivo = encontrarEnemigoMasCercano();
            jugador.usarCarta(slotIndex, objetivo);
            jugador.eliminarCarta(slotIndex); 
        }
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
}
