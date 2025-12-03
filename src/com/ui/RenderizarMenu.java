package com.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class RenderizarMenu {

	public void dibujarMenuPausa(Graphics g, int anchoVentana, int altoVentana, Font fontMenuTitulo, Font fontMenuOpcion) {
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
	
	public void dibujarMenuPrincipal(Graphics g, int anchoVentana, int altoVentana, Font fontMenuTitulo, Font fontMenuOpcion) {
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
	
	public void dibujarMenuDificultad(Graphics g, int anchoVentana, int altoVentana, Font fontMenuTitulo, Font fontMenuOpcion) {
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
	
	public void dibujarMenuJugadores(Graphics g, int anchoVentana, int altoVentana, Font fontMenuTitulo, Font fontMenuOpcion) {
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
    
    public void dibujarMenuInstrucciones(Graphics g, int anchoVentana, int altoVentana, Font fontMenuTitulo, Font fontMenuOpcion) {
        g.setColor(new Color(10, 10, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Instrucciones";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 4); 
        // Usar una fuente legible y dimensión fija para el texto de instrucciones
        Font fontInstrucciones = new Font("Arial", Font.PLAIN, 20);
        g.setFont(fontInstrucciones);

        String[] lineas = {
            "Muevete con W, A, S, D (Ortogonal) y Q, E, Z, C (Diagonal).",
            "Presiona [ENTER] para finalizar tu turno.",
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

        // Parámetros de layout
        int maxTextWidth = (int)(anchoVentana * 0.85); // 85% del ancho de ventana
        int startY = altoVentana / 2 - 120;
        int y = startY;
        java.awt.FontMetrics fm = g.getFontMetrics(fontInstrucciones);
        int lineHeight = fm.getHeight() + 6;

        // Función sencilla de envolver texto por palabras al ancho máximo
        for (String linea : lineas) {
            if (linea == null || linea.isEmpty()) {
                y += lineHeight; // espacio en blanco
                continue;
            }

            String[] palabras = linea.split("\\s+");
            StringBuilder current = new StringBuilder();
            for (int i = 0; i < palabras.length; i++) {
                String palabra = palabras[i];
                String tentativa = current.length() == 0 ? palabra : current + " " + palabra;
                int w = fm.stringWidth(tentativa);
                if (w <= maxTextWidth) {
                    if (current.length() > 0) current.append(' ');
                    current.append(palabra);
                } else {
                    // dibujar línea actual centrada
                    String lineaADibujar = current.toString();
                    int anchoLinea = fm.stringWidth(lineaADibujar);
                    g.drawString(lineaADibujar, (anchoVentana - anchoLinea) / 2, y);
                    y += lineHeight;
                    current = new StringBuilder(palabra);
                }
            }

            if (current.length() > 0) {
                String lineaADibujar = current.toString();
                int anchoLinea = fm.stringWidth(lineaADibujar);
                g.drawString(lineaADibujar, (anchoVentana - anchoLinea) / 2, y);
                y += lineHeight;
            }
        }
    }
}
