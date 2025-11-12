package com.ui; 

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Imports del Modelo
import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;

// Import del Controlador
import com.logica.ControladorJuego; 

/**
 * La "Vista" (Panel). Dibuja el estado del juego y captura el input.
 * Implementa KeyListener para capturar las pulsaciones de teclas.
 */
public class PanelJuego extends JPanel implements KeyListener {

    public static final int TAMAÑO_TILE = 32;

    // Referencias al Modelo
    private Tablero tablero;
    private int nivelZActual; 
    private Personaje jugador; 
    
    // Referencia al Controlador
    private ControladorJuego controlador;

    // Sprites
    private BufferedImage spriteRoca;
    private BufferedImage spriteVacio;  
    private BufferedImage spriteRampa; 
    private BufferedImage spriteAgua;   
    private BufferedImage spritePersonaje;

    /**
     * Constructor. Recibe los datos del Modelo.
     */
    public PanelJuego(Tablero tablero, Personaje jugador, int zInicial) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.nivelZActual = zInicial;
        
        int anchoPanel = tablero.getDimensionX() * TAMAÑO_TILE;
        int altoPanel = tablero.getDimensionY() * TAMAÑO_TILE;
        this.setPreferredSize(new Dimension(anchoPanel, altoPanel));
        
        this.setBackground(Color.BLACK); 
        cargarImagenes();
    }
    
    /**
     * Permite al Main "inyectar" el controlador.
     */
    public void setControlador(ControladorJuego controlador) {
        this.controlador = controlador;
    }
    
    /**
     * Activa el oyente de teclado.
     */
    public void iniciarOyenteTeclado() {
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Carga todos los archivos de imagen (sprites).
     */
    private void cargarImagenes() {
        try {
            this.spriteRoca = ImageIO.read(new File("src/sprites/roca.png"));
            this.spriteVacio = ImageIO.read(new File("src/sprites/vacio.png")); 
            this.spriteRampa = ImageIO.read(new File("src/sprites/rampa.png")); 
            this.spriteAgua = ImageIO.read(new File("src/sprites/agua.png"));
            this.spritePersonaje = ImageIO.read(new File("src/sprites/personaje.png"));
        } catch (IOException e) {
            System.err.println("Error al cargar uno o más sprites.");
            e.printStackTrace();
        }
    }

    /**
     * Lógica de dibujo. Se llama automáticamente (ej. con repaint()).
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Dibujar Terreno
        for (int y = 0; y < tablero.getDimensionY(); y++) {
            for (int x = 0; x < tablero.getDimensionX(); x++) {
                
                Casillero casillero = tablero.getCasillero(x, y, this.nivelZActual);
                int pixelX = x * TAMAÑO_TILE;
                int pixelY = y * TAMAÑO_TILE;
                
                if (casillero.getTipo() == TipoCasillero.ROCA) {
                    g.drawImage(this.spriteRoca, pixelX, pixelY, this);
                } else if (casillero.getTipo() == TipoCasillero.AGUA) { 
                    g.drawImage(this.spriteAgua, pixelX, pixelY, this);
                } else if (casillero.getTipo() == TipoCasillero.RAMPA) { 
                    g.drawImage(this.spriteRampa, pixelX, pixelY, this);
                } else if (casillero.getTipo() == TipoCasillero.VACIO) { 
                    g.drawImage(this.spriteVacio, pixelX, pixelY, this);
                }
            }
        }
        
        // 2. Dibujar Personaje (encima del terreno)
        if (jugador.getPosZ() == this.nivelZActual) {
            int pixelX = jugador.getPosX() * TAMAÑO_TILE;
            int pixelY = jugador.getPosY() * TAMAÑO_TILE;
            if (this.spritePersonaje != null) {
                g.drawImage(this.spritePersonaje, pixelX, pixelY, this);
            }
        }
    }

    /**
     * Cambia el piso que se está viendo y fuerza el redibujado.
     */
    public void setNivelZActual(int nuevoNivelZ) {
        this.nivelZActual = nuevoNivelZ;
        
        if (this.nivelZActual >= tablero.getDimensionZ()) {
            this.nivelZActual = tablero.getDimensionZ() - 1;
        }
        if (this.nivelZActual < 0) {
            this.nivelZActual = 0;
        }

        this.repaint();
    } 

    // --- MÉTODOS DE KEYLISTENER ---

    /**
     * Se llama cuando se presiona una tecla.
     * Delega la lógica al Controlador.
     */
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