package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.ui.PanelJuego;
import java.util.List;
import java.util.Random;

public class AdministradorDeJuego {

    private Tablero tablero;
    private List<Personaje> jugadores;
    private int indiceJugadorActual = 0;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;
    private Random random;

    public AdministradorDeJuego(Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.enemigos = enemigos;
        this.panelJuego = panel;
        this.random = new Random();
    }
    
    public boolean isJugadorMuerto() {
        // Devuelve true si TODOS los jugadores están muertos
        for (Personaje p : jugadores) {
            if (p.getVida() > 0) return false;
        }
        return true;
    }
    
    /**
     * Nuevo método público que revisa si todos los enemigos están muertos.
     */
    public boolean isVictoria() {
        for (Enemigo e : enemigos) {
            if (e.estaVivo()) {
                return false; // Si encuentra uno vivo, no hay victoria
            }
        }
        return true; // Si el bucle termina, todos están muertos
    }

    /** Devuelve la lista de jugadores (lectura). */
    public List<Personaje> getJugadores() {
        return this.jugadores;
    }

    /**
     * Devuelve el jugador actual (primer jugador vivo empezando en indiceJugadorActual)
     */
    public Personaje getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) return null;
        int size = jugadores.size();
        for (int i = 0; i < size; i++) {
            int idx = (indiceJugadorActual + i) % size;
            Personaje p = jugadores.get(idx);
            if (p.getVida() > 0) {
                indiceJugadorActual = idx;
                return p;
            }
        }
        return null;
    }

    public void procesarMovimiento(int dx, int dy) {
        Personaje jugador = getJugadorActual();
        if (jugador == null) return;
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

    public void activarCarta(int slotIndex) {
        Personaje jugador = getJugadorActual();
        if (jugador == null) return;
        if (slotIndex < jugador.getInventario().cantidadDeCartas()) {
            Carta carta = jugador.getInventario().getCarta(slotIndex);
            logBatalla("¡Has activado '" + carta.getNombre() + "'!");
            jugador.usarCarta(slotIndex, null);
            jugador.eliminarCarta(slotIndex);
        } else {
            logBatalla("Slot " + (slotIndex + 1) + " está vacío.");
        }
    }

    private boolean esMovimientoValido(int targetX, int targetY, int targetZ) {
        if (!tablero.esCoordenadaValida(targetX, targetY, targetZ)) return false;
        Casillero c = tablero.getCasillero(targetX, targetY, targetZ);
        if (c.getTipo() == TipoCasillero.ROCA || c.getTipo() == TipoCasillero.AGUA) return false;
        return true; 
    }

    private void revisarCasilleroActual(int x, int y, int z) {
        Personaje jugador = getJugadorActual();
        Enemigo enemigoEnCasilla = enemigoEnPosicion(x, y, z);
        if (enemigoEnCasilla != null && enemigoEnCasilla.estaVivo()) {
            ejecutarCombate(jugador, enemigoEnCasilla);
            return;
        }
        
        Casillero casilleroActual = tablero.getCasillero(x, y, z);
        if (casilleroActual.getCarta() != null) {
            if (jugador.getInventario().cantidadDeCartas() < 10) {
                jugador.agregarCarta(casilleroActual.getCarta());
                casilleroActual.setCarta(null);
                logBatalla("¡Recogiste una carta!");
            }
            return;
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

    private void ejecutarCombate(Personaje p, Enemigo e) {
        int dmgJugador;
        
        if (p.isAtaqueDobleActivo()) {
            logBatalla("¡ATAQUE DOBLE!");
            int dmgBase = (p.getFuerza() / 2) + random.nextInt(p.getFuerza());
            dmgJugador = dmgBase * 2;
            p.setAtaqueDobleActivo(false);
        } else {
            dmgJugador = (p.getFuerza() / 2) + random.nextInt(p.getFuerza());
        }

        int dmgEnemigo = (e.getFuerza() / 2) + random.nextInt(e.getFuerza());

        e.recibirDanio(dmgJugador);
        p.recibirDanio(dmgEnemigo);
        
        logBatalla(p.getNombre() + " golpea a " + e.getNombre() + " por " + dmgJugador + "!");
        logBatalla(e.getNombre() + " responde por " + dmgEnemigo + "!");
        
        if (p.getVida() <= 0) {
            logBatalla("¡HAS SIDO DERROTADO!");
        }
        if (!e.estaVivo()) {
            logBatalla("¡Has derrotado a " + e.getNombre() + "!");
        }
    }

    /**
     * Finaliza el turno del jugador actual: procesa los efectos de los enemigos
     * (Opción A: enemigos actúan tras cada turno de jugador) y avanza al siguiente jugador vivo.
     */
    public void finalizarTurno() {
        // Procesar acciones simples de enemigos
        procesarTurnoEnemigos();

        // Avanzar al siguiente jugador vivo
        int size = jugadores.size();
        for (int i = 1; i <= size; i++) {
            int next = (indiceJugadorActual + i) % size;
            if (jugadores.get(next).getVida() > 0) {
                indiceJugadorActual = next;
                break;
            }
        }
        // Actualizar la vista del panel para que muestre el nivel (Z) del jugador activo
        Personaje actual = getJugadorActual();
        if (actual != null && panelJuego != null) {
            panelJuego.setNivelZActual(actual.getPosZ());
        }
    }

    /**
     * Comportamiento mínimo de los enemigos: si están adyacentes atacan al jugador más cercano,
     * de lo contrario no se mueven (implementación simple para empezar).
     */
    private void procesarTurnoEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;
            Personaje objetivo = encontrarJugadorMasCercanoA(enemigo);
            if (objetivo == null) continue;
            if (enemigo.getPosZ() != objetivo.getPosZ()) continue;

            int dist = Math.abs(enemigo.getPosX() - objetivo.getPosX()) + Math.abs(enemigo.getPosY() - objetivo.getPosY());
            if (dist <= 1) {
                int dmg = (enemigo.getFuerza() / 2) + random.nextInt(enemigo.getFuerza());
                objetivo.recibirDanio(dmg);
                logBatalla(enemigo.getNombre() + " ataca a " + objetivo.getNombre() + " por " + dmg + "!");
                if (objetivo.getVida() <= 0) {
                    logBatalla(objetivo.getNombre() + " ha muerto!");
                }
            }
        }
    }

    private Personaje encontrarJugadorMasCercanoA(Enemigo enemigo) {
        Personaje masCercano = null;
        int menor = Integer.MAX_VALUE;
        for (Personaje p : jugadores) {
            if (p.getVida() <= 0) continue;
            int dist = Math.abs(enemigo.getPosX() - p.getPosX()) + Math.abs(enemigo.getPosY() - p.getPosY());
            if (dist < menor) {
                menor = dist;
                masCercano = p;
            }
        }
        return masCercano;
    }
    
    private void logBatalla(String mensaje) {
        if (panelJuego != null && panelJuego.getRenderUI() != null) {
            panelJuego.getRenderUI().agregarMensajeLog(mensaje);
        }
    }
    
    public void limpiarLogCombate() {
        if (panelJuego != null && panelJuego.getRenderUI() != null) {
            panelJuego.getRenderUI().limpiarLog();
        }
    }

    private Enemigo enemigoEnPosicion(int x, int y, int z) {
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

    private Enemigo encontrarEnemigoMasCercano() {
        Enemigo masCercano = null;
        int menorDistancia = Integer.MAX_VALUE;
        Personaje jugadorRef = getJugadorActual();
        if (jugadorRef == null) return null;
        int pX = jugadorRef.getPosX();
        int pY = jugadorRef.getPosY();
        int pZ = jugadorRef.getPosZ();

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