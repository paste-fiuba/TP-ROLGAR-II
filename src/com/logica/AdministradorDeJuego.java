package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.tablero.Tablero;
import com.ui.PanelJuego;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Administrador central del juego. Esta versión mantiene una lista de enemigos
 * (compatibilidad con los demás administradores) y expone un método para marcar
 * enemigos derrotados.
 */
public class AdministradorDeJuego {

    private ControladorJuego controlador;
    private Tablero tablero;
    private List<Personaje> jugadores;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;
    private Random random;
    private List<String> jugadoresEliminados;
    private AdministradorDeAlianzas adminAlianzas;
    private AdministradorDeEnemigos adminEnemigos;
    private AdministradorDePersonaje adminPersonaje;
    private Turno turno;

    public AdministradorDeJuego(ControladorJuego controlador, Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, PanelJuego panel) {
        this.controlador = controlador;
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.enemigos = (enemigos == null) ? new ArrayList<>() : enemigos;
        this.panelJuego = panel;
        this.random = new Random();
        this.jugadoresEliminados = new ArrayList<>();

        this.adminAlianzas = new AdministradorDeAlianzas(this::logBatalla);
        this.adminEnemigos = new AdministradorDeEnemigos(this.enemigos, this.controlador);
        this.adminPersonaje = new AdministradorDePersonaje(this.tablero, this.enemigos, this.panelJuego, this::logBatalla);
        this.turno = new Turno();
    }

    public void actualizarVisibilidad() {
        Personaje jugador = getJugadorActual();
        if (jugador == null) return;
        int pX = jugador.getPosX();
        int pY = jugador.getPosY();
        int pZ = jugador.getPosZ();
        int vision = jugador.getVision();
        for (int x = pX - vision; x <= pX + vision; x++) {
            for (int y = pY - vision; y <= pY + vision; y++) {
                if (tablero.esCoordenadaValida(x, y, pZ)) {
                    tablero.getCasillero(x, y, pZ).marcarVisitado();
                }
            }
        }
    }

    public boolean isJugadorMuerto() {
        if (jugadores == null || jugadores.isEmpty()) return true;
        for (Personaje p : jugadores) if (p.getVida() > 0) return false;
        return true;
    }

    public boolean isVictoria() {
        if (enemigos == null) return true;
        for (Enemigo e : enemigos) if (e != null && e.estaVivo()) return false;
        return true;
    }

    public List<Personaje> getJugadores() { return this.jugadores; }
    public Tablero getTablero() { return this.tablero; }

    public Personaje getJugadorActual() {
        if (jugadores == null || jugadores.isEmpty()) return null;
        int size = jugadores.size();
        for (int i = 0; i < size; i++) {
            int idx = (turno.getIndiceJugadorActual() + i) % size;
            Personaje p = jugadores.get(idx);
            if (p.getVida() > 0) {
                turno.setIndiceJugadorActual(idx);
                return p;
            }
        }
        return null;
    }

    // Delegados de acciones
    public boolean procesarMovimiento(int dx, int dy) {
        return adminPersonaje.procesarMovimiento(getJugadorActual(), dx, dy, this.controlador);
    }

    public void activarCarta(int slotIndex) { adminPersonaje.activarCarta(getJugadorActual(), slotIndex, this.jugadores); }
    public void atacarJugador(Personaje atacante, Personaje objetivo) { adminPersonaje.atacarJugador(atacante, objetivo, this.controlador); }
    public void proponerAlianza(Personaje proponente, Personaje objetivo) { adminAlianzas.proponerAlianza(proponente, objetivo, this.tablero); }
    public Personaje getPropuestaPara(Personaje target) { return adminAlianzas.getPropuestaPara(target); }
    public void aceptarPropuesta(Personaje target) { adminAlianzas.aceptarPropuesta(target); }
    public void rechazarPropuesta(Personaje target) { adminAlianzas.rechazarPropuesta(target); }
    public boolean transferirCarta(Personaje from, Personaje to, int slotIndex) { return adminAlianzas.transferirCarta(from, to, slotIndex, this.tablero); }

