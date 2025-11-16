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

    private LectorTeclado lectorTeclado;


    public ControladorJuego(PartidaDeRolgar partida, PanelJuego panel) {
        this.partida = partida;
        this.panelJuego = panel;
        this.estadoJuego = GameState.MENU_PRINCIPAL;
        this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.NORMAL;
        this.lectorTeclado = new LectorTeclado();
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
                manejarInputCombate(keyCode); // Llama al método modificado
                this.panelJuego.repaint();
                return;
            default:
                break;
        }

        // --- Flujo del Juego (RUNNING, PAUSED, etc.) ---
        
        AccionJuego accion = lectorTeclado.traducirInput(keyCode);

        if (estadoJuego != GameState.GAME_OVER && estadoJuego != GameState.VICTORY) {
            if (adminJuego != null) adminJuego.limpiarLogCombate();
        }

        if (accion == AccionJuego.PAUSA) {
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
            
            switch(accion) {
                case MOV_ARRIBA:            seMovio = adminJuego.procesarMovimiento(0, -1); break;
                case MOV_ABAJO:             seMovio = adminJuego.procesarMovimiento(0, 1); break;
                case MOV_IZQUIERDA:         seMovio = adminJuego.procesarMovimiento(-1, 0); break;
                case MOV_DERECHA:           seMovio = adminJuego.procesarMovimiento(1, 0); break;
                case MOV_DIAG_ARRIBA_IZQ:   seMovio = adminJuego.procesarMovimiento(-1, -1); break;
                case MOV_DIAG_ARRIBA_DER:   seMovio = adminJuego.procesarMovimiento(1, -1); break;
                case MOV_DIAG_ABAJO_IZQ:    seMovio = adminJuego.procesarMovimiento(-1, 1); break;
                case MOV_DIAG_ABAJO_DER:    seMovio = adminJuego.procesarMovimiento(1, 1); break;
                default:
                    break; 
            }
            
            if (seMovio) {
                adminJuego.actualizarVisibilidad(); 
                adminJuego.finalizarTurno(); 
            }
            
            else {
                switch(accion) {
                    case ACCION_ATACAR_JUGADOR:
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
                                adminJuego.atacarJugador(atacante, objetivo);
                            } else {
                                if (panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().agregarMensajeLog("No hay jugadores adyacentes para atacar.");
                            }
                        }
                        break;
                    
                    case ACCION_INICIAR_TRANSFERENCIA:
                        pendingTransferSlot = -2;
                        if (panelJuego != null && panelJuego.getRenderUI() != null) {
                            panelJuego.getRenderUI().agregarMensajeLog("Transferencia: presioná 1..0 para elegir el slot a transferir.");
                        }
                        break;
                    
                    case ACCION_PROPONER_ALIANZA:
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
                        break;
                    
                    case ACCION_ACEPTAR_ALIANZA:
                        actual = adminJuego.getJugadorActual();
                        if (actual != null) adminJuego.aceptarPropuesta(actual);
                        break;
                    
                    case ACCION_RECHAZAR_ALIANZA:
                        actual = adminJuego.getJugadorActual();
                        if (actual != null) adminJuego.rechazarPropuesta(actual);
                        break;

                    case USAR_CARTA_1: case USAR_CARTA_2: case USAR_CARTA_3: case USAR_CARTA_4: case USAR_CARTA_5:
                    case USAR_CARTA_6: case USAR_CARTA_7: case USAR_CARTA_8: case USAR_CARTA_9: case USAR_CARTA_0:
                        int slotIndex = getSlotFromAccionJuego(accion);
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
                            adminJuego.activarCarta(slotIndex); // <-- Esta es la llamada clave
                            adminJuego.finalizarTurno(); 
                        }
                        break;
                    
                    default:
                        break;
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

        if (estadoJuego == GameState.RUNNING && adminJuego != null) {
            if (adminJuego.isJugadorMuerto()) {
                estadoJuego = GameState.GAME_OVER;
            } else if (adminJuego.isVictoria()) {
                estadoJuego = GameState.VICTORY;
            }
        }

        this.panelJuego.repaint();
    }
    
    // --- Métodos de Menú (sin cambios) ---
    
    private void manejarMenuPrincipal(int keyCode) {
        if (keyCode == KeyEvent.VK_1) { 
            this.estadoJuego = GameState.MENU_DIFICULTAD;
        }
        if (keyCode == KeyEvent.VK_2) { 
            this.estadoJuego = GameState.MENU_INSTRUCCIONES;
        }
        if (keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_ESCAPE) { 
            System.exit(0);
        }
    }
    
    private void manejarMenuDificultad(int keyCode) {
        if (keyCode == KeyEvent.VK_1) { 
            this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.FACIL;
            this.estadoJuego = GameState.MENU_JUGADORES;
        }
        if (keyCode == KeyEvent.VK_2) { 
            this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.NORMAL;
            this.estadoJuego = GameState.MENU_JUGADORES;
        }
        if (keyCode == KeyEvent.VK_3) { 
            this.dificultadSeleccionada = PartidaDeRolgar.Dificultad.DIFICIL;
            this.estadoJuego = GameState.MENU_JUGADORES;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            this.estadoJuego = GameState.MENU_PRINCIPAL;
        }
    }
    
    private void manejarMenuJugadores(int keyCode) {
        if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_4) {
            this.jugadoresSeleccionados = keyCode - KeyEvent.VK_0; 
            iniciarPartida();
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
            this.estadoJuego = GameState.MENU_DIFICULTAD;
        }
    }

    private void manejarMenuInstrucciones(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) { 
            this.estadoJuego = GameState.MENU_PRINCIPAL;
        }
    }

    /**
     * MÉTODO CORREGIDO: Traduce el input basándose en el estado del combate.
     */
    private void manejarInputCombate(int keyCode) {
        if (adminCombate == null) return;
        
        AdministradorDeCombate.EstadoCombate estadoCombate = adminCombate.getEstado();
        AccionCombate accion = AccionCombate.NINGUNA;

        if (estadoCombate == AdministradorDeCombate.EstadoCombate.ELIGE_ACCION) {
            switch(keyCode) {
                case KeyEvent.VK_1: accion = AccionCombate.ATACAR; break;
                case KeyEvent.VK_2: accion = AccionCombate.ABRIR_MENU_CARTA; break;
                case KeyEvent.VK_3: accion = AccionCombate.HUIR; break;
            }
        } else if (estadoCombate == AdministradorDeCombate.EstadoCombate.ELIGE_CARTA) {
            switch(keyCode) {
                case KeyEvent.VK_1: accion = AccionCombate.USAR_CARTA_1; break;
                case KeyEvent.VK_2: accion = AccionCombate.USAR_CARTA_2; break;
                case KeyEvent.VK_3: accion = AccionCombate.USAR_CARTA_3; break;
                case KeyEvent.VK_4: accion = AccionCombate.USAR_CARTA_4; break;
                case KeyEvent.VK_5: accion = AccionCombate.USAR_CARTA_5; break;
                case KeyEvent.VK_6: accion = AccionCombate.USAR_CARTA_6; break;
                case KeyEvent.VK_7: accion = AccionCombate.USAR_CARTA_7; break;
                case KeyEvent.VK_8: accion = AccionCombate.USAR_CARTA_8; break;
                case KeyEvent.VK_9: accion = AccionCombate.USAR_CARTA_9; break;
                case KeyEvent.VK_0: accion = AccionCombate.USAR_CARTA_0; break;
                case KeyEvent.VK_ESCAPE: accion = AccionCombate.CANCELAR_CARTA; break;
            }
        }
        
        if (accion != AccionCombate.NINGUNA) {
            adminCombate.procesarAccion(accion);
        }
    }
    
    // --- Métodos de Control de Partida (sin cambios) ---

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
        
        if(this.adminJuego != null) {
            this.adminJuego.actualizarVisibilidad();
        }

        if (adminJuego.isJugadorMuerto()) {
            estadoJuego = GameState.GAME_OVER;
        } else if (adminJuego.isVictoria()) {
            estadoJuego = GameState.VICTORY;
        }
    }

    private int getSlotFromAccionJuego(AccionJuego accion) {
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
}