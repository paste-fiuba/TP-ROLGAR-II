package com.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

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

        int y = altoVentana / 2 - 100;
        for (String linea : lineas) {
            int anchoLinea = g.getFontMetrics().stringWidth(linea);
            g.drawString(linea, (anchoVentana - anchoLinea) / 2, y);
            y += 25;
        }
    }
}
