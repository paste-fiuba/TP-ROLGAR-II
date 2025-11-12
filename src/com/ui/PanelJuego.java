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
import java.util.List;

import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;
import com.entidades.Enemigo;
import com.items.Inventario; 
import com.logica.ControladorJuego; 

public class PanelJuego extends JPanel implements KeyListener {

    public static final int TAMAÑO_TILE = 32;
    public static final int ALTURA_HOTBAR = 64;

    private Tablero tablero;
    private int nivelZActual; 
    private Personaje jugador; 
    private List<Enemigo> enemigos;
    private ControladorJuego controlador;

    private BufferedImage spriteRoca, spriteVacio, spriteRampa, spriteAgua;
    private BufferedImage spritePersonaje, spriteEnemigo, spriteSlot, spriteCarta; 

    public PanelJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, int zInicial) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.nivelZActual = zInicial;
        
        int anchoPanel = tablero.getDimensionX() * TAMAÑO_TILE;
        int altoPanel = (tablero.getDimensionY() * TAMAÑO_TILE) + ALTURA_HOTBAR; 
        this.setPreferredSize(new Dimension(anchoPanel, altoPanel));
        
        this.setBackground(Color.BLACK); 
        cargarImagenes();
    }
    
    public void setControlador(ControladorJuego controlador) {
        this.controlador = controlador;
    }
    
    public void iniciarOyenteTeclado() {
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    private void cargarImagenes() {
        try {
            this.spriteRoca = ImageIO.read(new File("src/sprites/roca.png"));
            this.spriteVacio = ImageIO.read(new File("src/sprites/vacio.png")); 
            this.spriteRampa = ImageIO.read(new File("src/sprites/rampa.png")); 
            this.spriteAgua = ImageIO.read(new File("src/sprites/agua.png"));
            this.spritePersonaje = ImageIO.read(new File("src/sprites/personaje.png"));
            this.spriteEnemigo = ImageIO.read(new File("src/sprites/enemigo.png"));
            this.spriteSlot = ImageIO.read(new File("src/sprites/slot.png"));
            this.spriteCarta = ImageIO.read(new File("src/sprites/carta.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Dibujar Terreno (y cartas en el suelo)
        for (int y = 0; y < tablero.getDimensionY(); y++) {
            for (int x = 0; x < tablero.getDimensionX(); x++) {
                
                Casillero casillero = tablero.getCasillero(x, y, this.nivelZActual);
                int pixelX = x * TAMAÑO_TILE;
                int pixelY = y * TAMAÑO_TILE;
                
                // Dibuja el tipo de terreno (piso, roca, etc.)
                if (casillero.getTipo() == TipoCasillero.ROCA) {
                    g.drawImage(this.spriteRoca, pixelX, pixelY, this);
                } else if (casillero.getTipo() == TipoCasillero.AGUA) { 
                    g.drawImage(this.spriteAgua, pixelX, pixelY, this);
                } else if (casillero.getTipo() == TipoCasillero.RAMPA) { 
                    g.drawImage(this.spriteRampa, pixelX, pixelY, this);
                } else if (casillero.getTipo() == TipoCasillero.VACIO) { 
                    g.drawImage(this.spriteVacio, pixelX, pixelY, this);
                }
                
                // Dibuja la CARTA encima del terreno (si hay una)
                if (casillero.getCarta() != null && this.spriteCarta != null) {
                    int tamañoCarta = 24; // Más pequeña que el tile
                    int padding = (TAMAÑO_TILE - tamañoCarta) / 2;
                    g.drawImage(this.spriteCarta, pixelX + padding, pixelY + padding, tamañoCarta, tamañoCarta, this);
                }
            }
        }
        
        // 2. Dibujar Enemigos
        if (this.spriteEnemigo != null) {
            for (Enemigo enemigo : enemigos) {
                if (enemigo.estaVivo() && enemigo.getPosZ() == this.nivelZActual) {
                    g.drawImage(this.spriteEnemigo, enemigo.getPosX() * TAMAÑO_TILE, enemigo.getPosY() * TAMAÑO_TILE, this);
                }
            }
        }
        
        // 3. Dibujar Personaje
        if (jugador.getPosZ() == this.nivelZActual && this.spritePersonaje != null) {
            g.drawImage(this.spritePersonaje, jugador.getPosX() * TAMAÑO_TILE, jugador.getPosY() * TAMAÑO_TILE, this);
        }
        
        // 4. Dibujar la UI (Hotbar)
        dibujarHotbar(g);
    }

    private void dibujarHotbar(Graphics g) {
        
        int anchoMundoPx = tablero.getDimensionX() * TAMAÑO_TILE;
        int altoMundoPx = tablero.getDimensionY() * TAMAÑO_TILE;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, altoMundoPx, anchoMundoPx, ALTURA_HOTBAR);

        if (this.spriteSlot == null) return;
        
        int numSlots = 10;
        int tamañoSlot = 48;
        int padding = (ALTURA_HOTBAR - tamañoSlot) / 2;
        int anchoTotalSlots = (numSlots * tamañoSlot) + ((numSlots - 1) * 5);
        int startX = (anchoMundoPx - anchoTotalSlots) / 2;
        
        Inventario inventario = jugador.getInventario();
        
        for (int i = 0; i < numSlots; i++) {
            int x = startX + (i * (tamañoSlot + 5));
            int y = altoMundoPx + padding;
            
            g.drawImage(this.spriteSlot, x, y, tamañoSlot, tamañoSlot, this);
            
            if (i < inventario.cantidadDeCartas() && this.spriteCarta != null) {
                int tamañoCarta = 40;
                int paddingCarta = (tamañoSlot - tamañoCarta) / 2;
                g.drawImage(this.spriteCarta, x + paddingCarta, y + paddingCarta, tamañoCarta, tamañoCarta, this);
            }
        }
    }

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