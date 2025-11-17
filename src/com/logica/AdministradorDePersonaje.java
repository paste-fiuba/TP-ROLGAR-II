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
    /**
     * pre: jugador != null.
     * post: procesa el movimiento y devuelve true si fue válido.
     */
    public boolean procesarMovimiento(Personaje jugador, int dx, int dy, ControladorJuego controlador) {

        boolean resultado;
        boolean jugadorValido;
        int x;
        int y;
        int z;
        int newX;
        int newY;
        boolean movimientoValido;

        resultado = false;
        jugadorValido = (jugador != null);
        x = 0;
        y = 0;
        z = 0;
        newX = 0;
        newY = 0;
        movimientoValido = false;

        if (jugadorValido) {

            x = jugador.getPosX();
            y = jugador.getPosY();
            z = jugador.getPosZ();
            newX = x + dx;
            newY = y + dy;

            movimientoValido = esMovimientoValido(newX, newY, z);

            if (movimientoValido) {
                jugador.setPosicion(newX, newY, z);
                revisarCasilleroActual(jugador, newX, newY, z, controlador);
                resultado = true;
            } else {
                resultado = false;
            }

        } else {
            resultado = false;
        }

        return resultado;
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
     * pre: enemigos != null.
     * post: devuelve el enemigo vivo en la posición indicada o null.
     */
    private Enemigo enemigoEnPosicion(int x, int y, int z) {

        Enemigo resultado;
        int i;
        int n;
        Enemigo e;
        boolean encontrado;
        boolean vivo;

        resultado = null;
        i = 0;
        n = 0;
        e = null;
        encontrado = false;
        vivo = false;

        if (enemigos != null) {

            n = enemigos.size();

            while (i < n && !encontrado) {

                e = enemigos.get(i);

                if (e != null) {
                    vivo = e.estaVivo();
                    if (vivo) {
                        if (e.getPosX() == x && e.getPosY() == y && e.getPosZ() == z) {
                            resultado = e;
                            encontrado = true;
                        }
                    }
                }

                i++;
            }
        }

        return resultado;
    }

    /**
     * Verifica adyacencia (para robar cartas o atacar).
     */
    /**
     * pre: a != null y b != null.
     * post: devuelve true si están a distancia 1 en el mismo nivel.
     */
    private boolean sonAdyacentes(Personaje a, Personaje b) {

        boolean resultado;
        boolean datosValidos;
        boolean mismoZ;
        int dist;

        resultado = false;
        datosValidos = (a != null && b != null);
        mismoZ = false;
        dist = 0;

        if (datosValidos) {
            mismoZ = (a.getPosZ() == b.getPosZ());
            if (mismoZ) {
                dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
                resultado = (dist == 1);
            }
        }

        return resultado;
    }
}