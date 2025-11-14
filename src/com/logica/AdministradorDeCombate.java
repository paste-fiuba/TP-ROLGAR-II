package com.logica;

import com.entidades.Entidad;
import com.entidades.Personaje;
import com.entidades.Enemigo;
import com.items.Carta;
import java.awt.event.KeyEvent; // <-- IMPORTANTE
import java.util.Random;

/**
 * TDA para gestionar una instancia de combate por turnos.
 * Se activa cuando el estado del juego es EN_COMBATE.
 */
public class AdministradorDeCombate {

    // --- ESTADOS INTERNOS DEL COMBATE ---
    public enum EstadoCombate { 
        ELIGE_ACCION,     // Mostrando [1] Luchar, [2] Carta, [3] Huir
        ELIGE_CARTA,      // Mostrando el inventario
        TURNO_OPONENTE, 
        FINALIZADO 
    }
    
    private Personaje jugador;
    private Entidad oponente;
    private ControladorJuego controlador;
    private AdministradorDeJuego adminJuego; // Para usar el log
    
    private EstadoCombate estado;
    private Random random = new Random();
    private String mensajeAccion = ""; // Mensaje para mostrar en la UI

    /**
     * pre: controlador, adminJuego, jugador y oponente no son null.
     * post: Crea una nueva instancia de combate. El turno siempre empieza por el jugador.
     */
    public AdministradorDeCombate(ControladorJuego controlador, AdministradorDeJuego adminJuego, Personaje jugador, Entidad oponente) {
        this.controlador = controlador;
        this.adminJuego = adminJuego;
        this.jugador = jugador;
        this.oponente = oponente;
        this.estado = EstadoCombate.ELIGE_ACCION; // Inicia en la elección de acción
        this.adminJuego.limpiarLogCombate(); // Limpia el log al iniciar
        this.adminJuego.logBatalla("¡" + oponente.getNombre() + " te desafía!");
        this.mensajeAccion = "Elige tu acción...";
    }

    /**
     * pre: keyCode es la tecla presionada por el usuario.
     * post: Procesa el input según el estado actual del combate (elegir acción o elegir carta).
     */
    public void manejarInput(int keyCode) {
        switch (estado) {
            case ELIGE_ACCION:
                manejarInputAccion(keyCode);
                break;
            case ELIGE_CARTA:
                manejarInputCarta(keyCode);
                break;
            case TURNO_OPONENTE:
            case FINALIZADO:
                // No hacer nada si no es el turno del jugador
                break;
        }
    }

    /**
     * pre: El estado es ELIGE_ACCION.
     * post: Procesa la acción principal (Luchar, Carta, Huir).
     */
    private void manejarInputAccion(int keyCode) {
        if (keyCode == KeyEvent.VK_1) { // [1] LUCHAR
            jugadorAtaca();
            if (estado == EstadoCombate.ELIGE_ACCION) { // Si el combate no terminó
                cambiarTurno();
            }
        } else if (keyCode == KeyEvent.VK_2) { // [2] CARTA
            // Cambiar al sub-menú de cartas
            this.estado = EstadoCombate.ELIGE_CARTA;
            this.mensajeAccion = "Elige una carta (1-0) o [ESC] para cancelar.";
            adminJuego.limpiarLogCombate(); 
        } else if (keyCode == KeyEvent.VK_3) { // [3] HUIR
            jugadorIntentaHuir();
            if (estado == EstadoCombate.ELIGE_ACCION) { // Si falló la huida
                cambiarTurno();
            }
        }
    }

    /**
     * pre: El estado es ELIGE_CARTA.
     * post: Procesa la selección de carta o la cancelación.
     */
    private void manejarInputCarta(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) { // Cancelar
            this.estado = EstadoCombate.ELIGE_ACCION;
            this.mensajeAccion = "Elige tu acción...";
            adminJuego.limpiarLogCombate();
            adminJuego.logBatalla("¡" + oponente.getNombre() + " te desafía!"); // Restaurar mensaje
            return;
        }
        
        if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
            int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1; // 0 es el slot 10
            
            boolean exito = jugadorUsaCarta(slotIndex);
            
