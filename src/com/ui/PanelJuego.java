package com.ui; 

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.logica.ControladorJuego;
import com.logica.ControladorJuego.GameState;
import com.tablero.Tablero;
import java.awt.Color;
import java.awt.Dimension;
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

    private int ANCHO_LOGICO = 800;
    private int ALTO_LOGICO = 600;
    private int ALTO_JUEGO_LOGICO = 536;

    public PanelJuego() {
        this.setBackground(Color.BLACK); 
        this.renderMundo = new RenderizadorMundo();
        this.renderUI = new RenderizadorUI();
        
        this.setPreferredSize(new Dimension(ANCHO_LOGICO, ALTO_LOGICO));
    }
    
    public void setDatosDePartida(Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.enemigos = enemigos;
        this.nivelZActual = 0; 
        
        this.ANCHO_LOGICO = tablero.getDimensionX() * TAMAÑO_TILE;
        this.ALTO_JUEGO_LOGICO = tablero.getDimensionY() * TAMAÑO_TILE;
        this.ALTO_LOGICO = ALTO_JUEGO_LOGICO + ALTURA_HOTBAR; 
        
        this.setPreferredSize(new Dimension(ANCHO_LOGICO, ALTO_LOGICO));
        
        if (this.getTopLevelAncestor() instanceof javax.swing.JFrame) {
            ((javax.swing.JFrame) this.getTopLevelAncestor()).pack();
            ((javax.swing.JFrame) this.getTopLevelAncestor()).setLocationRelativeTo(null); 
        }
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
        
        if (controlador == null) return;
        GameState estado = controlador.getEstadoJuego();

        // Si estamos en un menú, solo dibujar el menú y salir
        if (estado == GameState.MENU_PRINCIPAL || 
            estado == GameState.MENU_DIFICULTAD || 
            estado == GameState.MENU_JUGADORES ||
            estado == GameState.MENU_INSTRUCCIONES) { // <-- AÑADIDO
            
            renderUI.dibujarMenu(g, estado, getWidth(), getHeight());
            return;
        }


        Graphics2D gJuego = (Graphics2D) g.create();
        try {
            double scaleX = (double) getWidth() / ANCHO_LOGICO;
            double scaleY = (double) getHeight() / ALTO_LOGICO;
            double scale = Math.min(scaleX, scaleY); 

            int scaledWidth = (int) (ANCHO_LOGICO * scale);
            int scaledHeight = (int) (ALTO_LOGICO * scale);
            int offsetX = (getWidth() - scaledWidth) / 2;
            int offsetY = (getHeight() - scaledHeight) / 2;

            gJuego.translate(offsetX, offsetY);
            gJuego.scale(scale, scale);
            
            gJuego.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            
            Personaje jugadorActual = null;
            if (this.administrador != null) {
                jugadorActual = this.administrador.getJugadorActual();
            }

            if (jugadorActual != null && this.tablero != null) {
                List<Personaje> listaParaDibujar = this.jugadores;
                if (this.administrador != null && this.administrador.getJugadores() != null) {
                    listaParaDibujar = this.administrador.getJugadores();
                }
                renderMundo.dibujar(gJuego, tablero, listaParaDibujar, enemigos, nivelZActual, jugadorActual);
                renderUI.dibujarHotbar(gJuego, jugadorActual.getInventario(), ANCHO_LOGICO, ALTO_JUEGO_LOGICO);
            }

        } finally {
            gJuego.dispose();
        }
        
        if (estado == GameState.RUNNING) {
            Personaje jugadorActual = (this.administrador != null) ? this.administrador.getJugadorActual() : null;
            int pending = (this.controlador != null) ? this.controlador.getPendingTransferSlot() : -1;
            renderUI.dibujarInfoJuego(g, jugadorActual, enemigos, (this.administrador != null ? this.administrador.getJugadores() : this.jugadores), this.administrador, pending);
        }
        else if (estado == GameState.PAUSED) {
            renderUI.dibujarMenuPausa(g, getWidth(), getHeight()); 
        }
        else if (estado == GameState.GAME_OVER) {
            renderUI.dibujarPantallaGameOver(g, getWidth(), getHeight());
        }
        else if (estado == GameState.VICTORY) {
            renderUI.dibujarPantallaVictoria(g, getWidth(), getHeight());
        }
    }

    public void setNivelZActual(int nuevoNivelZ) {
        this.nivelZActual = nuevoNivelZ;
        if (tablero != null) { 
            if (this.nivelZActual >= tablero.getDimensionZ()) this.nivelZActual = tablero.getDimensionZ() - 1;
            if (this.nivelZActual < 0) this.nivelZActual = 0;
        }
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