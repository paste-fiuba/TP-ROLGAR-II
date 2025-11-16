	package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.ui.PanelJuego;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdministradorDeJuego {

    private ControladorJuego controlador; 
    private Tablero tablero;
    private List<Personaje> jugadores;
<<<<<<< Updated upstream
    private List<Enemigo> enemigos;
=======
    private int indiceJugadorActual = 0;
    private EnemigoManager enemigoManager;
>>>>>>> Stashed changes
    private PanelJuego panelJuego;
    private Random random;
    private List<String> jugadoresEliminados;

    // --- MÓDULOS DELEGADOS ---
    private AdministradorDeAlianzas adminAlianzas;
    private AdministradorDeEnemigos adminEnemigos; 
    private AdministradorDePersonaje adminPersonaje; 
    private Turno turno; 

    public AdministradorDeJuego(ControladorJuego controlador, Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, PanelJuego panel) {
        this.controlador = controlador; 
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.enemigoManager = new EnemigoManager(enemigos);
        this.panelJuego = panel;
        this.random = new Random();
        this.jugadoresEliminados = new ArrayList<>();
        
        this.adminAlianzas = new AdministradorDeAlianzas(this::logBatalla);
        this.adminEnemigos = new AdministradorDeEnemigos(this.enemigos, this.controlador);
        this.adminPersonaje = new AdministradorDePersonaje(this.tablero, this.enemigos, this.panelJuego, this::logBatalla);
        this.turno = new Turno(); 
    }
    
    // --- Métodos de Estado y Consulta (sin cambios) ---
    
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
        for (Personaje p : jugadores) {
            if (p.getVida() > 0) return false;
        }
        return true;
    }
    
    public boolean isVictoria() {
        return enemigoManager == null || enemigoManager.allDead();
    }

    public List<Personaje> getJugadores() {
        return this.jugadores;
    }
    
    public Tablero getTablero() {
        return this.tablero;
    }

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

    // --- Métodos de Acción Delegados (sin cambios) ---

    public boolean procesarMovimiento(int dx, int dy) {
        return adminPersonaje.procesarMovimiento(getJugadorActual(), dx, dy, this.controlador);
    }

    public void activarCarta(int slotIndex) {
        adminPersonaje.activarCarta(getJugadorActual(), slotIndex, this.jugadores);
    }

    public void atacarJugador(Personaje atacante, Personaje objetivo) {
        adminPersonaje.atacarJugador(atacante, objetivo, this.controlador);
    }

    public void proponerAlianza(Personaje proponente, Personaje objetivo) {
        adminAlianzas.proponerAlianza(proponente, objetivo, this.tablero);
    }

    public Personaje getPropuestaPara(Personaje target) {
        return adminAlianzas.getPropuestaPara(target);
    }

    public void aceptarPropuesta(Personaje target) {
        adminAlianzas.aceptarPropuesta(target);
    }

    public void rechazarPropuesta(Personaje target) {
        adminAlianzas.rechazarPropuesta(target);
    }

    public boolean transferirCarta(Personaje from, Personaje to, int slotIndex) {
        return adminAlianzas.transferirCarta(from, to, slotIndex, this.tablero);
    }
    
    private void procesarRupturaAlianzas() {
        adminAlianzas.procesarRupturaAlianzas();
    }

    // --- Lógica de Turno (sin cambios) ---

    public void finalizarTurno() {
        actualizarVisibilidad(); 
        
        adminEnemigos.procesarTurnos(this.jugadores);
        procesarRupturaAlianzas(); 

        if (jugadores == null || jugadores.isEmpty()) return;
<<<<<<< Updated upstream
=======
        int size = jugadores.size();
        if (size == 0) return; 
        
        for (int i = 1; i <= size; i++) {
            int next = (indiceJugadorActual + i) % size;
            if (jugadores.get(next).getVida() > 0) {
                indiceJugadorActual = next;
                break;
            }
        }
        Personaje actual = getJugadorActual();
        if (actual != null && panelJuego != null) {
            panelJuego.setNivelZActual(actual.getPosZ());
        }
    }

    private void procesarTurnoEnemigos() {
        if (enemigoManager == null || enemigoManager.getEnemigos() == null) return;
        for (Enemigo enemigo : enemigoManager.getEnemigos()) {
            if (!enemigo.estaVivo()) continue;
            Personaje objetivo = encontrarJugadorMasCercanoA(enemigo);
            if (objetivo == null) continue;
            if (enemigo.getPosZ() != objetivo.getPosZ()) continue;

            int dist = Math.abs(enemigo.getPosX() - objetivo.getPosX()) + Math.abs(enemigo.getPosY() - objetivo.getPosY());
            if (dist <= 1) {
                // --- MODIFICADO ---
                controlador.iniciarCombate(objetivo, enemigo);
                break; // Un solo enemigo ataca por turno
            }
        }
>>>>>>> Stashed changes
        
        if (jugadores != null) {
            for (Personaje p : jugadores) {
                if (p != null && p.isInvisible()) {
                    p.setInvisible(false);
                    logBatalla("La invisibilidad de " + p.getNombre() + " ha expirado.");
                }
            }
        }
        
        eliminarJugadoresMuertos(); 
        if (jugadores.isEmpty()) return; 

        turno.nextPlayer(jugadores.size());
        
        Personaje actual = getJugadorActual(); 
        if (actual != null && panelJuego != null) {
            panelJuego.setNivelZActual(actual.getPosZ());
        }
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
                if (idx < turno.getIndiceJugadorActual()) { 
                    newIndex = Math.max(0, newIndex - 1); 
                }
            } else {
                idx++;
            }
        }
        
        if (removed) {
            turno.setIndiceJugadorActual((jugadores.isEmpty()) ? 0 : Math.min(newIndex, jugadores.size() - 1));
        }
    }

    // --- Métodos de Utilidad (Log, Buscadores) ---
    
    public List<String> getJugadoresEliminados() {
        return new ArrayList<>(this.jugadoresEliminados);
    }
    
    public void logBatalla(String mensaje) {
        if (panelJuego != null && panelJuego.getRenderUI() != null) {
            panelJuego.getRenderUI().agregarMensajeLog(mensaje);
        }
    }
    
    public void limpiarLogCombate() {
        if (panelJuego != null && panelJuego.getRenderUI() != null) {
            panelJuego.getRenderUI().limpiarLog();
        }
    }

