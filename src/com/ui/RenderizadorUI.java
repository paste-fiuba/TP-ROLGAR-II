package com.ui;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.Carta;
import com.items.Inventario;
import com.logica.ControladorJuego; 

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

    private BufferedImage spriteSlot;   
    private Font fontMenuTitulo, fontMenuOpcion, fontInfo, fontInstrucciones;
    private Font fontGameOver;
    private List<String> battleLog;

    public RenderizadorUI() {
        cargarSpritesUI();
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
        this.fontInfo = new Font("Arial", Font.BOLD, 16);
        this.fontGameOver = new Font("Arial", Font.BOLD, 100);
        this.fontInstrucciones = new Font("Arial", Font.PLAIN, 18); // Fuente para instrucciones
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

    
    public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico) {

        int ALTURA_HOTBAR = PanelJuego.ALTURA_HOTBAR;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, altoJuegoLogico, anchoLogico, ALTURA_HOTBAR);

        if (this.spriteSlot == null) return;
        if (inventario == null) return; 

        int numSlots = 10;
        int tamañoSlot = 48;
        int padding = (ALTURA_HOTBAR - tamañoSlot) / 2;
        int anchoTotalSlots = (numSlots * tamañoSlot) + ((numSlots - 1) * 5);
        int startX = (anchoLogico - anchoTotalSlots) / 2;

        for (int i = 0; i < numSlots; i++) {
            int x = startX + (i * (tamañoSlot + 5));
            int y = altoJuegoLogico + padding;

            g.drawImage(this.spriteSlot, x, y, tamañoSlot, tamañoSlot, null);

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

    /**
     * Dibuja la pantalla correspondiente al estado de menú actual.
     */
    public void dibujarMenu(Graphics g, ControladorJuego.GameState estado, int anchoVentana, int altoVentana) {
        switch (estado) {
            case MENU_PRINCIPAL:
                dibujarMenuPrincipal(g, anchoVentana, altoVentana);
                break;
            case MENU_DIFICULTAD:
                dibujarMenuDificultad(g, anchoVentana, altoVentana);
                break;
            case MENU_JUGADORES:
                dibujarMenuJugadores(g, anchoVentana, altoVentana);
                break;
            case MENU_INSTRUCCIONES: // <-- NUEVO CASE
                dibujarMenuInstrucciones(g, anchoVentana, altoVentana);
                break;
            default:
                break;
        }
    }

    private void dibujarMenuPrincipal(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "ROLGAR II";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String op1 = "[1] Empezar Partida";
        String op2 = "[2] Instrucciones";
        String op3 = "[3] Salir del Juego";

        g.drawString(op1, (anchoVentana - g.getFontMetrics().stringWidth(op1)) / 2, altoVentana / 2);
        g.drawString(op2, (anchoVentana - g.getFontMetrics().stringWidth(op2)) / 2, altoVentana / 2 + 40);
        g.drawString(op3, (anchoVentana - g.getFontMetrics().stringWidth(op3)) / 2, altoVentana / 2 + 80);
    }

    private void dibujarMenuDificultad(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(10, 0, 10, 200)); 
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Seleccionar Dificultad";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String op1 = "[1] Fácil";
        String op2 = "[2] Normal";
        String op3 = "[3] Difícil";
        String opEsc = "[ESC] Volver";

        g.setColor(Color.GREEN);
        g.drawString(op1, (anchoVentana - g.getFontMetrics().stringWidth(op1)) / 2, altoVentana / 2);
        g.setColor(Color.YELLOW);
        g.drawString(op2, (anchoVentana - g.getFontMetrics().stringWidth(op2)) / 2, altoVentana / 2 + 40);
        g.setColor(Color.RED);
        g.drawString(op3, (anchoVentana - g.getFontMetrics().stringWidth(op3)) / 2, altoVentana / 2 + 80);
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(opEsc, (anchoVentana - g.getFontMetrics().stringWidth(opEsc)) / 2, altoVentana / 2 + 150);
    }

    private void dibujarMenuJugadores(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 10, 10, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Cantidad de Jugadores";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String op1 = "[1] Un Jugador";
        String op2 = "[2] Dos Jugadores";
        String op3 = "[3] Tres Jugadores";
        String op4 = "[4] Cuatro Jugadores";
        String opEsc = "[ESC] Volver";

        g.setColor(Color.CYAN);
        g.drawString(op1, (anchoVentana - g.getFontMetrics().stringWidth(op1)) / 2, altoVentana / 2 - 20);
        g.drawString(op2, (anchoVentana - g.getFontMetrics().stringWidth(op2)) / 2, altoVentana / 2 + 20);
        g.drawString(op3, (anchoVentana - g.getFontMetrics().stringWidth(op3)) / 2, altoVentana / 2 + 60);
        g.drawString(op4, (anchoVentana - g.getFontMetrics().stringWidth(op4)) / 2, altoVentana / 2 + 100);
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(opEsc, (anchoVentana - g.getFontMetrics().stringWidth(opEsc)) / 2, altoVentana / 2 + 170);
    }
    
    /**
     * pre: -
     * post: Dibuja la pantalla de instrucciones en el objeto Graphics.
     */
    private void dibujarMenuInstrucciones(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(10, 10, 0, 200)); // Fondo amarillo oscuro
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Instrucciones";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 4); // Más arriba

        g.setFont(fontInstrucciones); // Usar fuente más pequeña
        
        String[] lineas = {
            "Muevete con W, A, S, D (Ortogonal) y Q, E, Z, C (Diagonal).",
            "Entra en combate con Enemigos (E) al caminar sobre ellos.",
            "Recoge Cartas (?) para obtener poderes.",
            "Usa 1-0 para activar cartas (gasta un turno).",
            "",
            "Multijugador:",
            "Presiona [F] para atacar a otro jugador adyacente.",
            "Presiona [L] para proponer alianzas a un jugador adyacente.",
            "Presiona [Y] / [N] para aceptar o rechazar alianzas.",
            "Presiona [T] + (1-0) para transferir una carta a un aliado adyacente.",
            "",
            "¡Sobrevive y derrota al REY MAGO en el último nivel!",
            "",
            "[ESC] Volver al Menú Principal"
        };

        int y = altoVentana / 2 - 100;
        for (String linea : lineas) {
            g.drawString(linea, (anchoVentana - g.getFontMetrics().stringWidth(linea)) / 2, y);
            y += 25; // Siguiente línea
        }
    }

    
    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer) {

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
        }

        if (admin != null) {
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
                }
            }
        }
        if (jugadores != null) {
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
        if (pendingTransfer == -2) {
            g.setColor(Color.MAGENTA);
            String txt = "TRANSFERIR: presioná 1..0 para elegir el slot a transferir a un aliado adyacente.";
            g.drawString(txt, 20, logY + 50);
        }
    }

    private Enemigo encontrarEnemigoMasCercano(Personaje jugador, List<Enemigo> enemigos) {
        Enemigo masCercano = null;
        int menorDistancia = Integer.MAX_VALUE;

        if (jugador == null || enemigos == null) return null;

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