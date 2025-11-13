package com.logica;

import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.ui.PanelJuego;
import java.util.List;
import java.util.Random;

public class AdministradorDeJuego {

    private Tablero tablero;
    private Personaje jugador;
    private List<Enemigo> enemigos;
    private PanelJuego panelJuego;
    private Random random;

    public AdministradorDeJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.panelJuego = panel;
        this.random = new Random();
    }
    
    public boolean isJugadorMuerto() {
        return jugador.getVida() <= 0;
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

    public void activarCarta(int slotIndex) {
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