<<<<<<< Updated upstream
    public Enemigo encontrarEnemigoMasCercano() {
=======
    private Enemigo enemigoEnPosicion(int x, int y, int z) {
        if (enemigoManager == null || enemigoManager.getEnemigos() == null) return null;
        for (Enemigo enemigo : enemigoManager.getEnemigos()) {
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
>>>>>>> Stashed changes
        Enemigo masCercano = null;
        int menorDistancia = Integer.MAX_VALUE;
        Personaje jugadorRef = getJugadorActual();
        if (jugadorRef == null) return null;
        int pX = jugadorRef.getPosX();
        int pY = jugadorRef.getPosY();
        int pZ = jugadorRef.getPosZ();

        if (enemigoManager == null || enemigoManager.getEnemigos() == null) return null;
        for (Enemigo enemigo : enemigoManager.getEnemigos()) {
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
<<<<<<< Updated upstream
    
    // ---
    // --- ¡NUEVOS MÉTODOS PARA ARREGLAR LA UI! ---
    // ---
    
    /**
     * Verifica si dos personajes son adyacentes (no diagonal).
     * Este método es usado por la UI (RenderizarData) y los comandos (ControladorJuego).
     */
    public boolean sonAdyacentes(Personaje a, Personaje b) {
        if (a == null || b == null) return false;
        if (a.getPosZ() != b.getPosZ()) return false;
        // Chequeo de distancia Manhattan
        int dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
        return dist == 1;
    }
    
    /**
     * Busca un jugador adyacente al que se le pueda proponer alianza.
     * Usado por RenderizarData para saber si debe dibujar el prompt "[L]".
     */
    public Personaje getJugadorAdyacenteParaAlianza(Personaje jugadorActual) {
        if (jugadores == null) return null;
        for (Personaje otro : jugadores) {
            if (otro == null || otro == jugadorActual || otro.getVida() <= 0) continue;
            // Si no está aliado Y es adyacente
            if (!jugadorActual.estaAliadoCon(otro) && sonAdyacentes(jugadorActual, otro)) {
                return otro; // Devuelve el primer candidato encontrado
=======

    /** Marca un enemigo como derrotado en el manager. */
    public void marcarEnemigoDerrotado(Enemigo e) {
        if (e == null || enemigoManager == null) return;
        enemigoManager.markDead(e);
        // opcional: remover inmediatamente los muertos de la colección
        enemigoManager.removeDead();
    }

    private void procesarRupturaAlianzas() {
        if (alianzas == null || alianzas.isEmpty()) return;

        java.util.List<com.entidades.Alianza> copia = new java.util.ArrayList<>(alianzas);
        for (com.entidades.Alianza al : copia) {
            if (al == null) continue;
            if (random.nextDouble() < PROB_RUPTURA_ALIANZA) {
                java.util.List<Personaje> miembros = al.getMiembros();
                if (miembros == null || miembros.isEmpty()) {
                    alianzas.remove(al);
                    continue;
                }
                int idx = random.nextInt(miembros.size());
                Personaje expulsado = miembros.get(idx);
                if (expulsado != null) {
                    al.eliminarMiembro(expulsado);
                    logBatalla("La alianza '" + al.toString() + "' se rompió: " + expulsado.getNombre() + " fue expulsado.");
                }
                java.util.List<Personaje> despues = al.getMiembros();
                if (despues.size() <= 1) {
                    for (Personaje p : despues) {
                        if (p != null) p.setAlianza(null);
                    }
                    alianzas.remove(al);
                    logBatalla("La alianza se disolvió por falta de miembros.");
                }
>>>>>>> Stashed changes
            }
        }
        return null; // No hay nadie
    }
    
    /**
     * Busca un jugador adyacente al que se pueda atacar.
     * Usado por RenderizarData para saber si debe dibujar el prompt "[F]".
     */
    public Personaje getJugadorAdyacenteParaAtacar(Personaje jugadorActual) {
        if (jugadores == null) return null;
        for (Personaje otro : jugadores) {
            if (otro == null || otro == jugadorActual || otro.getVida() <= 0) continue;
            // Si es adyacente (no importa si es aliado o no)
            if (sonAdyacentes(jugadorActual, otro)) {
                return otro; // Devuelve el primer candidato encontrado
            }
        }
        return null; // No hay nadie
    }
}