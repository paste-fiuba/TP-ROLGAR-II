package com; 

import com.ui.PanelJuego;
import com.ui.VentanaJuego;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje;
import com.entidades.Enemigo;
import java.util.List;
import java.util.ArrayList;
import com.logica.ControladorJuego; 

public class Main {

    private static final int ANCHO_TABLERO = 25;
    private static final int ALTO_TABLERO = 20;
    private static final int NIVELES_TABLERO = 4;
    private static final int NIVEL_INICIAL = 0;

    public static void main(String[] args) {
        
        Tablero miTablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO, NIVELES_TABLERO);
        crearMundoCoherente(miTablero);
        
        Personaje miPersonaje = crearPersonaje();
        List<Enemigo> listaEnemigos = crearEnemigos();

        PanelJuego miPanel = new PanelJuego(miTablero, miPersonaje, listaEnemigos, NIVEL_INICIAL);
        
        ControladorJuego miControlador = new ControladorJuego(miTablero, miPersonaje, miPanel);

        miPanel.setControlador(miControlador);
        new VentanaJuego(miPanel); 
        miPanel.iniciarOyenteTeclado();
    }

    private static Personaje crearPersonaje() {
        int centroX = ANCHO_TABLERO / 2;
        int centroY = ALTO_TABLERO / 2;
        
        return new Personaje("Héroe Rolgar", 100, 
                             centroX, centroY, NIVEL_INICIAL, 
                             10, 1, 5.0);
    }
    
    private static List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();
        
        enemigos.add(new Enemigo("Orco", "Guerrero", 50, 14, 4, 0, 5, 1, 0));
        enemigos.add(new Enemigo("Esqueleto", "Arquero", 30, 4, 9, 0, 3, 1, 0));
        enemigos.add(new Enemigo("Murciélago", "Volador", 10, 4, 10, 1, 1, 1, 0));
        enemigos.add(new Enemigo("Golem", "Tanque", 100, 12, 4, 2, 8, 1, 0));
        enemigos.add(new Enemigo("Mago Oscuro", "Jefe", 80, 12, 10, 3, 15, 1, 0));
        
        return enemigos;
    }
    
    private static void crearMundoCoherente(Tablero tablero) {
        
        for (int z = 0; z < NIVELES_TABLERO; z++) {
            for (int y = 0; y < ALTO_TABLERO; y++) {
                for (int x = 0; x < ANCHO_TABLERO; x++) {
                    tablero.getCasillero(x, y, z).setTipo(TipoCasillero.ROCA);
                }
            }
        }

        // --- Nivel 0 ---
        tallarHabitacion(tablero, 10, 8, 15, 12, 0); // Sala 1 (central)
        tallarHabitacion(tablero, 10, 2, 15, 5, 0);  // Sala 2 (superior)
        tallarHabitacion(tablero, 2, 8, 7, 12, 0);  // Sala 3 (izquierda)
        tallarPasilloVertical(tablero, 12, 5, 8, 0);   // Conecta S1 y S2
        tallarPasilloHorizontal(tablero, 7, 10, 10, 0); // Conecta S1 y S3
        tablero.getCasillero(12, 3, 0).setTipo(TipoCasillero.AGUA);
        tablero.getCasillero(13, 3, 0).setTipo(TipoCasillero.AGUA);
        tablero.getCasillero(4, 10, 0).setTipo(TipoCasillero.RAMPA); // Rampa 0->1

        // --- Nivel 1 ---
        tallarHabitacion(tablero, 3, 9, 5, 11, 1);  // Sala llegada Rampa 0
        tablero.getCasillero(4, 10, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->0
        tallarHabitacion(tablero, 10, 2, 15, 5, 1);  // Sala para Rampa 2
        tablero.getCasillero(12, 3, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->2
        
        // ¡PASILLO DE CONEXIÓN Nivel 1!
        tallarPasilloVertical(tablero, 12, 5, 9, 1);
        tallarPasilloHorizontal(tablero, 5, 12, 9, 1);

        // --- Nivel 2 ---
        tallarHabitacion(tablero, 10, 2, 15, 5, 2);  // Sala llegada Rampa 1
        tablero.getCasillero(12, 3, 2).setTipo(TipoCasillero.RAMPA); // Rampa 2->1
        tallarHabitacion(tablero, 10, 8, 15, 12, 2); // Sala para Rampa 3
        tablero.getCasillero(12, 10, 2).setTipo(TipoCasillero.RAMPA); // Rampa 2->3
        
        // ¡PASILLO DE CONEXIÓN Nivel 2!
        tallarPasilloVertical(tablero, 12, 5, 8, 2);

        // --- Nivel 3 (Piso final) ---
        tallarHabitacion(tablero, 10, 8, 15, 12, 3); // Sala llegada Rampa 2
        tablero.getCasillero(12, 10, 3).setTipo(TipoCasillero.RAMPA); // Rampa 3->2
    }

    private static void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if (tablero.esCoordenadaValida(x, y, z)) {
                    tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
                }
            }
        }
    }

    private static void tallarPasilloHorizontal(Tablero tablero, int x1, int x2, int y, int z) {
        for (int x = x1; x <= x2; x++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
    
    private static void tallarPasilloVertical(Tablero tablero, int x, int y1, int y2, int z) {
        for (int y = y1; y <= y2; y++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
}