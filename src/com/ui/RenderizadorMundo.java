package com.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import com.tablero.Tablero;
import com.tablero.Casillero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;
import com.entidades.Enemigo;

public class RenderizadorMundo {

    private BufferedImage spriteRoca, spriteVacio, spriteRampa, spriteAgua;
    private BufferedImage spritePersonaje, spriteEnemigo, spriteCarta;
    
    public RenderizadorMundo() {
        cargarSpritesDelMundo();
    }

    private void cargarSpritesDelMundo() {
        try {
            this.spriteRoca = ImageIO.read(new File("src/sprites/roca.png"));
            this.spriteVacio = ImageIO.read(new File("src/sprites/vacio.png")); 
            this.spriteRampa = ImageIO.read(new File("src/sprites/rampa.png")); 
            this.spriteAgua = ImageIO.read(new File("src/sprites/agua.png"));
            this.spritePersonaje = ImageIO.read(new File("src/sprites/personaje.png"));
            this.spriteEnemigo = ImageIO.read(new File("src/sprites/enemigo.png"));
            this.spriteCarta = ImageIO.read(new File("src/sprites/carta.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dibuja todos los elementos del juego (terreno, items, entidades).
     */
    public void dibujar(Graphics2D g, Tablero tablero, Personaje jugador, List<Enemigo> enemigos, int zActual) {
        
        int TAMAÑO_TILE = PanelJuego.TAMAÑO_TILE; // Tomamos la constante

        // 1. Dibujar Terreno y Cartas en el suelo
        for (int y = 0; y < tablero.getDimensionY(); y++) {
            for (int x = 0; x < tablero.getDimensionX(); x++) {
                Casillero casillero = tablero.getCasillero(x, y, zActual);
                int pixelX = x * TAMAÑO_TILE;
                int pixelY = y * TAMAÑO_TILE;
                
                if (casillero.getTipo() == TipoCasillero.ROCA) {
                    g.drawImage(this.spriteRoca, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.AGUA) { 
                    g.drawImage(this.spriteAgua, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.RAMPA) { 
                    g.drawImage(this.spriteRampa, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.VACIO) { 
                    g.drawImage(this.spriteVacio, pixelX, pixelY, null);
                }
                
                if (casillero.getCarta() != null && this.spriteCarta != null) {
                    g.drawImage(this.spriteCarta, pixelX + 4, pixelY + 4, 24, 24, null);
                }
            }
        }
        
        // 2. Dibujar Enemigos
        if (this.spriteEnemigo != null) {
            for (Enemigo enemigo : enemigos) {
                if (enemigo.estaVivo() && enemigo.getPosZ() == zActual) {
                    g.drawImage(this.spriteEnemigo, enemigo.getPosX() * TAMAÑO_TILE, enemigo.getPosY() * TAMAÑO_TILE, null);
                }
            }
        }
        
        // 3. Dibujar Personaje
        if (jugador.getPosZ() == zActual && this.spritePersonaje != null) {
            g.drawImage(this.spritePersonaje, jugador.getPosX() * TAMAÑO_TILE, jugador.getPosY() * TAMAÑO_TILE, null);
        }
    }
}