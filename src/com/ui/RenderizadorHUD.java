package com.ui;

import com.entidades.Personaje;
import com.logica.AdministradorDeJuego;
import com.tablero.Casillero;
import com.tablero.Tablero;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * TDA responsable de dibujar los elementos gráficos del HUD,
 * como el Minimapa. (Extraído de RenderizadorUI).
 */
public class RenderizadorHUD {

    // Dependencias de UI
    private Font fontInfo;
    private Color colorCajaBorde;
    private Color colorPlataformaBatalla;
    public RenderizadorHUD(Font fontInfo, Color colorCajaBorde, Color colorPlataforma) {
        this.fontInfo = fontInfo;
        this.colorCajaBorde = colorCajaBorde;
        this.colorPlataformaBatalla = colorPlataforma;
    }

    /**
     * pre: g no es null, admin no es null, nivelZ es válido.
     * post: Dibuja un mini-mapa en la esquina superior derecha.
     */
    public void dibujarMiniMapa(Graphics g, AdministradorDeJuego admin, int nivelZ, int anchoVentana, int altoVentana) {
        if (admin == null || admin.getTablero() == null) {
            return;
        }
        
        Tablero tablero = admin.getTablero();
        Personaje jugador = admin.getJugadorActual();
        
        int mapWidth = 200; 
        int mapHeight = 150; 
        int mapX = anchoVentana - mapWidth - 20; 
        int mapY = 20;                           
        int dotSize = 4; 

        // Fondo de la caja del minimapa
        g.setColor(new Color(30, 30, 30, 200)); 
        g.fillRoundRect(mapX, mapY, mapWidth, mapHeight, 15, 15);
        g.setColor(colorCajaBorde);
        g.drawRoundRect(mapX, mapY, mapWidth, mapHeight, 15, 15);
        
        // Título
        g.setFont(fontInfo);
        g.setColor(Color.WHITE);
        g.drawString("NIVEL " + (nivelZ + 1), mapX + 10, mapY + 20); 

        // Calcular offset para centrar los puntos
        int mapContentWidth = tablero.getDimensionX() * dotSize;
        int mapContentHeight = tablero.getDimensionY() * dotSize;
        int offsetX = mapX + (mapWidth - mapContentWidth) / 2;
        int offsetY = mapY + 25 + (mapHeight - 25 - mapContentHeight) / 2; 

        for (int x = 0; x < tablero.getDimensionX(); x++) {
            for (int y = 0; y < tablero.getDimensionY(); y++) {
                
                Casillero c = tablero.getCasillero(x, y, nivelZ);
                if (c.isVisitado()) {
                    // Dibuja el casillero visitado
                    switch (c.getTipo()) {
                        case VACIO:
                            g.setColor(colorPlataformaBatalla); 
                            break;
                        case RAMPA:
                            g.setColor(Color.YELLOW);
                            break;
                        case AGUA:
                            g.setColor(Color.BLUE);
                            break;
                        case ROCA:
                            g.setColor(Color.GRAY);
                            break;
                    }
                    g.fillRect(offsetX + x * dotSize, offsetY + y * dotSize, dotSize, dotSize);
                
                } 
            }
        }
        
        // Dibujar al jugador
        if (jugador != null && jugador.getPosZ() == nivelZ) {
            g.setColor(Color.WHITE);
            g.fillRect(offsetX + jugador.getPosX() * dotSize, offsetY + jugador.getPosY() * dotSize, dotSize, dotSize);
        }
    }
}