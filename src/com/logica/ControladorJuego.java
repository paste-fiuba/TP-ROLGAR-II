package com.logica;

import com.entidades.Enemigo;
import com.entidades.Entidad; 
import com.entidades.Personaje;
import com.tablero.Tablero;
import com.ui.PanelJuego;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {

    // Estados del juego
    public enum GameState { 
        MENU_PRINCIPAL,
        MENU_DIFICULTAD,
        MENU_JUGADORES,
        MENU_INSTRUCCIONES,
        RUNNING, 
        EN_COMBATE, 
        PAUSED, 
        GAME_OVER, 
        VICTORY 
    }
    
    private GameState estadoJuego;

    private AdministradorDeJuego adminJuego;
    private AdministradorDeCombate adminCombate; 
    private PanelJuego panelJuego;
    
    private PartidaDeRolgar partida; 
    private int pendingTransferSlot = -1;
    
    private PartidaDeRolgar.Dificultad dificultadSeleccionada;
    private int jugadoresSeleccionados;


    public ControladorJuego(PartidaDeRolgar partida, PanelJuego panel) {
        this.partida = partida;
        this.panelJuego = panel;
        this.estadoJuego = GameState.MENU_PRINCIPAL;
        this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.NORMAL; // Default
    }

    public void manejarInput(int keyCode) {
        
        // --- Flujo del Menú ---
        switch (estadoJuego) {
            case MENU_PRINCIPAL:
                manejarMenuPrincipal(keyCode);
                this.panelJuego.repaint();
                return;
            case MENU_DIFICULTAD:
                manejarMenuDificultad(keyCode);
                this.panelJuego.repaint();
                return;
            case MENU_JUGADORES:
                manejarMenuJugadores(keyCode);
                this.panelJuego.repaint();
                return;
            case MENU_INSTRUCCIONES:
                manejarMenuInstrucciones(keyCode);
                this.panelJuego.repaint();
                return;
            case EN_COMBATE: 
                manejarInputCombate(keyCode);
                this.panelJuego.repaint();
                return;
            default:
                break;
        }

        // --- Flujo del Juego (RUNNING, PAUSED, etc.) ---

        if (estadoJuego != GameState.GAME_OVER && estadoJuego != GameState.VICTORY) {
            if (adminJuego != null) adminJuego.limpiarLogCombate();
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (estadoJuego == GameState.RUNNING) {
                estadoJuego = GameState.PAUSED;
            } else if (estadoJuego == GameState.PAUSED) {
                estadoJuego = GameState.RUNNING;
            } else if (estadoJuego == GameState.MENU_DIFICULTAD || 
                       estadoJuego == GameState.MENU_JUGADORES ||
                       estadoJuego == GameState.MENU_INSTRUCCIONES) {
                estadoJuego = GameState.MENU_PRINCIPAL; 
            }
        }

        else if (estadoJuego == GameState.RUNNING) {
            if (adminJuego == null) return;
            
            boolean seMovio = false;
            if (keyCode == KeyEvent.VK_W) {
                seMovio = adminJuego.procesarMovimiento(0, -1);
            } else if (keyCode == KeyEvent.VK_S) {
                seMovio = adminJuego.procesarMovimiento(0, 1);
            } else if (keyCode == KeyEvent.VK_A) {
                seMovio = adminJuego.procesarMovimiento(-1, 0);
            } else if (keyCode == KeyEvent.VK_D) {
                seMovio = adminJuego.procesarMovimiento(1, 0);
            } else if (keyCode == KeyEvent.VK_Q) {
                seMovio = adminJuego.procesarMovimiento(-1, -1);
            } else if (keyCode == KeyEvent.VK_E) {
                seMovio = adminJuego.procesarMovimiento(1, -1);
            } else if (keyCode == KeyEvent.VK_Z) {
                seMovio = adminJuego.procesarMovimiento(-1, 1);
            } else if (keyCode == KeyEvent.VK_C) {
                seMovio = adminJuego.procesarMovimiento(1, 1);
            }
            
            if (seMovio) {
                adminJuego.actualizarVisibilidad(); // <-- ACTUALIZA VISIBILIDAD
                adminJuego.finalizarTurno(); 
            }
            
            // Lógica de Acciones (no movimiento)
            else if (keyCode == KeyEvent.VK_F) {
                Personaje atacante = adminJuego.getJugadorActual();
                if (atacante != null) {
                    Personaje objetivo = null;
                    for (Personaje p : adminJuego.getJugadores()) {
                        if (p == null || p == atacante || p.getVida() <= 0) continue;
                        if (adminJuego.sonAdyacentes(atacante, p)) { 
                            objetivo = p; 
                            break; 
                        }
                    }
                    if (objetivo != null) {
                        iniciarCombate(atacante, objetivo);
                    } else {
                        if (panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().agregarMensajeLog("No hay jugadores adyacentes para atacar.");
                    }
                }
            } else if (keyCode == KeyEvent.VK_T) {
                pendingTransferSlot = -2;
                if (panelJuego != null && panelJuego.getRenderUI() != null) {
                    panelJuego.getRenderUI().agregarMensajeLog("Transferencia: presioná 1..0 para elegir el slot a transferir.");
                }
            
            } else if (keyCode == KeyEvent.VK_L) {
                Personaje actual = adminJuego.getJugadorActual();
                if (actual != null) {
                    Personaje objetivo = null;
                    for (Personaje p : adminJuego.getJugadores()) {
                        if (p == null || p == actual || p.getVida() <= 0) continue;
                        if (!actual.estaAliadoCon(p) && adminJuego.sonAdyacentes(actual, p)) {
                            objetivo = p;
                            break;
                        }
                    }
                    if (objetivo != null) {
                        adminJuego.proponerAlianza(actual, objetivo);
                    }
                }
            } else if (keyCode == KeyEvent.VK_Y) {
                Personaje actual = adminJuego.getJugadorActual();
                if (actual != null) adminJuego.aceptarPropuesta(actual);
            } else if (keyCode == KeyEvent.VK_N) {
                Personaje actual = adminJuego.getJugadorActual();
                if (actual != null) adminJuego.rechazarPropuesta(actual);
            }
            else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1;
                if (pendingTransferSlot != -1) {
                    Personaje from = adminJuego.getJugadorActual();
                    if (from == null) {
                        pendingTransferSlot = -1;
                    } else {
                        Personaje objetivo = null;
                        for (Personaje p : adminJuego.getJugadores()) {
                            if (p == null || p == from || p.getVida() <= 0) continue;
                            if (from.getAlianza() != null && from.estaAliadoCon(p) && adminJuego.sonAdyacentes(from, p)) {
                                objetivo = p;
                                break;
                            }
                        }
                        if (objetivo == null) {
                            if (panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().agregarMensajeLog("No hay aliado adyacente para transferir.");
                        } else {
                            boolean ok = adminJuego.transferirCarta(from, objetivo, slotIndex);
                            if (!ok && panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().agregarMensajeLog("Transferencia fallida.");
                        }
                        pendingTransferSlot = -1;
                    }
                } else {
                    // Usar carta (esto gasta el turno)
                    adminJuego.activarCarta(slotIndex);
                    adminJuego.finalizarTurno(); 
                }
            }
        }

        else if (estadoJuego == GameState.PAUSED) {
            if (keyCode == KeyEvent.VK_R) {
                estadoJuego = GameState.RUNNING;
            } else if (keyCode == KeyEvent.VK_Q) {
                System.exit(0);
            }
        }

        else if (estadoJuego == GameState.GAME_OVER || estadoJuego == GameState.VICTORY) {
            if (keyCode == KeyEvent.VK_Q) {
                System.exit(0);
            }
        }

        // Revisar estado después de cada acción
        if (estadoJuego == GameState.RUNNING && adminJuego != null) {
            if (adminJuego.isJugadorMuerto()) {
                estadoJuego = GameState.GAME_OVER;
            } else if (adminJuego.isVictoria()) {
                estadoJuego = GameState.VICTORY;
            }
        }

        this.panelJuego.repaint();
    }
    
    // --- Métodos de manejo de Menú ---
    
    private void manejarMenuPrincipal(int keyCode) {
        if (keyCode == KeyEvent.VK_1) { // 1. Empezar Partida
            this.estadoJuego = GameState.MENU_DIFICULTAD;
        }
        if (keyCode == KeyEvent.VK_2) { // 2. Instrucciones
            this.estadoJuego = GameState.MENU_INSTRUCCIONES;
        }
        if (keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_ESCAPE) { // 3. Salir
            System.exit(0);
        }
    }
    
    private void manejarMenuDificultad(int keyCode) {
        if (keyCode == KeyEvent.VK_1) { // 1. Fácil
            this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.FACIL;
            this.estadoJuego = GameState.MENU_JUGADORES;
        }
        if (keyCode == KeyEvent.VK_2) { // 2. Normal
            this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.NORMAL;
            this.estadoJuego = GameState.MENU_JUGADORES;
        }
        if (keyCode == KeyEvent.VK_3) { // 3. Difícil
            this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.DIFICIL;
            this.estadoJuego = GameState.MENU_JUGADORES;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            this.estadoJuego = GameState.MENU_PRINCIPAL;
        }
    }
    
    private void manejarMenuJugadores(int keyCode) {
        if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_4) {
            this.jugadoresSeleccionados = keyCode - KeyEvent.VK_0; // '1' -> 1
            iniciarPartida();
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            this.estadoJuego = GameState.MENU_DIFICULTAD;
        }
    }

    private void manejarMenuInstrucciones(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) { // Salir con ESC
            this.estadoJuego = GameState.MENU_PRINCIPAL;
        }
    }

    private void manejarInputCombate(int keyCode) {
        if (adminCombate == null) return;
        adminCombate.manejarInput(keyCode);
    }
    
    // --- Métodos de Control de Partida ---

    public GameState getEstadoJuego() {
        return this.estadoJuego;
    }
    
    public AdministradorDeCombate getAdminCombate() {
        return this.adminCombate;
    }

    public int getPendingTransferSlot() {
        return this.pendingTransferSlot;
    }

    public void iniciarPartida() {
        partida.cargarPartida(dificultadSeleccionada, jugadoresSeleccionados);
        
        this.dificultadSeleccionada = partida.getDificultad(); 

        Tablero tablero = partida.getTablero();
        List<Personaje> jugadores = partida.getJugadores();
        List<Enemigo> enemigos = partida.getEnemigos();

        panelJuego.setDatosDePartida(tablero, jugadores, enemigos);

        this.adminJuego = new AdministradorDeJuego(this, tablero, jugadores, enemigos, panelJuego);
        panelJuego.setAdministrador(adminJuego);
        
        // --- NUEVO: Actualiza la visibilidad al iniciar ---
        this.adminJuego.actualizarVisibilidad(); 
        
        Personaje primer = this.adminJuego.getJugadorActual();
        if (primer != null) panelJuego.setNivelZActual(primer.getPosZ());
        
        this.estadoJuego = GameState.RUNNING;
        panelJuego.repaint();
    }
    
    public void iniciarCombate(Personaje jugador, Entidad oponente) {
        this.adminCombate = new AdministradorDeCombate(this, this.adminJuego, jugador, oponente);
        this.estadoJuego = GameState.EN_COMBATE;
    }
    
    public void finalizarCombate() {
        this.adminCombate = null;
        this.estadoJuego = GameState.RUNNING;
        
        // --- NUEVO: Actualiza la visibilidad al salir del combate ---
        if(this.adminJuego != null) {
            this.adminJuego.actualizarVisibilidad();
        }

        // Chequear estado general post-combate
        if (adminJuego.isJugadorMuerto()) {
            estadoJuego = GameState.GAME_OVER;
        } else if (adminJuego.isVictoria()) {
            estadoJuego = GameState.VICTORY;
        }
    }
}