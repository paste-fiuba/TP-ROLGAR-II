package com.ui;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;   //

public class RenderizadorMundo {

    private BufferedImage spriteRoca, spriteVacio, spriteRampa, spriteAgua;
    private BufferedImage spritePersonaje, spriteEnemigo;
    
    public RenderizadorMundo() {
        cargarSpritesDelMundo();
    }

    private void cargarSpritesDelMundo() {
        try {
            this.spriteRoca       = ImageIO.read(new File("src/sprites/roca.png"));
            this.spriteVacio      = ImageIO.read(new File("src/sprites/vacio.png")); 
            this.spriteRampa      = ImageIO.read(new File("src/sprites/rampa.png")); 
            this.spriteAgua       = ImageIO.read(new File("src/sprites/agua.png"));
            this.spritePersonaje  = ImageIO.read(new File("src/sprites/personaje.png"));
            this.spriteEnemigo    = ImageIO.read(new File("src/sprites/enemigo.png"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dibuja todos los elementos del juego (terreno, items, entidades).
     */
    public void dibujar(Graphics2D g, Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, int zActual, Personaje jugadorActivo) {
        
        int TAMAÑO_TILE = PanelJuego.TAMAÑO_TILE;

        // 1. Dibujar Terreno y Cartas en el suelo
        for (int y = 0; y < tablero.getDimensionY(); y++) {
            for (int x = 0; x < tablero.getDimensionX(); x++) {
                Casillero casillero = tablero.getCasillero(x, y, zActual);
                int pixelX = x * TAMAÑO_TILE;
                int pixelY = y * TAMAÑO_TILE;
                
                // Terreno
                if (casillero.getTipo() == TipoCasillero.ROCA) {
                    g.drawImage(this.spriteRoca, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.AGUA) { 
                    g.drawImage(this.spriteAgua, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.RAMPA) { 
                    g.drawImage(this.spriteRampa, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.VACIO) { 
                    g.drawImage(this.spriteVacio, pixelX, pixelY, null);
                }
                
                // Carta tirada en el casillero
                Carta carta = casillero.getCarta();
                if (carta != null && carta.getImagen() != null) {
                    int tamañoCarta = 24;
                    int offset = 4; // para centrar dentro del tile 32x32
                    g.drawImage(
                        carta.getImagen(),
                        pixelX + offset,
                        pixelY + offset,
                        tamañoCarta,
                        tamañoCarta,
                        null
                    );
                }
            }
        }
        
        // 2. Dibujar Enemigos
        if (this.spriteEnemigo != null) {
            for (Enemigo enemigo : enemigos) {
                if (enemigo.estaVivo() && enemigo.getPosZ() == zActual) {
                    g.drawImage(
                        this.spriteEnemigo,
                        enemigo.getPosX() * TAMAÑO_TILE,
                        enemigo.getPosY() * TAMAÑO_TILE,
                        null
                    );
                }
            }
        }
        
        // 3. Dibujar todos los jugadores vivos y sus nombres
        if (jugadores != null) {
            for (Personaje jugador : jugadores) {
                if (jugador == null) continue;
                if (jugador.getPosZ() == zActual && jugador.getVida() > 0) {
                    int pxTile = jugador.getPosX() * TAMAÑO_TILE;
                    int pyTile = jugador.getPosY() * TAMAÑO_TILE;
                    if (this.spritePersonaje != null) {
                        g.drawImage(this.spritePersonaje, pxTile, pyTile, null);
                    } else {
                        // marcador visual de respaldo cuando falta el sprite
                        g.setColor(new Color(0, 200, 0));
                        g.fillOval(pxTile + 4, pyTile + 4, TAMAÑO_TILE - 8, TAMAÑO_TILE - 8);
                    }
                    // Dibujar nombre del jugador centrado debajo del sprite
                    try {
                        String nombre = jugador.getNombre();
                        if (nombre != null && !nombre.isEmpty()) {
                            Font oldFont = g.getFont();
                            Font nameFont = new Font("Arial", Font.BOLD, 12);
                            g.setFont(nameFont);
                            int textWidth = g.getFontMetrics().stringWidth(nombre);
                            int textX = pxTile + (TAMAÑO_TILE - textWidth) / 2;
                            int textY = pyTile + TAMAÑO_TILE + 12; // debajo del tile
                            // Sombra para mejor legibilidad
                            g.setColor(Color.BLACK);
                            g.drawString(nombre, textX + 1, textY + 1);
                            // Color distinto si es el jugador activo
                            if (jugadorActivo != null && jugador == jugadorActivo) {
                                g.setColor(Color.YELLOW);
                            } else {
                                g.setColor(Color.WHITE);
                            }
                            g.drawString(nombre, textX, textY);
                            g.setFont(oldFont);
                        }
                    } catch (Exception ex) {
                        // No interrumpir el render si ocurre algún error al dibujar el nombre
                    }
                }
            }
        }
    }
}
