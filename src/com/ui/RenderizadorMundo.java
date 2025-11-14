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
    
    /**
     * Revisa si una coordenada (x, y, z) es visible por algún jugador.
     * (Versión optimizada de tu método).
     */
    private boolean personajeCercano(List<Personaje> jugadores, int x, int y, int z) {
        for(Personaje jugador : jugadores) {
            if (jugador.getPosZ() == z) {
                // Calcula la distancia absoluta (en tiles)
                int distAbsX = Math.abs(jugador.getPosX() - x);
                int distAbsY = Math.abs(jugador.getPosY() - y);

                // Si está en un cuadrado de 3x3 (distancia de 1 o 0 en cada eje)
                if (distAbsX <= 1 && distAbsY <= 1) {
                    return true; // Un jugador puede verlo, no sigas buscando
                }
            }
        }
        return false; // Ningún jugador puede verlo
    }

    private void loadEnemySprite(String key, String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                BufferedImage img = ImageIO.read(f);
                this.spritesEnemigos.put(key, img);
            }
        } catch (IOException ex) {
            // ignora si falta el sprite
        }
    }

    public void dibujar(Graphics2D g, Tablero tablero, List<Personaje> jugadores, List<Enemigo> enemigos, int zActual, Personaje jugadorActivo) {
        
        int TAMAÑO_TILE = PanelJuego.TAMAÑO_TILE;

        // 1. Dibujar Terreno y Cartas
        for (int y = 0; y < tablero.getDimensionY(); y++) {
            for (int x = 0; x < tablero.getDimensionX(); x++) {
                
                // Solo dibuja el tile si es visible
                if (personajeCercano(jugadores, x, y, zActual)) {
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
                    
                    Carta carta = casillero.getCarta();
                    if (carta != null && carta.getImagen() != null) {
                        g.drawImage(carta.getImagen(), pixelX + 4, pixelY + 4, 24, 24, null);
                    }
                }
                // (Opcional: podrías dibujar un tile de "niebla" si no es visible)
                // else { g.setColor(Color.BLACK); g.fillRect(x * TAMAÑO_TILE, y * TAMAÑO_TILE, TAMAÑO_TILE, TAMAÑO_TILE); }
            }
        }
        
        // 2. Dibujar Enemigos
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo() || enemigo.getPosZ() != zActual) continue;

            // --- ¡CORRECCIÓN AQUÍ! ---
            // Solo dibuja el enemigo si está dentro de la visión
            if (personajeCercano(jugadores, enemigo.getPosX(), enemigo.getPosY(), zActual)) {
            
                BufferedImage img = null;
                if (this.spritesEnemigos != null) {
                    String key = normalizeKey(enemigo.getNombre());
                    img = this.spritesEnemigos.get(key);
                    if (img == null) {
                        String tipoKey = normalizeKey(enemigo.getTipo());
                        img = this.spritesEnemigos.get(tipoKey);
                    }
                }
                if (img == null) img = this.spriteEnemigo;
                if (img != null) {
                    g.drawImage(img, enemigo.getPosX() * TAMAÑO_TILE, enemigo.getPosY() * TAMAÑO_TILE, null);
                } else {
                    g.setColor(Color.RED);
                    g.fillRect(enemigo.getPosX() * TAMAÑO_TILE + 4, enemigo.getPosY() * TAMAÑO_TILE + 4, TAMAÑO_TILE - 8, TAMAÑO_TILE - 8);
                }
            }
        }
        
        // 3. Dibujar todos los jugadores (estos siempre se ven si están en el piso)
        if (jugadores != null) {
            for (Personaje jugador : jugadores) {
                if (jugador == null) continue;
                if (jugador.getPosZ() == zActual && jugador.getVida() > 0) {
                    int pxTile = jugador.getPosX() * TAMAÑO_TILE;
                    int pyTile = jugador.getPosY() * TAMAÑO_TILE;
                    if (this.spritePersonaje != null) {
                        g.drawImage(this.spritePersonaje, pxTile, pyTile, null);
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
                        // No interrumpir el render
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