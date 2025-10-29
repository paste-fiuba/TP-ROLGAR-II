package com.ui; 

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;

public class PanelJuego extends JPanel {

    public static final int TAMAÑO_TILE = 32;

    private Tablero tablero;
    private int nivelZActual; 

    // --- Sprites (Bitmaps) ---
    private BufferedImage spriteRoca;
    private BufferedImage spriteVacio;  
    private BufferedImage spriteRampa; 
    private BufferedImage spriteAgua;   

    /**
     * Constructor.
     */
    public PanelJuego(Tablero tablero, int zInicial) {
        this.tablero = tablero;
        this.nivelZActual = zInicial;

        int anchoPanel = tablero.getDimensionX() * TAMAÑO_TILE;
        int altoPanel = tablero.getDimensionY() * TAMAÑO_TILE;

        this.setPreferredSize(new Dimension(anchoPanel, altoPanel));
        this.setBackground(Color.BLACK); // 
        
        cargarImagenes();
    }

    /**
     * Carga todos los archivos de imagen.
     */
    private void cargarImagenes() {
        try {
            this.spriteRoca = ImageIO.read(new File("src/sprites/roca.png"));
            this.spriteVacio = ImageIO.read(new File("src/sprites/vacio.png")); 
            this.spriteRampa = ImageIO.read(new File("src/sprites/rampa.png")); 
            this.spriteAgua = ImageIO.read(new File("src/sprites/agua.png"));

        } catch (IOException e) {
            System.err.println("Error al cargar uno o más sprites de terreno.");
            e.printStackTrace();
        }
    }

    /**
     * Se llama solo cada vez que hay que redibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Recorremos el tablero
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
                
                } else {
                    
                }
            }
        }
    }

    /**
     * Cambia el piso que se está viendo.
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

}