package com.logica;

import java.awt.event.KeyEvent;
import com.ui.PanelJuego;

/**
 * TDA Controlador: Maneja el input (teclado) y el estado del juego (Pausa).
 * Delega la lógica de juego al AdministradorDeJuego.
 */
public class ControladorJuego {

    public enum GameState { RUNNING, PAUSED }
    private GameState estadoJuego;

    private AdministradorDeJuego adminJuego;
    private PanelJuego panelJuego;

    public ControladorJuego(AdministradorDeJuego adminJuego, PanelJuego panel) {
        this.adminJuego = adminJuego;
        this.panelJuego = panel;
        this.estadoJuego = GameState.RUNNING;
    }

    public void manejarInput(int keyCode) {
        
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
            } else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
                int slotIndex = (keyCode == KeyEvent.VK_0) ? 9 : keyCode - KeyEvent.VK_1;
                adminJuego.usarCarta(slotIndex);
            }
        } 
        
        else if (estadoJuego == GameState.PAUSED) {
            if (keyCode == KeyEvent.VK_R) {
                estadoJuego = GameState.RUNNING;
            } else if (keyCode == KeyEvent.VK_Q) {
                System.exit(0);
            }
        }
        
        this.panelJuego.repaint();
    }
    
    public GameState getEstadoJuego() {
        return this.estadoJuego;
    }
}