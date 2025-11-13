package com.logica;

import java.awt.event.KeyEvent;
import com.ui.PanelJuego;

public class ControladorJuego {

    // 1. Añadimos el nuevo estado VICTORY
    public enum GameState { RUNNING, PAUSED, GAME_OVER, VICTORY }
    private GameState estadoJuego;

    private AdministradorDeJuego adminJuego;
    private PanelJuego panelJuego;

    public ControladorJuego(AdministradorDeJuego adminJuego, PanelJuego panel) {
        this.adminJuego = adminJuego;
        this.panelJuego = panel;
        this.estadoJuego = GameState.RUNNING;
    }

    public void manejarInput(int keyCode) {
        
        if (estadoJuego != GameState.GAME_OVER && estadoJuego != GameState.VICTORY) {
            adminJuego.limpiarLogCombate();
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (estadoJuego == GameState.RUNNING) {
                estadoJuego = GameState.PAUSED;
            } else if (estadoJuego == GameState.PAUSED) {
                estadoJuego = GameState.RUNNING;
            }
        } 
        
        else if (estadoJuego == GameState.RUNNING) {
            if (keyCode == KeyEvent.VK_W) {
                adminJuego.procesarMovimiento(0, -1);
            } else if (keyCode == KeyEvent.VK_S) {
                adminJuego.procesarMovimiento(0, 1);
            } else if (keyCode == KeyEvent.VK_A) {
                adminJuego.procesarMovimiento(-1, 0);
            } else if (keyCode == KeyEvent.VK_D) {
                adminJuego.procesarMovimiento(1, 0);
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
        
        // 2. Añadimos la lógica para Game Over y Victoria
        else if (estadoJuego == GameState.GAME_OVER || estadoJuego == GameState.VICTORY) {
            if (keyCode == KeyEvent.VK_Q) {
                System.exit(0);
            }
        }
        
        // 3. Revisamos el estado del juego DESPUÉS de cada acción
        if (estadoJuego == GameState.RUNNING) {
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
}