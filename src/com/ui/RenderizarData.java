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
	
	public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico, GerenciadorDeSprites sprites) {

        int ALTURA_HOTBAR = PanelJuego.ALTURA_HOTBAR;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, altoJuegoLogico, anchoLogico, ALTURA_HOTBAR);

        BufferedImage spriteSlot = sprites.getSpriteSlot(); 
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

            Carta carta = inventario.getCarta(i); 
            
            if (carta != null) { 
                BufferedImage imgCarta = sprites.getSpriteCarta(carta.getNombre());
                
                if (imgCarta != null) {
                    int tamañoCarta = 40;
                    int paddingCarta = (tamañoSlot - tamañoCarta) / 2;
                    g.drawImage(
                        imgCarta,
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
	
	/**
	 * MÉTODO CORREGIDO:
	 * Reescrito para usar un layout dinámico con espaciado de línea constante,
	 * evitando que el texto se "amontone".
	 */
	public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer, Font fontInfo, List<String> battleLog, Enemigo enemigoMasCercano) {

        if (p == null) return; 

        g.setFont(fontInfo);

        // --- INICIO DE LA CORRECCIÓN DE LAYOUT ---
        
        // 1. Definir un layout claro para el HUD
        int hudX = 20;
        int hudY = 30; // Posición Y inicial
        int lineHeight = 20; // Espacio entre líneas

        // 2. Dibujar la Vida
        g.setColor(Color.RED);
        g.drawString("JUGADOR HP: " + p.getVida(), hudX, hudY);
        hudY += lineHeight; // Mover Y para la siguiente línea

        // 3. Dibujar el Escudo (si existe)
        if (p.getVidaEscudo() > 0) {
            g.setColor(Color.CYAN);
            g.drawString("ESCUDO: " + p.getVidaEscudo(), hudX, hudY);
            hudY += lineHeight; // Mover Y para la siguiente línea
        }

        // 4. Dibujar Enemigos restantes
        g.setColor(Color.WHITE);
        int enemigosVivos = 0;
        if (e != null) {
            for (Enemigo en : e) {
                if (en != null && en.estaVivo()) enemigosVivos++;
            }
        }
        String enemigosTxt = "Criaturas enemigas restantes: " + enemigosVivos;
        g.drawString(enemigosTxt, hudX, hudY);
        hudY += lineHeight; // Mover Y para la siguiente línea

        // 5. Dibujar el Piso actual
        String pisoTxt = p.getNombre() + " se encuentra en el piso: " + p.getPosZ();
        g.drawString(pisoTxt, hudX, hudY);
        hudY += lineHeight; // Mover Y para la siguiente línea

        // --- FIN DE LA CORRECCIÓN DEL HUD ---

        // 6. Dibujar el HUD del Enemigo (Top-Right) - (Sin cambios)
        if (enemigoMasCercano != null) {
            g.setColor(Color.ORANGE);
            String textoEnemigo = enemigoMasCercano.getNombre() + " HP: " + enemigoMasCercano.getVida();
            int anchoTexto = g.getFontMetrics().stringWidth(textoEnemigo);
            g.drawString(textoEnemigo, g.getClipBounds().width - anchoTexto - 20, 30);
        }

        // 7. Dibujar el Battle Log (Ahora empieza DESPUÉS del HUD)
        hudY += 10; // Añadir un pequeño padding extra antes del log
        g.setColor(Color.GREEN);
        for (String mensaje : battleLog) {
            g.drawString(mensaje, hudX, hudY); // Usa la nueva 'hudY'
            hudY += 20; // Incrementa para la siguiente línea del log
        }

        // 8. Dibujar prompts de Alianza/Ataque (Usan la 'hudY' final)
        if (admin != null) {
            
            // (Jugadores eliminados - Sin cambios, está en la otra esquina)
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

            // (Prompts de acción)
            int promptY = hudY + 10; // Posición Y para los prompts de acción

            Personaje proponente = admin.getPropuestaPara(p);
            if (proponente != null) {
                g.setColor(Color.YELLOW);
                String texto = "Propuesta de alianza de " + proponente.getNombre() + " - [Y] Aceptar  [N] Rechazar";
                g.drawString(texto, hudX, promptY);
                promptY += 20;
            } else {
                Personaje candidatoAlianza = admin.getJugadorAdyacenteParaAlianza(p);
                if (candidatoAlianza != null) {
                    g.setColor(Color.CYAN);
                    String texto = "[L] Proponer alianza con " + candidatoAlianza.getNombre();
                    g.drawString(texto, hudX, promptY);
                    promptY += 20;
                }
            }
            
            Personaje candidatoAtaque = admin.getJugadorAdyacenteParaAtacar(p);
            if (candidatoAtaque != null) {
                g.setColor(Color.RED);
                String txt = "[F] Atacar a " + candidatoAtaque.getNombre();
                g.drawString(txt, hudX, promptY);
                promptY += 20;
            }
            
            if (pendingTransfer == -2) {
                g.setColor(Color.MAGENTA);
                String txt = "TRANSFERIR: presioná 1..0 para elegir el slot a transferir a un aliado adyacente.";
                g.drawString(txt, hudX, promptY);
                promptY += 20;
            }
        }
    }
	
	private void dibujarBarraHP(Graphics g, int x, int y, int vida, Font fontInfo) {
        int anchoBarra = 200;
        int altoBarra = 15;
        
        g.setFont(fontInfo);
        g.setColor(Color.BLACK);
        g.drawString("HP:", x, y);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        int vidaActual = Math.max(0, vida); 
        int anchoVida = (int) (anchoBarra * (vidaActual / 100.0));
        
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
	
	public void dibujarCajaInfo(Graphics g, String nombre, int vida, int x, int y, int ancho, int alto, Color colorCajaUI, Color colorCajaBorde, Font fontCombateInfo, Font fontInfo) {
        g.setColor(colorCajaUI);
        g.fillRoundRect(x, y, ancho, alto, 15, 15);
        
        g.setColor(colorCajaBorde);
        g.drawRoundRect(x, y, ancho, alto, 15, 15);
        
        g.setColor(Color.BLACK);
        g.setFont(fontCombateInfo);
        g.drawString(nombre, x + 15, y + 30);
        dibujarBarraHP(g, x + 15, y + 55, vida, fontInfo);
    }
}