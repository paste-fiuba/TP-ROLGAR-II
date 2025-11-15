package com.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.awt.Font;
import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.items.Inventario;

public class RenderizarData {
	public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico, BufferedImage spriteSlot) {

        int ALTURA_HOTBAR = PanelJuego.ALTURA_HOTBAR;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, altoJuegoLogico, anchoLogico, ALTURA_HOTBAR);

        if (spriteSlot == null) return;
        if (inventario == null) return; 

        int numSlots = 10;
        int tamañoSlot = 48;
        int padding = (ALTURA_HOTBAR - tamañoSlot) / 2;
        int anchoTotalSlots = (numSlots * tamañoSlot) + ((numSlots - 1) * 5);
        int startX = (anchoLogico - anchoTotalSlots) / 2;

        for (int i = 0; i < numSlots; i++) {
            int x = startX + (i * (tamañoSlot + 5));
            int y = altoJuegoLogico + padding;

            g.drawImage(spriteSlot, x, y, tamañoSlot, tamañoSlot, null);

            if (i < inventario.cantidadDeCartas()) {
                Carta carta = inventario.getCarta(i);
                if (carta != null && carta.getImagen() != null) {
                    int tamañoCarta = 40;
                    int paddingCarta = (tamañoSlot - tamañoCarta) / 2;
                    g.drawImage(
                        carta.getImagen(),
                        x + paddingCarta,
                        y + paddingCarta,
                        tamañoCarta,
                        tamañoCarta,
                        null
                    );
                }
            }
        }
    }
	
	public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer, Font fontInfo, List<String> battleLog, Enemigo enemigoMasCercano) {

        if (p == null) return; 

        g.setFont(fontInfo);

        g.setColor(Color.RED);
        g.drawString("JUGADOR HP: " + p.getVida(), 20, 30);

        int logY = 60;

        if (p.getVidaEscudo() > 0) {
            g.setColor(Color.CYAN);
            g.drawString("ESCUDO: " + p.getVidaEscudo(), 20, 50);
            logY = 80;
        }
        // Contar enemigos vivos
        int enemigosVivos = 0;
        if (e != null) {
            for (Enemigo en : e) {
                if (en != null && en.estaVivo()) enemigosVivos++;
            }
        }
        if (enemigoMasCercano != null) {
            g.setColor(Color.ORANGE);
            String textoEnemigo = enemigoMasCercano.getNombre() + " HP: " + enemigoMasCercano.getVida();
            int anchoTexto = g.getFontMetrics().stringWidth(textoEnemigo);
            g.drawString(textoEnemigo, g.getClipBounds().width - anchoTexto - 20, 30);
        }

        // Mostrar número total de criaturas enemigas vivas
        g.setColor(Color.WHITE);
        String enemigosTxt = "Criaturas enemigas restantes: " + enemigosVivos;
        g.drawString(enemigosTxt, 20, 60);

        g.setColor(Color.GREEN);
        for (String mensaje : battleLog) {
            g.drawString(mensaje, 20, logY);
            logY += 20;
        }

        if (admin != null) {
            java.util.List<String> eliminados = admin.getJugadoresEliminados();
            if (eliminados != null && !eliminados.isEmpty()) {
                g.setColor(Color.RED);
                int x = g.getClipBounds().width - 200;
                int y = 60;
                g.drawString("Eliminados:", x, y);
                y += 20;
                for (String name : eliminados) {
                    g.drawString("- " + name, x, y);
                    y += 18;
                }
            }
            Personaje proponente = admin.getPropuestaPara(p);
            if (proponente != null) {
                g.setColor(Color.YELLOW);
                String texto = "Propuesta de alianza de " + proponente.getNombre() + " - [Y] Aceptar  [N] Rechazar";
                g.drawString(texto, 20, logY + 20);
            } else {
                if (jugadores != null) {
                    for (Personaje otro : jugadores) {
                        if (otro == null || otro == p || otro.getVida() <= 0) continue;
                        if (!p.estaAliadoCon(otro) && admin.sonAdyacentes(p, otro)) {
                            g.setColor(Color.CYAN);
                            String texto = "[L] Proponer alianza con " + otro.getNombre();
                            g.drawString(texto, 20, logY + 20);
                            break;
                        }
                    }
                    for (Personaje otro : jugadores) {
                        if (otro == null || otro == p || otro.getVida() <= 0) continue;
                        if (p != null && admin != null && admin.sonAdyacentes(p, otro)) {
                            g.setColor(Color.RED);
                            String txt = "[F] Atacar a " + otro.getNombre();
                            g.drawString(txt, 20, logY + 80);
                            break;
                        }
                    }
                }
            }
        }
        if (pendingTransfer == -2) {
            g.setColor(Color.MAGENTA);
            String txt = "TRANSFERIR: presioná 1..0 para elegir el slot a transferir a un aliado adyacente.";
            g.drawString(txt, 20, logY + 50);
        }
    }
	/**
     * pre: g no es null, x e y son posiciones válidas.
     * post: Dibuja una barra de HP estilo Pokémon.
     */
	private void dibujarBarraHP(Graphics g, int x, int y, int vida, Font fontInfo) {
        int anchoBarra = 200;
        int altoBarra = 15;
        
        // Etiqueta "HP:"
        g.setFont(fontInfo);
        g.setColor(Color.BLACK);
        g.drawString("HP:", x, y);

        // Fondo de la barra
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        int vidaActual = Math.max(0, vida); 
        int anchoVida = (int) (anchoBarra * (vidaActual / 100.0)); // Asumimos 100 de vida máx por ahora
        
        // TODO: Usar vidaMaxima de la entidad
        // int anchoVida = (int) (anchoBarra * ((double)vidaActual / vidaMaxima));

        // Color de vida
        if (vidaActual > 50) {
            g.setColor(Color.GREEN);
        } else if (vidaActual > 20) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x + 40, y - 12, anchoVida, altoBarra);
        
        // Borde
        g.setColor(Color.BLACK);
        g.drawRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        // Texto de vida (ej. "69/69")
        g.setFont(fontInfo);
        g.drawString(vidaActual + "/100", x + anchoBarra + 50, y);
    }
	
	public void dibujarCajaInfo(Graphics g, String nombre, int vida, int x, int y, int ancho, int alto, Color colorCajaUI, Color colorCajaBorde, Font fontCombateInfo, Font fontInfo) {
        // Fondo de la caja (estilo Pokémon)
        g.setColor(colorCajaUI);
        g.fillRoundRect(x, y, ancho, alto, 15, 15);
        
        // Borde
        g.setColor(colorCajaBorde);
        g.drawRoundRect(x, y, ancho, alto, 15, 15);
        
        g.setColor(Color.BLACK);
        g.setFont(fontCombateInfo);
        g.drawString(nombre, x + 15, y + 30);
        dibujarBarraHP(g, x + 15, y + 55, vida, fontInfo);
    }

}
