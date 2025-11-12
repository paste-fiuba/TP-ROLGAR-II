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
import com.logica.AdministradorDeJuego;
import com.items.Carta;
import com.items.CartaAtaqueDoble;
import com.items.CartaEscudo;
import com.items.CartaCuracionAliado; 
import com.items.CartaTeletransportacion; 

public class Main {

    // --- Constantes del Mundo ---
    private static final int ANCHO_TABLERO = 40;
    private static final int ALTO_TABLERO = 25;
    private static final int NIVELES_TABLERO = 4;
    private static final int NIVEL_INICIAL = 0;

    public static void main(String[] args) {
        
        Tablero miTablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO, NIVELES_TABLERO);
        crearMundoCoherente(miTablero);
        
        Personaje miPersonaje = crearPersonaje();
        List<Enemigo> listaEnemigos = crearEnemigos();

        PanelJuego miPanel = new PanelJuego(miTablero, miPersonaje, listaEnemigos, NIVEL_INICIAL);
        AdministradorDeJuego miAdmin = new AdministradorDeJuego(miTablero, miPersonaje, listaEnemigos, miPanel);
        ControladorJuego miControlador = new ControladorJuego(miAdmin, miPanel);

        miPanel.setControlador(miControlador);
        new VentanaJuego(miPanel); 
        miPanel.iniciarOyenteTeclado();
    }

    /**
     * Crea al personaje en una posición de inicio fija.
     */
    private static Personaje crearPersonaje() {
        // Posición de inicio: Nivel 0, en la sala inicial
        Personaje personaje = new Personaje("Héroe Rolgar", 100, 
                                          5, 5, NIVEL_INICIAL, 
                                          10, 1, 5.0);
        try {
            personaje.agregarCarta(new CartaAtaqueDoble());
            personaje.agregarCarta(new CartaEscudo(6));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personaje;
    }
    
    /**
     * Crea la lista de enemigos en posiciones fijas.
     */
    private static List<Enemigo> crearEnemigos() {
        List<Enemigo> enemigos = new ArrayList<>();
        
        // Nivel 0
        enemigos.add(new Enemigo("Orco", "Guerrero", 50, 20, 12, 0, 5, 1, 0));
        enemigos.add(new Enemigo("Orco", "Guerrero", 50, 22, 13, 0, 5, 1, 0));
        
        // Nivel 1
        enemigos.add(new Enemigo("Esqueleto", "Arquero", 30, 7, 7, 1, 8, 1, 0));
        enemigos.add(new Enemigo("Esqueleto", "Arquero", 30, 20, 10, 1, 8, 1, 0));

        // Nivel 2
        enemigos.add(new Enemigo("Murciélago", "Volador", 10, 20, 5, 2, 2, 1, 0));
        enemigos.add(new Enemigo("Murciélago", "Volador", 10, 30, 12, 2, 2, 1, 0));
        enemigos.add(new Enemigo("Golem", "Tanque", 100, 35, 19, 2, 8, 1, 0));

        // Nivel 3 (Jefe)
        enemigos.add(new Enemigo("Mago Oscuro", "Jefe", 150, 20, 12, 3, 15, 1, 0));
        
        return enemigos;
    }
    
    /**
     * Diseña y "talla" el mundo nivel por nivel.
     */
    private static void crearMundoCoherente(Tablero tablero) {
        
        // 1. Rellenar todo de ROCA
        for (int z = 0; z < NIVELES_TABLERO; z++) {
            for (int y = 0; y < ALTO_TABLERO; y++) {
                for (int x = 0; x < ANCHO_TABLERO; x++) {
                    tablero.getCasillero(x, y, z).setTipo(TipoCasillero.ROCA);
                }
            }
        }

        // --- Nivel 0: Las Cuevas ---
        // Sala de inicio (donde aparece el jugador)
        tallarHabitacion(tablero, 3, 3, 10, 10, 0); 
        // Lago
        tallarHabitacion(tablero, 5, 15, 10, 20, 0, TipoCasillero.AGUA);
        // Sala central
        tallarHabitacion(tablero, 15, 10, 25, 15, 0); 
        // Pasillo 1 (Inicio a Sala Central)
        tallarPasilloHorizontal(tablero, 10, 15, 7, 0);
        tallarPasilloVertical(tablero, 15, 7, 10, 0);
        // Rampa 0->1 (en la sala central)
        tablero.getCasillero(20, 12, 0).setTipo(TipoCasillero.RAMPA);
        // Carta
        tablero.getCasillero(4, 4, 0).setCarta(new CartaCuracionAliado(6));


        // --- Nivel 1: El Cruce ---
        // Sala de llegada (de Rampa 0)
        tallarHabitacion(tablero, 18, 10, 22, 14, 1);
        tablero.getCasillero(20, 12, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->0
        // Sala de salida (a Rampa 2)
        tallarHabitacion(tablero, 3, 3, 10, 10, 1);
        tablero.getCasillero(5, 5, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->2
        // Pasillo de conexión
        tallarPasilloHorizontal(tablero, 10, 18, 10, 1);
        tallarPasilloVertical(tablero, 10, 5, 10, 1);
        // Carta
        tablero.getCasillero(4, 4, 1).setCarta(new CartaEscudo(6));
        

        // --- Nivel 2: La Prisión (Laberinto) ---
        // Sala de llegada (de Rampa 1)
        tallarHabitacion(tablero, 3, 3, 10, 10, 2);
        tablero.getCasillero(5, 5, 2).setTipo(TipoCasillero.RAMPA); // Rampa 2->1
        // Sala de salida (a Rampa 3)
        tallarHabitacion(tablero, 33, 18, 38, 23, 2);
        tablero.getCasillero(35, 20, 2).setTipo(TipoCasillero.RAMPA); // Rampa 2->3
        // Pasillos del laberinto
        tallarPasilloHorizontal(tablero, 10, 30, 5, 2); // Pasillo largo superior
        tallarPasilloVertical(tablero, 30, 5, 20, 2);   // Pasillo largo derecho
        tallarPasilloHorizontal(tablero, 33, 30, 20, 2); // Conexión final
        // Carta
        tablero.getCasillero(15, 5, 2).setCarta(new CartaTeletransportacion(6,10,12));


        // --- Nivel 3: El Santuario (Jefe) ---
        // Sala final amplia
        tallarHabitacion(tablero, 10, 5, 30, 20, 3);
        // Pasillo de llegada
        tallarHabitacion(tablero, 30, 18, 38, 22, 3); // Sala de llegada
        tablero.getCasillero(35, 20, 3).setTipo(TipoCasillero.RAMPA); // Rampa 3->2
        tallarPasilloHorizontal(tablero, 30, 35, 20, 3); // Conexión
        // Cartas de recompensa
        tablero.getCasillero(12, 7, 3).setCarta(new CartaAtaqueDoble());
        tablero.getCasillero(28, 7, 3).setCarta(new CartaAtaqueDoble());
    }

    // --- Métodos de ayuda para tallar el mundo ---

    /** Talla una habitación de VACIO */
    private static void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z) {
        tallarHabitacion(tablero, x1, y1, x2, y2, z, TipoCasillero.VACIO);
    }

    /** Talla una habitación de un tipo específico (ej. AGUA) */
    private static void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z, TipoCasillero tipo) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if (tablero.esCoordenadaValida(x, y, z)) {
                    tablero.getCasillero(x, y, z).setTipo(tipo);
                }
            }
        }
    }

    private static void tallarPasilloHorizontal(Tablero tablero, int x1, int x2, int y, int z) {
        // Asegura que x1 sea el menor
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
    
    private static void tallarPasilloVertical(Tablero tablero, int x, int y1, int y2, int z) {
        // Asegura que y1 sea el menor
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
}