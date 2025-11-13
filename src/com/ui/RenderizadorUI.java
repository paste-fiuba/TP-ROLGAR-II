package com.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import com.items.Inventario;
import com.entidades.Enemigo;
import com.entidades.Personaje;

public class RenderizadorUI {

    private BufferedImage spriteSlot, spriteCarta;
    private Font fontMenuTitulo, fontMenuOpcion, fontInfo;
    private Font fontGameOver;
    private List<String> battleLog;

    public RenderizadorUI() {
        cargarSpritesUI();
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
        this.fontInfo = new Font("Arial", Font.BOLD, 16);
        this.fontGameOver = new Font("Arial", Font.BOLD, 100);
        this.battleLog = new ArrayList<>();
    }

    private void cargarSpritesUI() {
        try {
            this.spriteSlot = ImageIO.read(new File("src/sprites/slot.png"));
            this.spriteCarta = ImageIO.read(new File("src/sprites/carta.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void agregarMensajeLog(String mensaje) {
        this.battleLog.add(mensaje);
        if (this.battleLog.size() > 5) {
            this.battleLog.remove(0);
        }
    }
    public void limpiarLog() {
        this.battleLog.clear();
    }

    public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico) {
        int ALTURA_HOTBAR = PanelJuego.ALTURA_HOTBAR;
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, altoJuegoLogico, anchoLogico, ALTURA_HOTBAR);

        if (this.spriteSlot == null) return;
        
        int numSlots = 10;
        int tamañoSlot = 48;
        int padding = (ALTURA_HOTBAR - tamañoSlot) / 2;
        int anchoTotalSlots = (numSlots * tamañoSlot) + ((numSlots - 1) * 5);
        int startX = (anchoLogico - anchoTotalSlots) / 2;
        
        for (int i = 0; i < numSlots; i++) {
            int x = startX + (i * (tamañoSlot + 5));
            int y = altoJuegoLogico + padding;
            g.drawImage(this.spriteSlot, x, y, tamañoSlot, tamañoSlot, null);
            
            if (i < inventario.cantidadDeCartas() && this.spriteCarta != null) {
                int tamañoCarta = 40;
                int paddingCarta = (tamañoSlot - tamañoCarta) / 2;
                g.drawImage(this.spriteCarta, x + paddingCarta, y + paddingCarta, tamañoCarta, tamañoCarta, null);
            }
        }
    }
    
    public void dibujarMenuPausa(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, anchoVentana, altoVentana);
        
        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "JUEGO PAUSADO";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 50);

        g.setFont(fontMenuOpcion);
        String opcion1 = "[R] Reanudar";
        String opcion2 = "[Q] Cerrar Juego";
        
        int anchoOpcion1 = g.getFontMetrics().stringWidth(opcion1);
        int anchoOpcion2 = g.getFontMetrics().stringWidth(opcion2);
        
        g.drawString(opcion1, (anchoVentana - anchoOpcion1) / 2, altoVentana / 2 + 20);
        g.drawString(opcion2, (anchoVentana - anchoOpcion2) / 2, altoVentana / 2 + 60);
    }
    
    public void dibujarPantallaGameOver(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(100, 0, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);
        
        g.setColor(Color.RED);
        g.setFont(fontGameOver);
        String titulo = "PERDISTE";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuOpcion);
        String opcion = "[Q] Cerrar Juego";
        int anchoOpcion = g.getFontMetrics().stringWidth(opcion);
        g.drawString(opcion, (anchoVentana - anchoOpcion) / 2, altoVentana / 2 + 60);
    }
    
    public void dibujarPantallaVictoria(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 80, 20, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);
        
        g.setColor(Color.YELLOW);
        g.setFont(fontGameOver);
        String titulo = "¡GANASTE!";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuOpcion);
        String opcion = "[Q] Cerrar Juego";
        int anchoOpcion = g.getFontMetrics().stringWidth(opcion);
        g.drawString(opcion, (anchoVentana - anchoOpcion) / 2, altoVentana / 2 + 60);
    }

    /**
     * Dibuja la información de HP (con escudo) y el Log de Batalla.
     */
    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e) {
        
        g.setFont(fontInfo);
        
        // Dibuja HP del Jugador
        g.setColor(Color.RED);
        g.drawString("JUGADOR HP: " + p.getVida(), 20, 30);
        
        // Posición Y inicial para el log
        int logY = 60; 

        // ¡NUEVO! Dibuja el Escudo si existe
        if (p.getVidaEscudo() > 0) {
            g.setColor(Color.CYAN); // Color cian para el escudo
            g.drawString("ESCUDO: " + p.getVidaEscudo(), 20, 50);
            logY = 80; // Mueve el log más abajo para hacer espacio
        }
        
        // Dibuja HP del Enemigo más cercano
        Enemigo masCercano = encontrarEnemigoMasCercano(p, e);
        if (masCercano != null) {
            g.setColor(Color.ORANGE);
            String textoEnemigo = masCercano.getNombre() + " HP: " + masCercano.getVida();
            int anchoTexto = g.getFontMetrics().stringWidth(textoEnemigo);
            g.drawString(textoEnemigo, g.getClipBounds().width - anchoTexto - 20, 30);
        }
        
        // Dibuja el Log de Batalla
        g.setColor(Color.GREEN);
        for (String mensaje : battleLog) {
            g.drawString(mensaje, 20, logY);
            logY += 20;
        }
    }
    
    private Enemigo encontrarEnemigoMasCercano(Personaje jugador, List<Enemigo> enemigos) {
        Enemigo masCercano = null;
        int menorDistancia = Integer.MAX_VALUE;
        int pX = jugador.getPosX();
        int pY = jugador.getPosY();
        int pZ = jugador.getPosZ();

        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && enemigo.getPosZ() == pZ) {
                int dist = Math.abs(enemigo.getPosX() - pX) + Math.abs(enemigo.getPosY() - pY);
                if (dist < menorDistancia) {
                    menorDistancia = dist;
                    masCercano = enemigo;
                }
            }
        }
        return masCercano;
    }
}