package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.ui.PanelJuego;
import java.util.List;
import java.util.function.Consumer; 

/**
 * TDA para gestionar las acciones específicas de un jugador 
 */
public class AdministradorDePersonaje {

    private Tablero tablero;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;
    private Consumer<String> logger;

    public AdministradorDePersonaje(Tablero tablero, List<Enemigo> enemigos, PanelJuego panelJuego,
            Consumer<String> logger) {
        this.tablero = tablero;
        this.enemigos = enemigos;
        this.panelJuego = panelJuego;
        this.logger = logger;
    }

    /**
     * Procesa el intento de movimiento de un jugador.
     * Devuelve true si el movimiento fue exitoso.
     */
    public boolean procesarMovimiento(Personaje jugador, int dx, int dy, ControladorJuego controlador) {
        if (jugador == null)
            return false;
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();
        int newX = x + dx;
        int newY = y + dy;

        if (esMovimientoValido(newX, newY, z)) {
            jugador.setPosicion(newX, newY, z);
            revisarCasilleroActual(jugador, newX, newY, z, controlador);
            return true;
        }
        return false;
    }

    /**
     * Procesa el intento de un jugador de activar una carta.
     */
    public void activarCarta(Personaje jugador, int slotIndex, List<Personaje> todosLosJugadores) {
        if (jugador == null)
            return;
        

        Carta carta = jugador.getInventario().getCarta(slotIndex);


        if (carta != null) {
            logger.accept("¡Has activado '" + carta.getNombre() + "'!");

            if (carta instanceof com.items.CartaRoboDeCarta) {
                Personaje objetivo = null;
                for (Personaje p : todosLosJugadores) {
                    if (p == null || p == jugador || p.getVida() <= 0)
                        continue;
                    if (sonAdyacentes(jugador, p)) { 
                        objetivo = p;
                        break;
                    }
                }
                if (objetivo != null) {
                    jugador.usarCarta(slotIndex, objetivo);
                } else {
                    logger.accept("No hay ningún jugador adyacente para robarle una carta.");
                }
            } else {
                jugador.usarCarta(slotIndex, null);
            }
            
            jugador.eliminarCarta(slotIndex);
            
        } else {
            logger.accept("Slot " + (slotIndex + 1) + " está vacío.");
        }
    }

    /**
     * Inicia el combate de un jugador contra otro.
     */
    public void atacarJugador(Personaje atacante, Personaje objetivo, ControladorJuego controlador) {
        if (atacante == null || objetivo == null)
            return;
        if (!sonAdyacentes(atacante, objetivo)) {
            logger.accept("No estás lo suficientemente cerca para atacar a " + objetivo.getNombre());
            return;
        }
        controlador.iniciarCombate(atacante, objetivo);
    }

    /**
     * Verifica si un movimiento a (targetX, targetY, targetZ) es válido.
     */
    private boolean esMovimientoValido(int targetX, int targetY, int targetZ) {
        if (tablero == null || !tablero.esCoordenadaValida(targetX, targetY, targetZ))
            return false;
        Casillero c = tablero.getCasillero(targetX, targetY, targetZ);
        if (c.getTipo() == TipoCasillero.ROCA || c.getTipo() == TipoCasillero.AGUA)
            return false;
        return true;
    }

    /**
     * Revisa la casilla donde el jugador acaba de aterrizar.
     */
    private void revisarCasilleroActual(Personaje jugador, int x, int y, int z, ControladorJuego controlador) {
        if (jugador == null || tablero == null)
            return;


        Enemigo enemigoEnCasilla = enemigoEnPosicion(x, y, z);
        if (enemigoEnCasilla != null && enemigoEnCasilla.estaVivo()) {
            controlador.iniciarCombate(jugador, enemigoEnCasilla);
            return;
        }


        Casillero casilleroActual = tablero.getCasillero(x, y, z);
        if (casilleroActual.getCarta() != null) {
            if (jugador.getInventario().cantidadDeCartas() < 10) { 
                jugador.agregarCarta(casilleroActual.getCarta()); 
                casilleroActual.setCarta(null);
                logger.accept("¡Recogiste una carta!");
            } else {
                logger.accept("¡Inventario lleno! No puedes recoger la carta.");
            }
            return;
        }


        if (casilleroActual.getTipo() == TipoCasillero.RAMPA) {
            int zArriba = z + 1;
            if (tablero.esCoordenadaValida(x, y, zArriba) &&
                    tablero.getCasillero(x, y, zArriba).getTipo() == TipoCasillero.RAMPA) {
                jugador.setPosicion(x, y, zArriba);
                panelJuego.setNivelZActual(zArriba);
                logger.accept("Subiste al nivel " + zArriba);
                return;
            }
            int zAbajo = z - 1;
            if (tablero.esCoordenadaValida(x, y, zAbajo) &&
                    tablero.getCasillero(x, y, zAbajo).getTipo() == TipoCasillero.RAMPA) {
                jugador.setPosicion(x, y, zAbajo);
                panelJuego.setNivelZActual(zAbajo);
                logger.accept("Bajaste al nivel " + zAbajo);
            }
        }
    }

    /**
     * Devuelve un enemigo en la posición (x, y, z) o null si no hay ninguno.
     */
    private Enemigo enemigoEnPosicion(int x, int y, int z) {
        if (enemigos == null)
            return null;
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() &&
                    enemigo.getPosX() == x &&
                    enemigo.getPosY() == y &&
                    enemigo.getPosZ() == z) {
                return enemigo;
            }
        }
        return null;
    }

    /**
     * Verifica adyacencia (para robar cartas o atacar).
     */
    private boolean sonAdyacentes(Personaje a, Personaje b) {
        if (a == null || b == null)
            return false;
        if (a.getPosZ() != b.getPosZ())
            return false;
        int dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
        return dist == 1;
    }
}