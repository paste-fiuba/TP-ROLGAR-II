package com.ui;

import com.entidades.Entidad;
import com.entidades.Personaje;
import com.items.Carta;
import com.items.Inventario;
import com.logica.AdministradorDeCombate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * TDA responsable de dibujar la pantalla de combate.
 * (Extraído de RenderizadorUI)
 */
public class RenderizadorCombate {

    // --- Dependencias (Fuentes, Sprites, Colores) ---
    private BufferedImage spritePersonajeBatalla;
    private BufferedImage spriteEnemigoBatalla;

    private Font fontMenuOpcion, fontInfo, fontCombate, fontCombateInfo;

    private Color colorCajaUI;
    private Color colorCajaBorde;
    private Color colorFondoBatallaCielo;
    private Color colorFondoBatallaPasto;
    private Color colorPlataformaBatalla;

    /**
     * Constructor que recibe todas las dependencias de RenderizadorUI.
     */
    public RenderizadorCombate(BufferedImage spritePersonaje, BufferedImage spriteEnemigo,
                               Font fontMenuOpcion, Font fontInfo, Font fontCombate, Font fontCombateInfo,
                               Color colorCajaUI, Color colorCajaBorde, Color colorFondoCielo,
                               Color colorFondoPasto, Color colorPlataforma) {
        
        this.spritePersonajeBatalla = spritePersonaje;
        this.spriteEnemigoBatalla = spriteEnemigo;
        this.fontMenuOpcion = fontMenuOpcion;
        this.fontInfo = fontInfo;
        this.fontCombate = fontCombate;
        this.fontCombateInfo = fontCombateInfo;
        this.colorCajaUI = colorCajaUI;
        this.colorCajaBorde = colorCajaBorde;
        this.colorFondoBatallaCielo = colorFondoCielo;
        this.colorFondoBatallaPasto = colorFondoPasto;
        this.colorPlataformaBatalla = colorPlataforma;
    }

