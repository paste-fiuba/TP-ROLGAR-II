package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.tablero.Tablero;
import com.ui.PanelJuego;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorJuego {

    // Estados del juego
    public enum GameState { MENU, RUNNING, PAUSED, GAME_OVER, VICTORY }
    private GameState estadoJuego;

    private AdministradorDeJuego adminJuego;
    private PanelJuego panelJuego;
    // Estado transitorio para transferencia de cartas: -1 = none, -2 = esperando slot
    private int pendingTransferSlot = -1;

    // Recursos para crear la partida desde el menú
    private Tablero tablero;
    private List<Enemigo> enemigos;

    // Constructor para iniciar en modo menú
    public ControladorJuego(Tablero tablero, List<Enemigo> enemigos, PanelJuego panel) {
        this.tablero = tablero;
        this.enemigos = enemigos;
        this.panelJuego = panel;
        this.estadoJuego = GameState.MENU;
    }

    public void manejarInput(int keyCode) {
        if (estadoJuego == GameState.MENU) {
            // En el menú, permitir elegir 1-4 jugadores con teclas 1..4
            if (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_4) {
                int cantidad = keyCode - KeyEvent.VK_0; // '1' -> 1
                iniciarPartida(cantidad);
            }
            // Escape para salir
            if (keyCode == KeyEvent.VK_ESCAPE) System.exit(0);
            this.panelJuego.repaint();
            return;
        }

        if (estadoJuego != GameState.GAME_OVER && estadoJuego != GameState.VICTORY) {
            if (adminJuego != null) adminJuego.limpiarLogCombate();
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (estadoJuego == GameState.RUNNING) {
                estadoJuego = GameState.PAUSED;
            } else if (estadoJuego == GameState.PAUSED) {
                estadoJuego = GameState.RUNNING;
            }
        }

        else if (estadoJuego == GameState.RUNNING) {
            if (adminJuego == null) return;
            // Movimiento
            if (keyCode == KeyEvent.VK_W) {
                boolean moved = adminJuego.procesarMovimiento(0, -1);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_S) {
                boolean moved = adminJuego.procesarMovimiento(0, 1);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_A) {
                boolean moved = adminJuego.procesarMovimiento(-1, 0);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_D) {
                boolean moved = adminJuego.procesarMovimiento(1, 0);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_Q) {
                // Diagonal adelante-izquierda (W + A)
                boolean moved = adminJuego.procesarMovimiento(-1, -1);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_E) {
                // Diagonal adelante-derecha (W + D)
                boolean moved = adminJuego.procesarMovimiento(1, -1);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_Z) {
                // Diagonal atras-izquierda (S + A)
                boolean moved = adminJuego.procesarMovimiento(-1, 1);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_C) {
                // Diagonal atras-derecha (S + D)
                boolean moved = adminJuego.procesarMovimiento(1, 1);
                if (moved) {
                    com.entidades.Personaje jugador = adminJuego.getJugadorActual();
                    if (jugador != null && jugador.getMovimientosExtra() > 0) {
                        jugador.setMovimientosExtra(jugador.getMovimientosExtra() - 1);
                    } else {
                        adminJuego.finalizarTurno();
                    }
                }
            } else if (keyCode == KeyEvent.VK_F) {
            } else if (keyCode == KeyEvent.VK_F) {
                // Atacar a un jugador adyacente (PvP)
                com.entidades.Personaje atacante = adminJuego.getJugadorActual();
                if (atacante != null) {
                    com.entidades.Personaje objetivo = null;
                    for (com.entidades.Personaje p : adminJuego.getJugadores()) {
                        if (p == null || p == atacante) continue;
                        if (p.getVida() <= 0) continue;
                        if (adminJuego.sonAdyacentes(atacante, p)) { objetivo = p; break; }
                    }
                    if (objetivo != null) {
                        adminJuego.atacarJugador(atacante, objetivo);
                    } else {
                        if (panelJuego != null && panelJuego.getRenderUI() != null) panelJuego.getRenderUI().agregarMensajeLog("No hay jugadores adyacentes para atacar.");
                    }
                }
            } else if (keyCode == KeyEvent.VK_T) {
                // Iniciar modo transferencia: esperar la tecla de slot (1..0)
                pendingTransferSlot = -2;
                if (panelJuego != null && panelJuego.getRenderUI() != null) {
                    panelJuego.getRenderUI().agregarMensajeLog("Transferencia: presioná 1..0 para elegir el slot a transferir.");
                }
            } else if (keyCode == KeyEvent.VK_ENTER) {
                // Finalizar el turno del jugador actual
                adminJuego.finalizarTurno();
            } else if (keyCode == KeyEvent.VK_L) {
                // Proponer alianza con un jugador adyacente no aliado (si existe)
                com.entidades.Personaje actual = adminJuego.getJugadorActual();
                if (actual != null) {
                    com.entidades.Personaje objetivo = null;
                    for (com.entidades.Personaje p : adminJuego.getJugadores()) {
                        if (p == null || p == actual) continue;
                        if (p.getVida() <= 0) continue;
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
                // Aceptar propuesta en tu turno
                com.entidades.Personaje actual = adminJuego.getJugadorActual();
                if (actual != null) adminJuego.aceptarPropuesta(actual);
            } else if (keyCode == KeyEvent.VK_N) {
                com.entidades.Personaje actual = adminJuego.getJugadorActual();
                if (actual != null) adminJuego.rechazarPropuesta(actual);
            }
            else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1;
                // Si estamos en modo transferencia, tratar de transferir ese slot
                if (pendingTransferSlot != -1) {
                    com.entidades.Personaje from = adminJuego.getJugadorActual();
                    if (from == null) {
                        pendingTransferSlot = -1;
                    } else {
                        // buscar primer aliado adyacente
                        com.entidades.Personaje objetivo = null;
                        for (com.entidades.Personaje p : adminJuego.getJugadores()) {
                            if (p == null || p == from) continue;
                            if (p.getVida() <= 0) continue;
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
                    adminJuego.activarCarta(slotIndex);
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

    public GameState getEstadoJuego() {
        return this.estadoJuego;
    }

    public int getPendingTransferSlot() {
        return this.pendingTransferSlot;
    }

    /**
     * Crea la partida con la cantidad de jugadores seleccionada desde el menú.
     */
    public void iniciarPartida(int cantidadJugadores) {
        List<Personaje> jugadores = new ArrayList<>();
        for (int i = 0; i < cantidadJugadores; i++) {
            int startX = 5 + (i * 3);
            int startY = 5;
            Personaje p = new Personaje("Jugador" + (i + 1), 100, startX, startY, 0, 10, 1, 5.0);
            // cartas iniciales simples
            try {
                p.agregarCarta(new com.items.CartaAtaqueDoble());
                p.agregarCarta(new com.items.CartaEscudo());
            } catch (Exception ex) {
                // ignore
            }
            jugadores.add(p);
        }

        // Crear administrador con los jugadores y enemigos existentes
        this.adminJuego = new AdministradorDeJuego(tablero, jugadores, enemigos, panelJuego);
        panelJuego.setAdministrador(adminJuego);
        // Asegurar que la vista muestre el nivel del primer jugador
        com.entidades.Personaje primer = this.adminJuego.getJugadorActual();
        if (primer != null) panelJuego.setNivelZActual(primer.getPosZ());
        this.estadoJuego = GameState.RUNNING;
        panelJuego.repaint();
    }
}