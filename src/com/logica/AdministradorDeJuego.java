package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.ui.PanelJuego;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AdministradorDeJuego {

    private ControladorJuego controlador; 
    private Tablero tablero;
    private List<Personaje> jugadores;
    private int indiceJugadorActual = 0;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;
    private Random random;
    private List<com.entidades.Alianza> alianzas;
    private Map<Personaje, Personaje> propuestas;
    private static final double PROB_RUPTURA_ALIANZA = 0.15;
    private List<String> jugadoresEliminados;

    public AdministradorDeJuego(ControladorJuego controlador, Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, PanelJuego panel) {
        this.controlador = controlador; 
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.enemigos = enemigos;
        this.panelJuego = panel;
        this.random = new Random();
        this.alianzas = new ArrayList<>();
        this.propuestas = new HashMap<>();
        this.jugadoresEliminados = new ArrayList<>();
    }
    
    // --- MÉTODO NUEVO ---
    /**
     * pre: -
     * post: Marca como "visitados" todos los casilleros alrededor del jugador
     * actual, según su rango de visión.
     */
    public void actualizarVisibilidad() {
        Personaje jugador = getJugadorActual();
        if (jugador == null) return;
        
        int pX = jugador.getPosX();
        int pY = jugador.getPosY();
        int pZ = jugador.getPosZ();
        int vision = jugador.getVision(); // Asumimos 1 por defecto

        for (int x = pX - vision; x <= pX + vision; x++) {
            for (int y = pY - vision; y <= pY + vision; y++) {
                // Chequea que la coordenada esté dentro del tablero
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
        if (enemigos == null || enemigos.isEmpty()) return true; 
        for (Enemigo e : enemigos) {
            if (e.estaVivo()) {
                return false;
            }
        }
        return true;
    }

    public List<Personaje> getJugadores() {
        return this.jugadores;
    }
    
    // --- NUEVO GETTER PARA EL RENDERIZADOR ---
    public Tablero getTablero() {
        return this.tablero;
    }

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
    
    public boolean procesarMovimiento(int dx, int dy) {
        Personaje jugador = getJugadorActual();
        if (jugador == null) return false;
        int x = jugador.getPosX();
        int y = jugador.getPosY();
        int z = jugador.getPosZ();
        int newX = x + dx;
        int newY = y + dy;

        if (esMovimientoValido(newX, newY, z)) {
            jugador.setPosicion(newX, newY, z);
            revisarCasilleroActual(jugador, newX, newY, z);
            return true;
        }
        return false;
    }

    public void activarCarta(int slotIndex) {
        Personaje jugador = getJugadorActual();
        if (jugador == null) return;
        if (slotIndex < jugador.getInventario().cantidadDeCartas()) {
            Carta carta = jugador.getInventario().getCarta(slotIndex);
            logBatalla("¡Has activado '" + carta.getNombre() + "'!");
            if (carta instanceof com.items.CartaRoboDeCarta) {
                Personaje objetivo = null;
                for (Personaje p : jugadores) {
                    if (p == null || p == jugador || p.getVida() <= 0) continue;
                    if (sonAdyacentes(jugador, p)) { 
                        objetivo = p; 
                        break; 
                    }
                }
                if (objetivo != null) {
                    jugador.usarCarta(slotIndex, objetivo);
                } else {
                    logBatalla("No hay ningún jugador adyacente para robarle una carta.");
                }
            } else {
                jugador.usarCarta(slotIndex, null); 
            }
            jugador.eliminarCarta(slotIndex);
        } else {
            logBatalla("Slot " + (slotIndex + 1) + " está vacío.");
        }
    }

    private boolean esMovimientoValido(int targetX, int targetY, int targetZ) {
        if (tablero == null || !tablero.esCoordenadaValida(targetX, targetY, targetZ)) return false;
        Casillero c = tablero.getCasillero(targetX, targetY, targetZ);
        if (c.getTipo() == TipoCasillero.ROCA || c.getTipo() == TipoCasillero.AGUA) return false;
        return true; 
    }

    private void revisarCasilleroActual(Personaje jugador, int x, int y, int z) {
        if (jugador == null || tablero == null) return;
        
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
                logBatalla("¡Recogiste una carta!");
            } else {
                logBatalla("¡Inventario lleno! No puedes recoger la carta.");
            }
            return;
        }
        
        if (casilleroActual.getTipo() == TipoCasillero.RAMPA) {
            int zArriba = z + 1;
            if (tablero.esCoordenadaValida(x, y, zArriba) && 
                tablero.getCasillero(x, y, zArriba).getTipo() == TipoCasillero.RAMPA) {
                jugador.setPosicion(x, y, zArriba);
                panelJuego.setNivelZActual(zArriba);
                logBatalla("Subiste al nivel " + zArriba);
                return;
            }
            int zAbajo = z - 1;
            if (tablero.esCoordenadaValida(x, y, zAbajo) && 
                tablero.getCasillero(x, y, zAbajo).getTipo() == TipoCasillero.RAMPA) {
                jugador.setPosicion(x, y, zAbajo);
                panelJuego.setNivelZActual(zAbajo);
                logBatalla("Bajaste al nivel " + zAbajo);
            }
        }
    }

    public boolean sonAdyacentes(Personaje a, Personaje b) {
        if (a == null || b == null) return false;
        if (a.getPosZ() != b.getPosZ()) return false;
        int dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
        return dist == 1;
    }

    public void proponerAlianza(Personaje proponente, Personaje objetivo) {
        if (proponente == null || objetivo == null) return;
        if (!sonAdyacentes(proponente, objetivo)) {
            logBatalla("No estás lo suficientemente cerca para proponer alianza.");
            return;
        }
        if (proponente.getAlianza() != null && proponente.estaAliadoCon(objetivo)) {
            logBatalla("Ya estás aliado con " + objetivo.getNombre());
            return;
        }
        propuestas.put(objetivo, proponente);
        logBatalla(proponente.getNombre() + " propone alianza a " + objetivo.getNombre());
    }

    public Personaje getPropuestaPara(Personaje target) {
        return propuestas.get(target);
    }

    public void aceptarPropuesta(Personaje target) {
        if (target == null) return;
        Personaje proponente = propuestas.remove(target);
        if (proponente == null) {
            logBatalla("No hay propuestas para aceptar.");
            return;
        }
        com.entidades.Alianza nueva = new com.entidades.Alianza("Alianza " + proponente.getNombre() + "-" + target.getNombre());
        nueva.agregarMiembro(proponente);
        nueva.agregarMiembro(target);
        alianzas.add(nueva);
        logBatalla(target.getNombre() + " aceptó la alianza con " + proponente.getNombre());
    }

    public void rechazarPropuesta(Personaje target) {
        if (target == null) return;
        Personaje proponente = propuestas.remove(target);
        if (proponente != null) {
            logBatalla(target.getNombre() + " rechazó la alianza de " + proponente.getNombre());
        } else {
            logBatalla("No hay propuestas para rechazar.");
        }
    }

    public boolean transferirCarta(Personaje from, Personaje to, int slotIndex) {
        if (from == null || to == null) return false;
        if (!sonAdyacentes(from, to)) return false;
        if (from.getAlianza() == null || !from.estaAliadoCon(to)) return false;
        Carta carta = from.getInventario().getCarta(slotIndex);
        if (carta == null) return false;
        if (to.getInventario().cantidadDeCartas() >= 10) return false;
        to.agregarCarta(carta);
        from.eliminarCarta(slotIndex);
        logBatalla(from.getNombre() + " transfirió " + carta.getNombre() + " a " + to.getNombre());
        return true;
    }

    public void atacarJugador(Personaje atacante, Personaje objetivo) {
        if (atacante == null || objetivo == null) return;
        if (!sonAdyacentes(atacante, objetivo)) {
            logBatalla("No estás lo suficientemente cerca para atacar a " + objetivo.getNombre());
            return;
        }
        
        controlador.iniciarCombate(atacante, objetivo);
    }

    public void finalizarTurno() {
        actualizarVisibilidad(); // <-- ACTUALIZA VISIBILIDAD ANTES DE MOVER ENEMIGOS
        procesarTurnoEnemigos();
        procesarRupturaAlianzas();

        if (jugadores == null || jugadores.isEmpty()) return;
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
        if (enemigos == null) return;
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;
            Personaje objetivo = encontrarJugadorMasCercanoA(enemigo);
            if (objetivo == null) continue;
            if (enemigo.getPosZ() != objetivo.getPosZ()) continue;

            int dist = Math.abs(enemigo.getPosX() - objetivo.getPosX()) + Math.abs(enemigo.getPosY() - objetivo.getPosY());
            if (dist <= 1) {
                controlador.iniciarCombate(objetivo, enemigo);
                break; 
            }
        }
        
        if (jugadores != null) {
            for (Personaje p : jugadores) {
                if (p != null && p.isInvisible()) {
                    p.setInvisible(false);
                    logBatalla("La invisibilidad de " + p.getNombre() + " ha expirado.");
                }
            }
        }
        eliminarJugadoresMuertos();
    }

    private Personaje encontrarJugadorMasCercanoA(Enemigo enemigo) {
        if (jugadores == null || jugadores.isEmpty()) return null;
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

    private void eliminarJugadoresMuertos() {
        if (jugadores == null || jugadores.isEmpty()) return;
        boolean removed = false;
        java.util.Iterator<Personaje> it = jugadores.iterator();
        int idx = 0;
        int newIndex = indiceJugadorActual;
        while (it.hasNext()) {
            Personaje p = it.next();
            if (p.getVida() <= 0) {
                it.remove();
                logBatalla(p.getNombre() + " fue eliminado de la partida por muerte.");
                jugadoresEliminados.add(p.getNombre());
                removed = true;
                if (idx < indiceJugadorActual) {
                    newIndex = Math.max(0, newIndex - 1);
                }
            } else {
                idx++;
            }
        }
        if (removed) {
            indiceJugadorActual = (jugadores.isEmpty()) ? 0 : Math.min(newIndex, jugadores.size() - 1);
        }
    }

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

    private Enemigo enemigoEnPosicion(int x, int y, int z) {
        if (enemigos == null) return null;
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

        if (enemigos == null) return null;
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
            }
        }
    }
}