    public void finalizarTurno() {
        actualizarVisibilidad();
        adminEnemigos.procesarTurnos(this.jugadores);
        adminAlianzas.procesarRupturaAlianzas();

        if (jugadores != null) {
            for (Personaje p : jugadores) {
                if (p != null && p.isInvisible()) {
                    p.setInvisible(false);
                    logBatalla("La invisibilidad de " + p.getNombre() + " ha expirado.");
                }
            }
        }

        eliminarJugadoresMuertos();
        if (jugadores == null || jugadores.isEmpty()) return;

        turno.nextPlayer(jugadores.size());
        Personaje actual = getJugadorActual();
        if (actual != null && panelJuego != null) panelJuego.setNivelZActual(actual.getPosZ());
    }

    private void eliminarJugadoresMuertos() {
        if (jugadores == null || jugadores.isEmpty()) return;
        boolean removed = false;
        java.util.Iterator<Personaje> it = jugadores.iterator();
        int idx = 0;
        int newIndex = turno.getIndiceJugadorActual();

        while (it.hasNext()) {
            Personaje p = it.next();
            if (p.getVida() <= 0) {
                it.remove();
                logBatalla(p.getNombre() + " fue eliminado de la partida por muerte.");
                jugadoresEliminados.add(p.getNombre());
                removed = true;
                if (idx < turno.getIndiceJugadorActual()) newIndex = Math.max(0, newIndex - 1);
            } else idx++;
        }

        if (removed) turno.setIndiceJugadorActual((jugadores.isEmpty()) ? 0 : Math.min(newIndex, jugadores.size() - 1));
    }

    // Logging
    public List<String> getJugadoresEliminados() { return new ArrayList<>(this.jugadoresEliminados); }
    public void logBatalla(String mensaje) { if (panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().agregarMensajeLog(mensaje); }
    public void limpiarLogCombate() { if (panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().limpiarLog(); }

    // Buscadores
    private Enemigo enemigoEnPosicion(int x, int y, int z) {
        if (enemigos == null) return null;
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && enemigo.getPosX() == x && enemigo.getPosY() == y && enemigo.getPosZ() == z) return enemigo;
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
        if (enemigos == null) return null;
        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && enemigo.getPosZ() == pZ) {
                int dist = Math.abs(enemigo.getPosX() - pX) + Math.abs(enemigo.getPosY() - pY);
                if (dist < menorDistancia) { menorDistancia = dist; masCercano = enemigo; }
            }
        }
        return masCercano;
    }

    public boolean sonAdyacentes(Personaje a, Personaje b) {
        if (a == null || b == null) return false;
        if (a.getPosZ() != b.getPosZ()) return false;
        int dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
        return dist == 1;
    }

    public Personaje getJugadorAdyacenteParaAlianza(Personaje jugadorActual) {
        if (jugadores == null) return null;
        for (Personaje otro : jugadores) {
            if (otro == null || otro == jugadorActual || otro.getVida() <= 0) continue;
            if (!jugadorActual.estaAliadoCon(otro) && sonAdyacentes(jugadorActual, otro)) return otro;
        }
        return null;
    }

    public Personaje getJugadorAdyacenteParaAtacar(Personaje jugadorActual) {
        if (jugadores == null) return null;
        for (Personaje otro : jugadores) {
            if (otro == null || otro == jugadorActual || otro.getVida() <= 0) continue;
            if (sonAdyacentes(jugadorActual, otro)) return otro;
        }
        return null;
    }

    /** Marca un enemigo como derrotado y lo notifica en el log. */
    public void marcarEnemigoDerrotado(Enemigo e) {
        if (e == null) return;
        e.setVida(0);
        e.recibirDanio(0); // asegurarse que estado 'vivo' se actualice
        logBatalla("El enemigo " + e.getNombre() + " fue derrotado.");
    }
}