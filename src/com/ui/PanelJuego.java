package com.ui; 

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;
import com.entidades.Enemigo;
import com.items.Inventario; 
import com.logica.ControladorJuego;
import com.logica.ControladorJuego.GameState; 

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

    private final int ANCHO_LOGICO;
    private final int ALTO_LOGICO;
    
    private Font fontMenuTitulo;
    private Font fontMenuOpcion;

    public PanelJuego(Tablero tablero, Personaje jugador, List<Enemigo> enemigos, int zInicial) {
        this.tablero = tablero;
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.nivelZActual = zInicial;
        
        this.ANCHO_LOGICO = tablero.getDimensionX() * TAMAÑO_TILE;
        this.ALTO_LOGICO = (tablero.getDimensionY() * TAMAÑO_TILE) + ALTURA_HOTBAR; 
        
        this.setBackground(Color.BLACK); 
        cargarImagenes();
        
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
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
        
        // Creamos una copia de Graphics para dibujar el juego escalado
        Graphics2D gJuego = (Graphics2D) g.create();
        
        try {
            // --- 1. Calcular y aplicar escalado (SOLO a gJuego) ---
            double scaleX = (double) getWidth() / ANCHO_LOGICO;
            double scaleY = (double) getHeight() / ALTO_LOGICO;
            gJuego.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            gJuego.scale(scaleX, scaleY);
            
            // --- 2. Dibujar Terreno (con gJuego) ---
            for (int y = 0; y < tablero.getDimensionY(); y++) {
                for (int x = 0; x < tablero.getDimensionX(); x++) {
                    Casillero casillero = tablero.getCasillero(x, y, this.nivelZActual);
                    int pixelX = x * TAMAÑO_TILE;
                    int pixelY = y * TAMAÑO_TILE;
                    
                    if (casillero.getTipo() == TipoCasillero.ROCA) {
                        gJuego.drawImage(this.spriteRoca, pixelX, pixelY, this);
                    } else if (casillero.getTipo() == TipoCasillero.AGUA) { 
                        gJuego.drawImage(this.spriteAgua, pixelX, pixelY, this);
                    } else if (casillero.getTipo() == TipoCasillero.RAMPA) { 
                        gJuego.drawImage(this.spriteRampa, pixelX, pixelY, this);
                    } else if (casillero.getTipo() == TipoCasillero.VACIO) { 
                        gJuego.drawImage(this.spriteVacio, pixelX, pixelY, this);
                    }
                    
                    if (casillero.getCarta() != null && this.spriteCarta != null) {
                        gJuego.drawImage(this.spriteCarta, pixelX + 4, pixelY + 4, 24, 24, this);
                    }
                }
            }
            
            // --- 3. Dibujar Enemigos (con gJuego) ---
            if (this.spriteEnemigo != null) {
                for (Enemigo enemigo : enemigos) {
                    if (enemigo.estaVivo() && enemigo.getPosZ() == this.nivelZActual) {
                        gJuego.drawImage(this.spriteEnemigo, enemigo.getPosX() * TAMAÑO_TILE, enemigo.getPosY() * TAMAÑO_TILE, this);
                    }
                }
            }
            
            // --- 4. Dibujar Personaje (con gJuego) ---
            if (jugador.getPosZ() == this.nivelZActual && this.spritePersonaje != null) {
                gJuego.drawImage(this.spritePersonaje, jugador.getPosX() * TAMAÑO_TILE, jugador.getPosY() * TAMAÑO_TILE, this);
            }
            
            // --- 5. Dibujar la UI (Hotbar) (con gJuego) ---
            dibujarHotbar(gJuego);

        } finally {
            // Liberamos la copia de Graphics
            gJuego.dispose();
        }
        
        // --- 6. Dibujar Menú de Pausa (con 'g' original, sin escalar) ---
        if (controlador != null && controlador.getEstadoJuego() == GameState.PAUSED) {
            dibujarMenuPausa(g); 
        }
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
    
    private void dibujarMenuPausa(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.WHITE);
        
        g.setFont(fontMenuTitulo);
        String titulo = "JUEGO PAUSADO";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (getWidth() - anchoTitulo) / 2, getHeight() / 2 - 50);

        g.setFont(fontMenuOpcion);
        String opcion1 = "[R] Reanudar";
        String opcion2 = "[Q] Cerrar Juego";
        
        int anchoOpcion1 = g.getFontMetrics().stringWidth(opcion1);
        int anchoOpcion2 = g.getFontMetrics().stringWidth(opcion2);
        
        g.drawString(opcion1, (getWidth() - anchoOpcion1) / 2, getHeight() / 2 + 20);
        g.drawString(opcion2, (getWidth() - anchoOpcion2) / 2, getHeight() / 2 + 60);
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