package com.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class GerenciadorDeSprites {

    private BufferedImage spriteRoca, spriteVacio, spriteRampa, spriteAgua;
    private BufferedImage spritePersonaje;
    private BufferedImage spriteEnemigoPorDefecto;
    private Map<String, BufferedImage> spritesEnemigos;
    private BufferedImage spriteSlot;
    private BufferedImage spritePersonajeBatalla;
    private BufferedImage spriteEnemigoBatalla;
    private Map<String, BufferedImage> spritesCartas;

    public GerenciadorDeSprites() {
        this.spritesEnemigos = new HashMap<>();
        this.spritesCartas = new HashMap<>();
        
        cargarSpritesDelMundo();
        cargarSpritesDeUI();
        cargarSpritesDeCartas(); 
    }


    private void cargarSpritesDelMundo() {
        this.spriteRoca = cargarImagen("src/sprites/roca.png");
        this.spriteVacio = cargarImagen("src/sprites/vacio.png");
        this.spriteRampa = cargarImagen("src/sprites/rampa.png");
        this.spriteAgua = cargarImagen("src/sprites/agua.png");
        this.spritePersonaje = cargarImagen("src/sprites/personaje.png");
        this.spriteEnemigoPorDefecto = cargarImagen("src/sprites/enemigo.png");
        cargarSpriteEnemigo("orco", "src/sprites/ogro.png");
        cargarSpriteEnemigo("esqueleto", "src/sprites/esqueleto.png");
        cargarSpriteEnemigo("murcielago", "src/sprites/murcielago.png");
        cargarSpriteEnemigo("golem", "src/sprites/golem.png");
        cargarSpriteEnemigo("serpiente", "src/sprites/serpiente.png");
        cargarSpriteEnemigo("jefe", "src/sprites/jefe.png");
    }

    private void cargarSpritesDeUI() {
        this.spriteSlot = cargarImagen("src/sprites/slot.png");

        this.spritePersonajeBatalla = cargarImagenSilenciosa("src/sprites/personaje_batalla.png");
        this.spriteEnemigoBatalla = cargarImagenSilenciosa("src/sprites/enemigo_batalla.png");
        if (this.spritePersonajeBatalla == null) {
            this.spritePersonajeBatalla = cargarImagen("src/sprites/personaje.png");
        }
        if (this.spriteEnemigoBatalla == null) {
            this.spriteEnemigoBatalla = cargarImagen("src/sprites/ogro.png");
        }
    }


    private void cargarSpritesDeCartas() {
        cargarSpriteCarta("ataquedoble", "src/sprites/dobleAtaque.png");
        cargarSpriteCarta("escudo", "src/sprites/escudo.png");
        cargarSpriteCarta("aumentodevida", "src/sprites/curacion-parcial.png");
        cargarSpriteCarta("curaciondealiado", "src/sprites/curar-aliado.png");
        cargarSpriteCarta("curaciontotal", "src/sprites/curacion-completa.png");
        cargarSpriteCarta("doblemovimiento", "src/sprites/doble-movimiento.png");
        cargarSpriteCarta("evasion", "src/sprites/esquivar.png");
        cargarSpriteCarta("invisibilidad", "src/sprites/ceguera.png");
        cargarSpriteCarta("robodecarta", "src/sprites/robo-carta.png");
        cargarSpriteCarta("teletransportacion", "src/sprites/teletransportarse.png");
    }

    private BufferedImage cargarImagen(String ruta) {
        try {
            File f = new File(ruta);
            if (f.exists()) {
                return ImageIO.read(f);
            } else {
                System.err.println("WARN: No se pudo encontrar el sprite en: " + ruta);
            }
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo cargar el sprite: " + ruta);
            e.printStackTrace();
        }
        return null;
    }


    private BufferedImage cargarImagenSilenciosa(String ruta) {
        try {
            File f = new File(ruta);
            if (f.exists()) {
                return ImageIO.read(f);
            }
        } catch (IOException e) {
            System.err.println("ERROR: No se pudo cargar el sprite (silencioso): " + ruta);
            e.printStackTrace();
        }
        return null;
    }

    private void cargarSpriteEnemigo(String clave, String ruta) {
        BufferedImage img = cargarImagen(ruta);
        if (img != null) {
            this.spritesEnemigos.put(clave, img);
        }
    }

    private void cargarSpriteCarta(String clave, String ruta) {
        BufferedImage img = cargarImagen(ruta);
        if (img != null) {
            this.spritesCartas.put(clave, img);
        }
    }

    // --- Métodos de Acceso  ---

    public BufferedImage getSpriteRoca() { return spriteRoca; }
    public BufferedImage getSpriteVacio() { return spriteVacio; }
    public BufferedImage getSpriteRampa() { return spriteRampa; }
    public BufferedImage getSpriteAgua() { return spriteAgua; }
    public BufferedImage getSpritePersonaje() { return spritePersonaje; }
    
    public BufferedImage getSpriteEnemigo(String nombre, String tipo) {
        String claveNombre = normalizarClave(nombre);
        String claveTipo = normalizarClave(tipo);
        if (claveTipo.equals("jefe")) {
            return spritesEnemigos.get("jefe");
        }

        BufferedImage img = spritesEnemigos.get(claveNombre);
        if (img != null) return img;
        img = spritesEnemigos.get(claveTipo);
        if (img != null) return img;
        if (claveNombre.contains("jefe")) return spritesEnemigos.get("jefe"); 
        if (claveNombre.contains("orco")) return spritesEnemigos.get("orco");
        if (claveNombre.contains("esqueleto")) return spritesEnemigos.get("esqueleto");
        return spriteEnemigoPorDefecto;
    }
    
    public BufferedImage getSpriteSlot() { return spriteSlot; }
    public BufferedImage getSpritePersonajeBatalla() { return spritePersonajeBatalla; }
    public BufferedImage getSpriteEnemigoBatalla() { return spriteEnemigoBatalla; }

    
    public BufferedImage getSpriteCarta(String nombreCarta) {
        String clave = normalizarClave(nombreCarta); 
        

        BufferedImage img = spritesCartas.get(clave); 
        
        if (img == null) {
             System.err.println("WARN: No se encontró sprite para la carta con clave: " + clave);
        }
        return img;
    }


    private String normalizarClave(String s) {
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