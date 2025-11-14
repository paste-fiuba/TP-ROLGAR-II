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

    private Tablero tablero;
    private List<Personaje> jugadores;
    private int indiceJugadorActual = 0;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;
    private Random random;
    // Alianzas y propuestas (target -> proposer)
    private List<com.entidades.Alianza> alianzas;
    private Map<Personaje, Personaje> propuestas;
    // Probabilidad de ruptura de cada alianza por turno (15% por defecto)
    private static final double PROB_RUPTURA_ALIANZA = 0.15;
    private List<String> jugadoresEliminados;

    public AdministradorDeJuego(Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.enemigos = enemigos;
        this.panelJuego = panel;
        this.random = new Random();
        this.alianzas = new ArrayList<>();
        this.propuestas = new HashMap<>();
        this.jugadoresEliminados = new ArrayList<>();
    }
    
    public boolean isJugadorMuerto() {
        // Devuelve true si TODOS los jugadores están muertos
        if (jugadores == null || jugadores.isEmpty()) return true; // Si no hay jugadores
        for (Personaje p : jugadores) {
            if (p.getVida() > 0) return false;
        }
        return true;
    }
    
    /**
     * post: Devuelve true si todos los enemigos están muertos, false en caso contrario.
     */
    public boolean isVictoria() {
        if (enemigos == null || enemigos.isEmpty()) return true; // Si no hay enemigos, hay victoria
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
    
    /**
     * post: Si el jugador tiene movimientos extra, los gasta. Si no, finaliza el turno.
     */
    public void finalizarTurnoSiCorresponde() {
        Personaje jugador = getJugadorActual();
        if (jugador != null && jugador.getMovimientosExtra() > 0) {
            jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
            logBatalla(jugador.getNombre() + " usó un movimiento extra.");
        } else {
            finalizarTurno();
        }
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
            revisarCasilleroActual(newX, newY, z);
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
            // Si es una carta de robo, intentar robar a un jugador adyacente
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
            // Eliminar la carta usada
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

    private void revisarCasilleroActual(int x, int y, int z) {
        Personaje jugador = getJugadorActual();
        if (jugador == null || tablero == null) return;
        
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

        // Aliados cercanos que ayudan en combate (si pertenecen a la misma alianza)
        if (p.getAlianza() != null) {
            for (Personaje aliado : p.getAlianza().getMiembros()) {
                if (aliado == p || aliado.getVida() <= 0) continue;
                if (aliado.getPosZ() != e.getPosZ()) continue;
                int dist = Math.abs(aliado.getPosX() - e.getPosX()) + Math.abs(aliado.getPosY() - e.getPosY());
                if (dist <= 1) {
                    int ayuda = (aliado.getFuerza() / 3) + random.nextInt(Math.max(1, aliado.getFuerza() / 2));
                    dmgJugador += ayuda;
                    logBatalla(aliado.getNombre() + " ayuda a " + p.getNombre() + " por " + ayuda + " de daño.");
                }
            }
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
        // Remover jugadores muertos para que desaparezcan completamente
        eliminarJugadoresMuertos();
    }

    // Comprueba si dos personajes están adyacentes (misma Z y distancia Manhattan == 1)
    public boolean sonAdyacentes(Personaje a, Personaje b) {
        if (a == null || b == null) return false;
        if (a.getPosZ() != b.getPosZ()) return false;
        int dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
        return dist == 1;
    }

    // Proponer una alianza desde 'proponente' hacia 'objetivo' si están adyacentes y no ya aliados
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

    // Devuelve el proponente de una propuesta dirigida a 'target', o null
    public Personaje getPropuestaPara(Personaje target) {
        return propuestas.get(target);
    }

    // Aceptar una propuesta dirigida al jugador actual
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

    // Transferir carta entre jugadores aliados y adyacentes
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

    /**
     * Permite que un jugador ataque a otro jugador adyacente.
     * Ambos reciben daño (ataque y contraataque) similar al combate jugador-enemigo.
     */
    public void atacarJugador(Personaje atacante, Personaje objetivo) {
        if (atacante == null || objetivo == null) return;
        if (!sonAdyacentes(atacante, objetivo)) {
            logBatalla("No estás lo suficientemente cerca para atacar a " + objetivo.getNombre());
            return;
        }

        int dmgAtacante = (atacante.getFuerza() / 2) + random.nextInt(Math.max(1, atacante.getFuerza()));
        int dmgObjetivo = (objetivo.getFuerza() / 2) + random.nextInt(Math.max(1, objetivo.getFuerza()));

        objetivo.recibirDanio(dmgAtacante);
        atacante.recibirDanio(dmgObjetivo);

        logBatalla(atacante.getNombre() + " ataca a " + objetivo.getNombre() + " por " + dmgAtacante + "!");
        logBatalla(objetivo.getNombre() + " contraataca a " + atacante.getNombre() + " por " + dmgObjetivo + "!");

        if (atacante.getVida() <= 0) logBatalla(atacante.getNombre() + " ha muerto en el combate contra " + objetivo.getNombre());
        if (objetivo.getVida() <= 0) logBatalla(objetivo.getNombre() + " ha muerto en el combate contra " + atacante.getNombre());
        // Remover jugadores muertos tras combate PvP
        eliminarJugadoresMuertos();
    }

    /**
     * Finaliza el turno del jugador actual: procesa los efectos de los enemigos
     * (Opción A: enemigos actúan tras cada turno de jugador) y avanza al siguiente jugador vivo.
     */
    public void finalizarTurno() {
        // Procesar acciones simples de enemigos
        procesarTurnoEnemigos();

        // Procesar rupturas aleatorias de alianzas cada vez que finaliza un turno
        procesarRupturaAlianzas();

        // Avanzar al siguiente jugador vivo
        if (jugadores == null || jugadores.isEmpty()) return;
        int size = jugadores.size();
        if (size == 0) return; // No hay jugadores vivos
        
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
        if (enemigos == null) return;
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
        // Limpiar invisibilidad: la carta dura exactamente un turno de enemigos
        if (jugadores != null) {
            for (Personaje p : jugadores) {
                if (p != null && p.isInvisible()) {
                    p.setInvisible(false);
                    logBatalla("La invisibilidad de " + p.getNombre() + " ha expirado.");
                }
            }
        }
        // Remover jugadores muertos tras acciones de enemigos
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

    // Elimina de la lista los jugadores con vida <= 0 y ajusta el índice del jugador actual
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
                // Guardar nombre para mostrar en UI
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

    /**
     * Devuelve una copia de los nombres de jugadores eliminados (para mostrar en UI).
     */
    public List<String> getJugadoresEliminados() {
        return new ArrayList<>(this.jugadoresEliminados);
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

    /**
     * Procesa rupturas aleatorias de alianzas. Cada alianza tiene una probabilidad
     * de romperse parcialmente (se elimina un miembro aleatorio) en cada turno.
     * Si tras la eliminación la alianza queda con 0 o 1 miembros, la alianza se
     * disuelve completamente.
     */
    private void procesarRupturaAlianzas() {
        if (alianzas == null || alianzas.isEmpty()) return;

        // Usar copia para evitar ConcurrentModification
        java.util.List<com.entidades.Alianza> copia = new java.util.ArrayList<>(alianzas);
        for (com.entidades.Alianza al : copia) {
            if (al == null) continue;
            if (random.nextDouble() < PROB_RUPTURA_ALIANZA) {
                java.util.List<Personaje> miembros = al.getMiembros();
                if (miembros == null || miembros.isEmpty()) {
                    alianzas.remove(al);
                    continue;
                }
                // Elegir miembro al azar para expulsar de la alianza
                int idx = random.nextInt(miembros.size());
                Personaje expulsado = miembros.get(idx);
                if (expulsado != null) {
                    al.eliminarMiembro(expulsado);
                    logBatalla("La alianza '" + al.toString() + "' se rompió: " + expulsado.getNombre() + " fue expulsado.");
                }
                // Si la alianza quedó con 0 o 1 miembros, disolverla
                java.util.List<Personaje> despues = al.getMiembros();
                if (despues.size() <= 1) {
                    // Eliminar cualquier referencia restante
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