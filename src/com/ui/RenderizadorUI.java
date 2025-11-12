package com.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.items.Inventario;

public class RenderizadorUI {

    private BufferedImage spriteSlot, spriteCarta;
    private Font fontMenuTitulo, fontMenuOpcion;

    public RenderizadorUI() {
        cargarSpritesUI();
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
    }

    private void cargarSpritesUI() {
        try {
            this.spriteSlot = ImageIO.read(new File("src/sprites/slot.png"));
            this.spriteCarta = ImageIO.read(new File("src/sprites/carta.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dibuja la barra de inventario (hotbar).
     */
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
    
    /**
     * Dibuja la pantalla de pausa.
     */
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
}
