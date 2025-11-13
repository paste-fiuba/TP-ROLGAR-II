package com.ui; 

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.logica.ControladorJuego;
import com.logica.ControladorJuego.GameState;
import com.tablero.Tablero;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JPanel; 

public class PanelJuego extends JPanel implements KeyListener {

    public static final int TAMAÑO_TILE = 32;
    public static final int ALTURA_HOTBAR = 64;

    private Tablero tablero;
    private int nivelZActual; 
    private List<Personaje> jugadores;
    private List<Enemigo> enemigos;
    private com.logica.AdministradorDeJuego administrador;
    private ControladorJuego controlador;
    private RenderizadorMundo renderMundo;
    private RenderizadorUI renderUI;

    private final int ANCHO_LOGICO;
    private final int ALTO_LOGICO;
    private final int ALTO_JUEGO_LOGICO;

    public PanelJuego(Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, int zInicial) {
        this.tablero = tablero;
        this.jugadores = jugadores;
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

    public void setAdministrador(com.logica.AdministradorDeJuego admin) {
        this.administrador = admin;
    }
    
    public void iniciarOyenteTeclado() {
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    
    public RenderizadorUI getRenderUI() {
        return this.renderUI;
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
            
            com.entidades.Personaje jugadorActual = null;
            if (this.administrador != null) {
                jugadorActual = this.administrador.getJugadorActual();
            }
            if (jugadorActual == null && this.jugadores != null && !this.jugadores.isEmpty()) {
                jugadorActual = this.jugadores.get(0);
            }

            if (jugadorActual != null) {
                // Obtener lista de jugadores desde el administrador si está inicializado
                java.util.List<com.entidades.Personaje> listaParaDibujar = this.jugadores;
                if (this.administrador != null && this.administrador.getJugadores() != null) {
                    listaParaDibujar = this.administrador.getJugadores();
                }
                renderMundo.dibujar(gJuego, tablero, listaParaDibujar, enemigos, nivelZActual, jugadorActual);
                renderUI.dibujarHotbar(gJuego, jugadorActual.getInventario(), ANCHO_LOGICO, ALTO_JUEGO_LOGICO);
            }

        } finally {
            gJuego.dispose();
        }
        
        if (controlador == null) return;
        
        GameState estado = controlador.getEstadoJuego();

        if (estado == GameState.MENU) {
            renderUI.dibujarMenuInicio(g, getWidth(), getHeight());
            return;
        }

        if (estado == GameState.RUNNING) {
            com.entidades.Personaje jugadorActual = (this.administrador != null) ? this.administrador.getJugadorActual() : (this.jugadores != null && !this.jugadores.isEmpty() ? this.jugadores.get(0) : null);
            renderUI.dibujarInfoJuego(g, jugadorActual, enemigos);
        }
        else if (estado == GameState.PAUSED) {
            renderUI.dibujarMenuPausa(g, getWidth(), getHeight()); 
        }
        else if (estado == GameState.GAME_OVER) {
            renderUI.dibujarPantallaGameOver(g, getWidth(), getHeight());
        }
        // 1. Añadimos el chequeo para el estado de Victoria
        else if (estado == GameState.VICTORY) {
            renderUI.dibujarPantallaVictoria(g, getWidth(), getHeight());
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