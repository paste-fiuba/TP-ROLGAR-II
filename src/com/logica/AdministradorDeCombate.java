package com.logica;

import com.entidades.Entidad;
import com.entidades.Personaje;
import com.entidades.Enemigo;
import com.items.Carta;
import com.items.CartaAtaqueDoble; 
import com.items.CartaEscudo;     
import java.util.Random;

public class AdministradorDeCombate {

    public enum EstadoCombate { 
        ELIGE_ACCION,     
        ELIGE_CARTA,      
        TURNO_OPONENTE, 
        FINALIZADO 
    }
    
    private Personaje jugador;
    private Entidad oponente;
    private ControladorJuego controlador;
    private AdministradorDeJuego adminJuego; 
    
    private EstadoCombate estado;
    private Random random = new Random();
    private String mensajeAccion = ""; 
    
    public AdministradorDeCombate(ControladorJuego controlador, AdministradorDeJuego adminJuego, Personaje jugador, Entidad oponente) {
        this.controlador = controlador;
        this.adminJuego = adminJuego;
        this.jugador = jugador;
        this.oponente = oponente;
        this.estado = EstadoCombate.ELIGE_ACCION; 
        this.adminJuego.limpiarLogCombate(); 
        this.adminJuego.logBatalla("¡" + oponente.getNombre() + " te desafía!");
        this.mensajeAccion = "Elige tu acción...";
    }

    /**
     * pre: accion no es NINGUNA.
     * post: Procesa la acción del jugador (traducida por ControladorJuego).
     */
    public void procesarAccion(AccionCombate accion) {
        switch (estado) {
            case ELIGE_ACCION:
                procesarAccionPrincipal(accion);
                break;
            case ELIGE_CARTA:
                procesarAccionCarta(accion);
                break;
            case TURNO_OPONENTE:
            case FINALIZADO:
                break;
        }
    }

    /**
     * pre: El estado es ELIGE_ACCION.
     * post: Procesa la acción principal (Luchar, Carta, Huir).
     */
    private void procesarAccionPrincipal(AccionCombate accion) {
        if (accion == AccionCombate.ATACAR) {
            jugadorAtaca();
            if (estado == EstadoCombate.ELIGE_ACCION) { 
                cambiarTurno();
            }
        } else if (accion == AccionCombate.ABRIR_MENU_CARTA) {
            this.estado = EstadoCombate.ELIGE_CARTA;
            this.mensajeAccion = "Elige una carta (1-0) o [ESC] para cancelar.";
            adminJuego.limpiarLogCombate(); 
        } else if (accion == AccionCombate.HUIR) {
            jugadorIntentaHuir();
            if (estado == EstadoCombate.ELIGE_ACCION) { 
                cambiarTurno();
            }
        }
    }

    /**
     * pre: El estado es ELIGE_CARTA.
     * post: Procesa la selección de carta o la cancelación.
     */
    private void procesarAccionCarta(AccionCombate accion) {
        if (accion == AccionCombate.CANCELAR_CARTA) { 
            this.estado = EstadoCombate.ELIGE_ACCION;
            this.mensajeAccion = "Elige tu acción...";
            adminJuego.limpiarLogCombate();
            adminJuego.logBatalla("¡" + oponente.getNombre() + " te desafía!"); 
            return;
        }
        
        int slotIndex = getSlotFromAccion(accion);
        
        if (slotIndex != -1) { 
            boolean exito = jugadorUsaCarta(slotIndex);
            
            if (exito && estado == EstadoCombate.ELIGE_CARTA) { 
                this.estado = EstadoCombate.ELIGE_ACCION;
                cambiarTurno();
            }
        }
    }
    
    /**
     * Convierte un enum de AccionCombate a un índice de slot (0-9).
     * Devuelve -1 si no es una acción de carta.
     */
    private int getSlotFromAccion(AccionCombate accion) {
        switch(accion) {
            case USAR_CARTA_1: return 0;
            case USAR_CARTA_2: return 1;
            case USAR_CARTA_3: return 2;
            case USAR_CARTA_4: return 3;
            case USAR_CARTA_5: return 4;
            case USAR_CARTA_6: return 5;
            case USAR_CARTA_7: return 6;
            case USAR_CARTA_8: return 7;
            case USAR_CARTA_9: return 8;
            case USAR_CARTA_0: return 9;
            default: return -1;
        }
    }



    