            if (exito && estado == EstadoCombate.ELIGE_CARTA) { // Si usó la carta y el combate no terminó
                // El estado vuelve a ELIGE_ACCION antes de cambiar de turno
                this.estado = EstadoCombate.ELIGE_ACCION;
                cambiarTurno();
            }
            // Si 'exito' es false (ej. slot vacío), no cambia de turno,
            // el jugador puede elegir otra carta.
        }
    }

    /**
     * pre: -
     * post: El jugador ataca al oponente. Se aplican buffs y se loguea la acción.
     */
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

        // Aliados cercanos ayudan
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
    
    /**
     * pre: slotIndex es un índice válido en el inventario.
     * post: El jugador usa una carta. Se aplica el efecto y se consume la carta.
     * Devuelve true si la carta se usó, false si no.
     */
    private boolean jugadorUsaCarta(int slotIndex) {
        if (jugador.getInventario().cantidadDeCartas() > slotIndex) {
            Carta carta = jugador.getInventario().getCarta(slotIndex);
            adminJuego.logBatalla(jugador.getNombre() + " usó '" + carta.getNombre() + "'!");
            
            if (carta instanceof com.items.CartaRoboDeCarta || carta instanceof com.items.CartaCuracionAliado) {
                 adminJuego.logBatalla("¡No puedes usar esa carta en combate!");
                 return false; // No gasta el turno si la carta es inválida
            }

            jugador.usarCarta(slotIndex, oponente); // Aplicar efecto
            jugador.eliminarCarta(slotIndex); // Consumir carta
            
            verificarFinDeCombate();
            return true; // Se usó la carta
        } else {
            adminJuego.logBatalla("¡No hay una carta en ese slot!");
            return false; // No se usó la carta
        }
    }

    /**
     * pre: -
     * post: El jugador intenta huir. Si tiene éxito, finaliza el combate.
     */
    private void jugadorIntentaHuir() {
        // Un 30% de chance de huir
        if (random.nextDouble() < 0.30) {
            adminJuego.logBatalla("¡Lograste escapar!");
            this.estado = EstadoCombate.FINALIZADO;
            controlador.finalizarCombate();
        } else {
            adminJuego.logBatalla("¡No pudiste escapar!");
        }
    }

    /**
     * pre: -
     * post: Cambia el turno al oponente y ejecuta su lógica.
     */
    private void cambiarTurno() {
        this.estado = EstadoCombate.TURNO_OPONENTE;
        this.mensajeAccion = "Turno del oponente...";
        
        procesarTurnoOponente();
    }

    /**
     * pre: -
     * post: El oponente realiza su acción (atacar) y se cambia el turno al jugador.
     */
    private void procesarTurnoOponente() {
        if (estado != EstadoCombate.TURNO_OPONENTE) return;

        // Lógica de IA del oponente (simple: siempre ataca)
        int dmgEnemigo = (oponente.getFuerza() / 2) + random.nextInt(Math.max(1, oponente.getFuerza()));
        jugador.recibirDanio(dmgEnemigo);
        adminJuego.logBatalla(oponente.getNombre() + " golpea por " + dmgEnemigo + "!");

        verificarFinDeCombate();

        // Si el combate no terminó, devolver el turno al jugador
        if (estado == EstadoCombate.TURNO_OPONENTE) {
            this.estado = EstadoCombate.ELIGE_ACCION;
            this.mensajeAccion = "Elige tu acción...";
        }
    }

    /**
     * pre: -
     * post: Verifica si alguno de los combatientes ha sido derrotado.
     * Si es así, finaliza el combate.
     */
    private void verificarFinDeCombate() {
        if (jugador.getVida() <= 0) {
            adminJuego.logBatalla("¡" + jugador.getNombre() + " ha sido derrotado!");
            this.estado = EstadoCombate.FINALIZADO;
            controlador.finalizarCombate(); 
            
        } else if (oponente.getVida() <= 0) {
            adminJuego.logBatalla("¡Has derrotado a " + oponente.getNombre() + "!");
            
            if (oponente instanceof Enemigo) {
                ((Enemigo) oponente).recibirDanio(0); // Esto lo marcará como muerto
            }
            
            this.estado = EstadoCombate.FINALIZADO;
            controlador.finalizarCombate();
        }
    }

    // --- Getters para el RenderizadorUI ---
    public EstadoCombate getEstado() { return this.estado; }
    public Personaje getJugador() { return jugador; }
    public Entidad getOponente() { return oponente; }
    public String getMensajeAccion() { return mensajeAccion; }
}