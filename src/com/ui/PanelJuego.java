package com.ui; 

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.tablero.Tablero;
import com.entidades.Personaje;
import com.entidades.Enemigo;
import com.logica.ControladorJuego;
import com.logica.ControladorJuego.GameState; 

/**
 * El "Lienzo" principal. Delega el dibujo a los Renderizadores.
 * Maneja el escalado y el input (KeyListener).
 */
public class PanelJuego extends JPanel implements KeyListener {

    public static final int TAMAÑO_TILE = 32;
    public static final int ALTURA_HOTBAR = 64;

    // Referencias al Modelo
    private Tablero tablero;
    private int nivelZActual; 
    private Personaje jugador; 
    private List<Enemigo> enemigos;
    
    // Referencia al Controlador
    private ControladorJuego controlador;

    // TDA de Vista
    private RenderizadorMundo renderMundo;
    private RenderizadorUI renderUI;

    private final int ANCHO_LOGICO;
    private final int ALTO_LOGICO;
    private final int ALTO_JUEGO_LOGICO; // Alto sin la hotbar

    public PanelJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, int zInicial) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.nivelZActual = zInicial;
        
        this.ANCHO_LOGICO = tablero.getDimensionX() * TAMAÑO_TILE;
        this.ALTO_JUEGO_LOGICO = tablero.getDimensionY() * TAMAÑO_TILE;
        this.ALTO_LOGICO = ALTO_JUEGO_LOGICO + ALTURA_HOTBAR; 
        
        this.setBackground(Color.BLACK); 
        
        this.renderMundo = new RenderizadorMundo();
        this.renderUI = new RenderizadorUI();
    }
    
    public void setControlador(ControladorJuego controlador) {
        this.controlador = controlador;
    }
    
    public void iniciarOyenteTeclado() {
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D gJuego = (Graphics2D) g.create();
        
        try {
            double scaleX = (double) getWidth() / ANCHO_LOGICO;
            double scaleY = (double) getHeight() / ALTO_LOGICO;
            gJuego.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            gJuego.scale(scaleX, scaleY);
            
            // 1. Dibujar el Mundo (delegado)
            renderMundo.dibujar(gJuego, tablero, jugador, enemigos, nivelZActual);
            
            // 2. Dibujar la Hotbar (delegado)
            renderUI.dibujarHotbar(gJuego, jugador.getInventario(), ANCHO_LOGICO, ALTO_JUEGO_LOGICO);

        } finally {
            gJuego.dispose();
        }
        
        // 3. Dibujar Menú de Pausa (delegado, sin escalar)
        if (controlador != null && controlador.getEstadoJuego() == GameState.PAUSED) {
            renderUI.dibujarMenuPausa(g, getWidth(), getHeight()); 
        }
    }

    public void setNivelZActual(int nuevoNivelZ) {
        this.nivelZActual = nuevoNivelZ;
        if (this.nivelZActual >= tablero.getDimensionZ()) this.nivelZActual = tablero.getDimensionZ() - 1;
        if (this.nivelZActual < 0) this.nivelZActual = 0;
        this.repaint();
    } 

    @Override
    public void keyPressed(KeyEvent e) {
        if (controlador != null) {
            controlador.manejarInput(e.getKeyCode());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}