    /**
     * pre: adminCombate no es null, battleLog no es null.
     * post: Dibuja la pantalla de combate estilo Pokémon.
     */
    public void dibujarPantallaCombate(Graphics g, AdministradorDeCombate adminCombate, int anchoVentana, int altoVentana, List<String> battleLog) {
        if (adminCombate == null) return;
        
        Personaje jugador = adminCombate.getJugador();
        Entidad oponente = adminCombate.getOponente();
        AdministradorDeCombate.EstadoCombate estado = adminCombate.getEstado();

        // 1. Fondo (Estilo Cueva)
        g.setColor(colorFondoBatallaCielo); 
        g.fillRect(0, 0, anchoVentana, (int)(altoVentana * 0.65)); 
        g.setColor(colorFondoBatallaPasto); 
        g.fillRect(0, (int)(altoVentana * 0.65), anchoVentana, (int)(altoVentana * 0.35));

        // 2. Plataformas (Color cueva)
        int plataformaAncho = 240;
        int plataformaAlto = 40;
        int plataformaOponenteX = (int)(anchoVentana * 0.65) - (plataformaAncho / 2);
        int plataformaOponenteY = (int)(altoVentana * 0.2) + 120;
        int plataformaJugadorX = (int)(anchoVentana * 0.15) + 10;
        int plataformaJugadorY = (int)(altoVentana * 0.4) + 120;
        
        g.setColor(colorPlataformaBatalla); 
        g.fillOval(plataformaOponenteX, plataformaOponenteY, plataformaAncho, plataformaAlto);
        g.fillOval(plataformaJugadorX, plataformaJugadorY, plataformaAncho, plataformaAlto);

        // 3. Dibujar Sprites (escalados 96x96)
        int spriteSize = 96; 
        
        if (spriteEnemigoBatalla != null) {
             g.drawImage(spriteEnemigoBatalla, 
                (int)(anchoVentana * 0.65), 
                (int)(altoVentana * 0.2), 
                spriteSize, spriteSize, null);
        }
        
        if (spritePersonajeBatalla != null) {
            g.drawImage(spritePersonajeBatalla, 
                (int)(anchoVentana * 0.15) + (spriteSize / 2), 
                (int)(altoVentana * 0.4), 
                spriteSize, spriteSize, null);
        }
        
        // 4. Caja de Acciones (Abajo)
        int altoCajaAccion = (int)(altoVentana * 0.35); 
        int yCajaAccion = altoVentana - altoCajaAccion;
        
        g.setColor(colorCajaUI);
        g.fillRoundRect(0, yCajaAccion, anchoVentana-1, altoCajaAccion, 15, 15);
        
        g.setColor(colorCajaBorde);
        g.drawRoundRect(0, yCajaAccion, anchoVentana-1, altoCajaAccion, 15, 15);

        int xDivision = anchoVentana / 2;
        g.fillRect(xDivision, yCajaAccion, 4, altoCajaAccion); 

        // Lado Izquierdo: Log de Batalla
        g.setFont(fontMenuOpcion);
        int logX = 40; 
        int logY = yCajaAccion + 60;
        g.setColor(Color.BLACK);
        
        g.drawString(adminCombate.getMensajeAccion(), logX, logY);
        
        int logMsgY = logY + 40;
        // Muestra los últimos 2 mensajes del log de batalla
        int logCountBatalla = 0;
        for (int i = battleLog.size() - 1; i >= 0 && logCountBatalla < 2; i--) {
            g.drawString(battleLog.get(i), logX, logMsgY + (logCountBatalla * 25));
            logCountBatalla++;
        }

        // Lado Derecho: Opciones (Depende del estado)
        if (estado == AdministradorDeCombate.EstadoCombate.ELIGE_ACCION) {
            g.setFont(fontCombate);
            g.drawString("[1] LUCHAR", xDivision + 50, yCajaAccion + 80);
            g.drawString("[2] CARTA", xDivision + 270, yCajaAccion + 80);
            g.drawString("[3] HUIR", xDivision + 270, yCajaAccion + 140);
        
        } else if (estado == AdministradorDeCombate.EstadoCombate.ELIGE_CARTA) {
            // Dibujar el inventario para seleccionar
            g.setFont(fontInfo); // Fuente más chica
            Inventario inv = jugador.getInventario();
            for (int i = 0; i < 10; i++) {
                int slot = (i + 1) % 10; // 1, 2, ... 9, 0
                String textoCarta = "[" + slot + "] ";
                Carta c = inv.getCarta(i);
                if (c != null) {
                    textoCarta += c.getNombre();
                } else {
                    textoCarta += "- Vacío -";
                }
                
                int col = i / 5; // Columna 0 o 1
                int fila = i % 5; // Fila 0 a 4
                
                int x = xDivision + 40 + (col * 220);
                int y = yCajaAccion + 60 + (fila * 20);
                g.drawString(textoCarta, x, y);
            }
            g.setFont(fontMenuOpcion);
            g.setColor(Color.BLUE);
            g.drawString("[ESC] Cancelar", xDivision + 50, yCajaAccion + 170);
        }
        
        // 5. Cajas de Info (HP)
        g.setFont(fontCombateInfo);
        
        int cajaAncho = 350;
        int cajaAlto = 80;
        int cajaOponenteX = 50;
        int cajaOponenteY = 50;
        dibujarCajaInfo(g, oponente.getNombre(), oponente.getVida(), cajaOponenteX, cajaOponenteY, cajaAncho, cajaAlto);

        int cajaJugadorX = anchoVentana - cajaAncho - 150;
        int cajaJugadorY = (int)(altoVentana * 0.65) - cajaAlto - 30;
        dibujarCajaInfo(g, jugador.getNombre(), jugador.getVida(), cajaJugadorX, cajaJugadorY, cajaAncho, cajaAlto);
    }
    
    /**
     * pre: g no es null, x e y son posiciones válidas.
     * post: Dibuja la caja de info de un combatiente (nombre y HP).
     * (Movido de RenderizadorUI)
     */
    private void dibujarCajaInfo(Graphics g, String nombre, int vida, int x, int y, int ancho, int alto) {
        g.setColor(colorCajaUI);
        g.fillRoundRect(x, y, ancho, alto, 15, 15);
        
        g.setColor(colorCajaBorde);
        g.drawRoundRect(x, y, ancho, alto, 15, 15);
        
        g.setColor(Color.BLACK);
        g.setFont(fontCombateInfo);
        g.drawString(nombre, x + 15, y + 30);
        dibujarBarraHP(g, x + 15, y + 55, vida);
    }

    /**
     * pre: g no es null, x e y son posiciones válidas.
     * post: Dibuja una barra de HP estilo Pokémon.
     * (Movido de RenderizadorUI)
     */
    private void dibujarBarraHP(Graphics g, int x, int y, int vida) {
        int anchoBarra = 200;
        int altoBarra = 15;
        
        g.setFont(fontInfo);
        g.setColor(Color.BLACK);
        g.drawString("HP:", x, y);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        int vidaActual = Math.max(0, vida); 
        int anchoVida = (int) (anchoBarra * (vidaActual / 100.0)); // Asumimos 100 de vida máx
        
        if (vidaActual > 50) {
            g.setColor(Color.GREEN);
        } else if (vidaActual > 20) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x + 40, y - 12, anchoVida, altoBarra);
        
        g.setColor(Color.BLACK);
        g.drawRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        g.setFont(fontInfo);
        g.drawString(vidaActual + "/100", x + anchoBarra + 50, y);
    } 
}