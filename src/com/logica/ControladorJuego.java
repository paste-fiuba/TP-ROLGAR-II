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
            if (keyCode == KeyEvent.VK_W) {
                adminJuego.procesarMovimiento(0, -1);
            } else if (keyCode == KeyEvent.VK_S) {
                adminJuego.procesarMovimiento(0, 1);
            } else if (keyCode == KeyEvent.VK_A) {
                adminJuego.procesarMovimiento(-1, 0);
            } else if (keyCode == KeyEvent.VK_D) {
                adminJuego.procesarMovimiento(1, 0);
            } else if (keyCode == KeyEvent.VK_ENTER) {
                // Finalizar el turno del jugador actual
                adminJuego.finalizarTurno();
            } 
            else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1;
                adminJuego.activarCarta(slotIndex);
            }
        } 
        
        else if (estadoJuego == GameState.PAUSED) {
            if (keyCode == KeyEvent.VK_R) {
                estadoJuego = GameState.RUNNING;
            } else if (keyCode == KeyEvent.VK_Q) {
                System.exit(0);
            }
        }
        
        // Game over / victory handling
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