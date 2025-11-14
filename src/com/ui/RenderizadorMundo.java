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
import javax.imageio.ImageIO;   //

public class RenderizadorMundo {

    private BufferedImage spriteRoca, spriteVacio, spriteRampa, spriteAgua;
    private BufferedImage spritePersonaje, spriteEnemigo;
    private Map<String, BufferedImage> spritesEnemigos;
    
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
            
            // Sprites por tipo/nombre de enemigo
            this.spritesEnemigos = new HashMap<>();
            loadEnemySprite("orco", "src/sprites/ogro.png");
            loadEnemySprite("esqueleto", "src/sprites/esqueleto.png");
            loadEnemySprite("murcielago", "src/sprites/murcielago.png");
            loadEnemySprite("golem", "src/sprites/golem.png");
            loadEnemySprite("serpiente", "src/sprites/serpiente.png");
            loadEnemySprite("mago", "src/sprites/mago.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean personajeCercano(List<Personaje> jugadores, int x, int y, int z) {
    	boolean respuesta = false;
    	for(Personaje jugador : jugadores) {
    		if(((jugador.getPosX()==x && (jugador.getPosY()==y || jugador.getPosY()==y-1 || jugador.getPosY()==y+1)) || (jugador.getPosX()==x-1 && (jugador.getPosY()==y || jugador.getPosY()==y-1 || jugador.getPosY()==y+1)) || (jugador.getPosX()==x+1 && (jugador.getPosY()==y || jugador.getPosY()==y-1 || jugador.getPosY()==y+1))) && jugador.getPosZ()==z) {
    			respuesta=true;
    		}
    	}
    	
    	return respuesta;
    }

    private void loadEnemySprite(String key, String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                BufferedImage img = ImageIO.read(f);
                this.spritesEnemigos.put(key, img);
            }
        } catch (IOException ex) {
            // ignore missing enemy sprite
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
                if (casillero.getTipo() == TipoCasillero.ROCA && personajeCercano(jugadores, x, y, zActual)) {
                    g.drawImage(this.spriteRoca, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.AGUA && personajeCercano(jugadores, x, y, zActual)) { 
                    g.drawImage(this.spriteAgua, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.RAMPA && personajeCercano(jugadores, x, y, zActual)) { 
                    g.drawImage(this.spriteRampa, pixelX, pixelY, null);
                } else if (casillero.getTipo() == TipoCasillero.VACIO && personajeCercano(jugadores, x, y, zActual)) { 
                    g.drawImage(this.spriteVacio, pixelX, pixelY, null);
                }
                
                // Carta tirada en el casillero
                Carta carta = casillero.getCarta();
                if (carta != null && carta.getImagen() != null && personajeCercano(jugadores, x, y, zActual)) {
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
        
        // 2. Dibujar Enemigos (usar sprite específico si existe)
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo() || enemigo.getPosZ() != zActual) continue;
            BufferedImage img = null;
            if (this.spritesEnemigos != null) {
                String key = normalizeKey(enemigo.getNombre());
                img = this.spritesEnemigos.get(key);
                if (img == null) {
                    // intentar por tipo
                    String tipoKey = normalizeKey(enemigo.getTipo());
                    img = this.spritesEnemigos.get(tipoKey);
                }
            }
            if (img == null) img = this.spriteEnemigo;
            if (img != null) {
                g.drawImage(img, enemigo.getPosX() * TAMAÑO_TILE, enemigo.getPosY() * TAMAÑO_TILE, null);
            } else {
                // fallback visual
                g.setColor(Color.RED);
                g.fillRect(enemigo.getPosX() * TAMAÑO_TILE + 4, enemigo.getPosY() * TAMAÑO_TILE + 4, TAMAÑO_TILE - 8, TAMAÑO_TILE - 8);
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

    private String normalizeKey(String s) {
        if (s == null) return "";
        String key = s.toLowerCase();
        key = key.replaceAll("[áàäâ]", "a");
        key = key.replaceAll("[éèëê]", "e");
        key = key.replaceAll("[íìïî]", "i");
        key = key.replaceAll("[óòöô]", "o");
        key = key.replaceAll("[úùüû]", "u");
        key = key.replaceAll("[^a-z0-9]", "");
        return key;
    }
}
