package com.ui;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.items.Inventario;
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

public class RenderizadorUI {

    private BufferedImage spriteSlot;   // 🔥 solo esto queda
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

    // 🔥🔥🔥 HOTBAR FINAL QUE USA IMAGEN DE CADA CARTA 🔥🔥🔥
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

            // Dibujo el slot vacío
            g.drawImage(this.spriteSlot, x, y, tamañoSlot, tamañoSlot, null);

            // Dibujo la carta si existe
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

    public void dibujarMenuInicio(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "ROLGAR II - MENÚ";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String linea1 = "Presioná 1,2,3 o 4 para seleccionar cantidad de jugadores.";
        String linea2 = "Controles: W/A/S/D mover, 1-0 usar carta, ENTER terminar turno.";
        int w1 = g.getFontMetrics().stringWidth(linea1);
        int w2 = g.getFontMetrics().stringWidth(linea2);
        g.drawString(linea1, (anchoVentana - w1) / 2, altoVentana / 2 - 20);
        g.drawString(linea2, (anchoVentana - w2) / 2, altoVentana / 2 + 10);

        // Explicación adicional: alianzas, transferencia y objetivo
        int infoY = altoVentana / 2 + 50;
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String a1 = "Alianzas: Acercate a otro jugador y presioná 'L' para proponer alianza.";
        String a2 = "Si te proponen, presioná 'Y' para aceptar o 'N' para rechazar en tu turno.";
        String t1 = "Transferencia de cartas: en tu turno presioná 'T' y luego 1..0 para elegir el slot a transferir a un aliado adyacente.";
        String obj = "Objetivo: Colaborar (o no) para eliminar a los enemigos del mapa.";
        int aw = g.getFontMetrics().stringWidth(a1);
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(a1, (anchoVentana - aw) / 2, infoY);
        g.drawString(a2, (anchoVentana - g.getFontMetrics().stringWidth(a2)) / 2, infoY + 20);
        g.drawString(t1, (anchoVentana - g.getFontMetrics().stringWidth(t1)) / 2, infoY + 40);
        g.setColor(Color.YELLOW);
        g.drawString(obj, (anchoVentana - g.getFontMetrics().stringWidth(obj)) / 2, infoY + 70);
    }

    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer) {

        g.setFont(fontInfo);

        g.setColor(Color.RED);
        g.drawString("JUGADOR HP: " + p.getVida(), 20, 30);

        int logY = 60;

        if (p.getVidaEscudo() > 0) {
            g.setColor(Color.CYAN);
            g.drawString("ESCUDO: " + p.getVidaEscudo(), 20, 50);
            logY = 80;
        }

        Enemigo masCercano = encontrarEnemigoMasCercano(p, e);
        if (masCercano != null) {
            g.setColor(Color.ORANGE);
            String textoEnemigo = masCercano.getNombre() + " HP: " + masCercano.getVida();
            int anchoTexto = g.getFontMetrics().stringWidth(textoEnemigo);
            g.drawString(textoEnemigo, g.getClipBounds().width - anchoTexto - 20, 30);
        }

        g.setColor(Color.GREEN);
        for (String mensaje : battleLog) {
            g.drawString(mensaje, 20, logY);
            logY += 20;
        }

        // Mostrar opciones de alianza
        if (admin != null) {
            Personaje proponente = admin.getPropuestaPara(p);
            if (proponente != null) {
                g.setColor(Color.YELLOW);
                String texto = "Propuesta de alianza de " + proponente.getNombre() + " - [Y] Aceptar  [N] Rechazar";
                g.drawString(texto, 20, logY + 20);
            } else {
                // Buscar un jugador adyacente no aliado
                if (jugadores != null) {
                    for (Personaje otro : jugadores) {
                        if (otro == null || otro == p) continue;
                        if (otro.getVida() <= 0) continue;
                        if (!p.estaAliadoCon(otro) && admin.sonAdyacentes(p, otro)) {
                            g.setColor(Color.CYAN);
                            String texto = "[L] Proponer alianza con " + otro.getNombre();
                            g.drawString(texto, 20, logY + 20);
                            break;
                        }
                    }
                }
            }
        }
        // Prompt para transferencia de cartas
        if (pendingTransfer == -2) {
            g.setColor(Color.MAGENTA);
            String txt = "TRANSFERIR: presioná 1..0 para elegir el slot a transferir a un aliado adyacente.";
            g.drawString(txt, 20, logY + 50);
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