    private void jugadorAtaca() {
        int dmgJugador;
        
        if (jugador.isAtaqueDobleActivo()) {
            adminJuego.logBatalla("¡ATAQUE DOBLE!");
            int dmgBase = (jugador.getFuerza() / 2) + random.nextInt(Math.max(1, jugador.getFuerza()));
            dmgJugador = dmgBase * 2;
            jugador.setAtaqueDobleActivo(false);
        } else {
            dmgJugador = (jugador.getFuerza() / 2) + random.nextInt(Math.max(1, jugador.getFuerza()));
        }

        if (jugador.getAlianza() != null) {
            for (Personaje aliado : jugador.getAlianza().getMiembros()) {
                if (aliado == jugador || aliado.getVida() <= 0) continue;
                if (aliado.getPosZ() != oponente.getPosZ()) continue;
                int dist = Math.abs(aliado.getPosX() - oponente.getPosX()) + Math.abs(aliado.getPosY() - oponente.getPosY());
                if (dist <= 1) { 
                    int ayuda = (aliado.getFuerza() / 3) + random.nextInt(Math.max(1, aliado.getFuerza() / 2));
                    dmgJugador += ayuda;
                    adminJuego.logBatalla(aliado.getNombre() + " ayuda por " + ayuda + " de daño.");
                }
            }
        }
        
        oponente.recibirDanio(dmgJugador);
        adminJuego.logBatalla(jugador.getNombre() + " golpea por " + dmgJugador + "!");
        
        verificarFinDeCombate();
    }
    
    private boolean jugadorUsaCarta(int slotIndex) {
        if (jugador.getInventario().cantidadDeCartas() > slotIndex) {
            Carta carta = jugador.getInventario().getCarta(slotIndex);
            
            if (!(carta instanceof CartaAtaqueDoble) && !(carta instanceof CartaEscudo)) {
                adminJuego.logBatalla("¡Solo puedes usar cartas de Ataque o Escudo en combate!");
                return false; 
            }

            adminJuego.logBatalla(jugador.getNombre() + " usó '" + carta.getNombre() + "'!");
            jugador.usarCarta(slotIndex, oponente); 
            jugador.eliminarCarta(slotIndex); 
            
            verificarFinDeCombate();
            return true; 
        } else {
            adminJuego.logBatalla("¡No hay una carta en ese slot!");
            return false; 
        }
    }

    private void jugadorIntentaHuir() {
        if (random.nextDouble() < 0.30) {
            adminJuego.logBatalla("¡Lograste escapar!");
            this.estado = EstadoCombate.FINALIZADO;
            controlador.finalizarCombate();
        } else {
            adminJuego.logBatalla("¡No pudiste escapar!");
        }
    }

    private void cambiarTurno() {
        this.estado = EstadoCombate.TURNO_OPONENTE;
        this.mensajeAccion = "Turno del oponente...";
        
        procesarTurnoOponente();
    }

    private void procesarTurnoOponente() {
        if (estado != EstadoCombate.TURNO_OPONENTE) return;

        int dmgEnemigo = (oponente.getFuerza() / 2) + random.nextInt(Math.max(1, oponente.getFuerza()));
        jugador.recibirDanio(dmgEnemigo);
        adminJuego.logBatalla(oponente.getNombre() + " golpea por " + dmgEnemigo + "!");

        verificarFinDeCombate();

        if (estado == EstadoCombate.TURNO_OPONENTE) {
            this.estado = EstadoCombate.ELIGE_ACCION;
            this.mensajeAccion = "Elige tu acción...";
        }
    }

    private void verificarFinDeCombate() {
        if (jugador.getVida() <= 0) {
            adminJuego.logBatalla("¡" + jugador.getNombre() + " ha sido derrotado!");
            this.estado = EstadoCombate.FINALIZADO;
            controlador.finalizarCombate(); 
            
        } else if (oponente.getVida() <= 0) {
            adminJuego.logBatalla("¡Has derrotado a " + oponente.getNombre() + "!");
            
            if (oponente instanceof Enemigo) {
                ((Enemigo) oponente).recibirDanio(0); 
            }
            
            this.estado = EstadoCombate.FINALIZADO;
            controlador.finalizarCombate();
        }
    }

    // --- Getters ---
    public EstadoCombate getEstado() { return this.estado; }
    public Personaje getJugador() { return jugador; }
    public Entidad getOponente() { return oponente; }
    public String getMensajeAccion() { return mensajeAccion; }
}