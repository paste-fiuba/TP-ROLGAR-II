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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class RenderizadorMundo {

    private GerenciadorDeSprites sprites; 
    
    /**
     * Constructor modificado: ahora recibe el gerenciador de sprites.
     */
    public RenderizadorMundo(GerenciadorDeSprites gerenciadorSprites) {
        this.sprites = gerenciadorSprites;
    }

    private boolean personajeCercano(List<Personaje> jugadores, int x, int y, int z) {
        for(Personaje jugador : jugadores) {
            if (jugador.getPosZ() == z) {
                int distAbsX = Math.abs(jugador.getPosX() - x);
                int distAbsY = Math.abs(jugador.getPosY() - y);
                if (distAbsX <= 1 && distAbsY <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public void dibujar(Graphics2D g, Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, int zActual, Personaje jugadorActivo) {
        
        int TAMAÑO_TILE = PanelJuego.TAMAÑO_TILE;

        // Dibujar Terreno y Cartas
        for (int y = 0; y < tablero.getDimensionY(); y++) {
            for (int x = 0; x < tablero.getDimensionX(); x++) {
                
                if (personajeCercano(jugadores, x, y, zActual)) {
                    Casillero casillero = tablero.getCasillero(x, y, zActual);
                    int pixelX = x * TAMAÑO_TILE;
                    int pixelY = y * TAMAÑO_TILE;
    
                    if (casillero.getTipo() == TipoCasillero.ROCA) {
                        g.drawImage(sprites.getSpriteRoca(), pixelX, pixelY, null);
                    } else if (casillero.getTipo() == TipoCasillero.AGUA) { 
                        g.drawImage(sprites.getSpriteAgua(), pixelX, pixelY, null);
                    } else if (casillero.getTipo() == TipoCasillero.RAMPA) { 
                        g.drawImage(sprites.getSpriteRampa(), pixelX, pixelY, null);
                    } else if (casillero.getTipo() == TipoCasillero.VACIO) { 
                        g.drawImage(sprites.getSpriteVacio(), pixelX, pixelY, null);
                    }
                    
                    Carta carta = casillero.getCarta();
                    if (carta != null) {
                        BufferedImage imgCarta = sprites.getSpriteCarta(carta.getNombre());
                        
                        if (imgCarta != null) {
                            g.drawImage(imgCarta, pixelX + 4, pixelY + 4, 24, 24, null);
                        }
                    }
                }
            }
        }
        
        // Dibujar Enemigos
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo() || enemigo.getPosZ() != zActual) continue;

            if (personajeCercano(jugadores, enemigo.getPosX(), enemigo.getPosY(), zActual)) {
            
                BufferedImage img = sprites.getSpriteEnemigo(enemigo.getNombre(), enemigo.getTipo());
                
                if (img != null) {
                    g.drawImage(img, enemigo.getPosX() * TAMAÑO_TILE, enemigo.getPosY() * TAMAÑO_TILE, null);
                } else {
                    g.setColor(Color.RED);
                    g.fillRect(enemigo.getPosX() * TAMAÑO_TILE + 4, enemigo.getPosY() * TAMAÑO_TILE + 4, TAMAÑO_TILE - 8, TAMAÑO_TILE - 8);
                }
            }
        }
        
        // Dibujar todos los jugadores
        if (jugadores != null) {
            for (Personaje jugador : jugadores) {
                if (jugador == null) continue;
                if (jugador.getPosZ() == zActual && jugador.getVida() > 0) {
                    int pxTile = jugador.getPosX() * TAMAÑO_TILE;
                    int pyTile = jugador.getPosY() * TAMAÑO_TILE;
                    
                    BufferedImage imgJugador = sprites.getSpritePersonaje(); 
                    
                    if (imgJugador != null) {
                        g.drawImage(imgJugador, pxTile, pyTile, null);
                    } else {
                        g.setColor(new Color(0, 200, 0));
                        g.fillOval(pxTile + 4, pyTile + 4, TAMAÑO_TILE - 8, TAMAÑO_TILE - 8);
                    }
                    
                    try {
                        String nombre = jugador.getNombre();
                        if (nombre != null && !nombre.isEmpty()) {
                            Font oldFont = g.getFont();
                            Font nameFont = new Font("Arial", Font.BOLD, 12);
                            g.setFont(nameFont);
                            int textWidth = g.getFontMetrics().stringWidth(nombre);
                            int textX = pxTile + (TAMAÑO_TILE - textWidth) / 2;
                            int textY = pyTile + TAMAÑO_TILE + 12;
                            g.setColor(Color.BLACK);
                            g.drawString(nombre, textX + 1, textY + 1);
                            if (jugadorActivo != null && jugador == jugadorActivo) {
                                g.setColor(Color.YELLOW);
                            } else {
                                g.setColor(Color.WHITE);
                            }
                            g.drawString(nombre, textX, textY);
                            g.setFont(oldFont);
                        }
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }
}