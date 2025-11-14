package com; 

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.*;
import com.logica.ControladorJuego;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.ui.PanelJuego;
import com.ui.VentanaJuego; 		
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
// El menú ahora se muestra dentro de la ventana del juego (ControladorJuego MENU)

public class Main {

    // --- Constantes del Mundo ---
    private static final int ANCHO_TABLERO = 40;
    private static final int ALTO_TABLERO = 25;
    private static final int NIVELES_TABLERO = 10; // 10 NIVELES
    private static final int NIVEL_INICIAL = 0;

    public static void main(String[] args) {
        // El número de jugadores se selecciona desde el menú dentro de la ventana.

        Tablero miTablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO, NIVELES_TABLERO);
        crearMundoCoherente(miTablero);

        List<Enemigo> listaEnemigos = crearEnemigos();

        // Panel con lista vacía de jugadores; la partida se inicializa desde el menú
        List<Personaje> jugadores = new ArrayList<>();
        PanelJuego miPanel = new PanelJuego(miTablero, jugadores, listaEnemigos, NIVEL_INICIAL);

        // Controlador en modo MENU que conoce el tablero y los enemigos
        ControladorJuego miControlador = new ControladorJuego(miTablero, listaEnemigos, miPanel);
        miPanel.setControlador(miControlador);

        new VentanaJuego(miPanel);
        miPanel.iniciarOyenteTeclado();
    }

    private static Personaje crearPersonaje() {
        // Posición de inicio: Nivel 0, en la sala inicial
        Personaje personaje = new Personaje("Héroe Rolgar", 100, 
                                          5, 5, NIVEL_INICIAL, 
                                          10, 1, 5.0);
        try {
            personaje.agregarCarta(new CartaAtaqueDoble());
            personaje.agregarCarta(new CartaEscudo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personaje;
    }
    
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
        // Nivel 3
        enemigos.add(new Enemigo("Orco", "Guerrero", 60, 15, 15, 3, 7, 1, 0));
        enemigos.add(new Enemigo("Orco", "Guerrero", 60, 17, 15, 3, 7, 1, 0));
        // Nivel 4
        enemigos.add(new Enemigo("Esqueleto", "Elite", 50, 32, 20, 4, 10, 1, 0));
        enemigos.add(new Enemigo("Esqueleto", "Elite", 50, 7, 5, 4, 10, 1, 0));
        // Nivel 5
        enemigos.add(new Enemigo("Serpiente Acuática", "Rápida", 40, 12, 10, 5, 10, 1, 0));
        enemigos.add(new Enemigo("Serpiente Acuática", "Rápida", 40, 28, 15, 5, 10, 1, 0));
        // Nivel 6
        enemigos.add(new Enemigo("Golem", "Tanque", 120, 10, 12, 6, 10, 1, 0));
        enemigos.add(new Enemigo("Golem", "Tanque", 120, 30, 12, 6, 10, 1, 0));
        // Nivel 7
        enemigos.add(new Enemigo("Murciélago", "Enjambre", 15, 7, 18, 7, 3, 1, 0));
        enemigos.add(new Enemigo("Murciélago", "Enjambre", 15, 8, 18, 7, 3, 1, 0));
        enemigos.add(new Enemigo("Murciélago", "Enjambre", 15, 9, 18, 7, 3, 1, 0));
        // Nivel 8
        enemigos.add(new Enemigo("Golem", "Magma", 150, 20, 7, 8, 12, 1, 0));
        // Nivel 9 (Jefe)
        enemigos.add(new Enemigo("REY MAGO", "JEFE", 300, 20, 19, 9, 20, 1, 0));
        
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

        // --- Nivel 0: Las Cuevas (Inicio) ---
        tallarHabitacion(tablero, 3, 3, 10, 10, 0); 
        tallarHabitacion(tablero, 5, 15, 10, 20, 0, TipoCasillero.AGUA);
        tallarHabitacion(tablero, 15, 10, 25, 15, 0); 
        tallarPasilloHorizontal(tablero, 10, 15, 7, 0);
        tallarPasilloVertical(tablero, 15, 7, 10, 0);
        tablero.getCasillero(20, 12, 0).setTipo(TipoCasillero.RAMPA); // Rampa 0->1
        tablero.getCasillero(4, 4, 0).setCarta(new CartaCuracionAliado(30));

        // --- Nivel 1: El Cruce ---
        tallarHabitacion(tablero, 18, 10, 22, 14, 1);
        tablero.getCasillero(20, 12, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->0
        tallarHabitacion(tablero, 3, 3, 10, 10, 1);
        tablero.getCasillero(5, 5, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->2
        tallarPasilloHorizontal(tablero, 10, 18, 10, 1);
        tallarPasilloVertical(tablero, 10, 5, 10, 1);
        tablero.getCasillero(4, 4, 1).setCarta(new CartaEscudo());
        
        // --- Nivel 2: La Prisión ---
        tallarHabitacion(tablero, 3, 3, 10, 10, 2);
        tablero.getCasillero(5, 5, 2).setTipo(TipoCasillero.RAMPA); // Rampa 2->1
        tallarHabitacion(tablero, 33, 18, 38, 23, 2);
        tablero.getCasillero(35, 20, 2).setTipo(TipoCasillero.RAMPA); // Rampa 2->3
        tallarPasilloHorizontal(tablero, 10, 30, 5, 2);
        tallarPasilloVertical(tablero, 30, 5, 20, 2);
        tallarPasilloHorizontal(tablero, 33, 30, 20, 2);

        // --- Nivel 3: El Gran Salón ---
        tallarHabitacion(tablero, 33, 18, 38, 23, 3);
        tablero.getCasillero(35, 20, 3).setTipo(TipoCasillero.RAMPA); // Rampa 3->2
        tallarHabitacion(tablero, 10, 5, 30, 20, 3);
        tallarPasilloHorizontal(tablero, 30, 35, 18, 3);
        tablero.getCasillero(15, 12, 3).setTipo(TipoCasillero.RAMPA); // Rampa 3->4
        
        // --- Nivel 4: Las Tumbas ---
        tallarHabitacion(tablero, 10, 5, 25, 20, 4);
        tablero.getCasillero(15, 12, 4).setTipo(TipoCasillero.RAMPA); // Rampa 4->3
        tallarHabitacion(tablero, 5, 3, 8, 6, 4);
        tallarHabitacion(tablero, 30, 3, 33, 6, 4);
        tallarHabitacion(tablero, 5, 18, 8, 21, 4);
        tallarHabitacion(tablero, 30, 18, 33, 21, 4);
        tallarPasilloHorizontal(tablero, 8, 10, 5, 4);
        tallarPasilloHorizontal(tablero, 25, 30, 5, 4);
        tallarPasilloHorizontal(tablero, 8, 10, 20, 4);
        tallarPasilloHorizontal(tablero, 25, 30, 20, 4);
        tablero.getCasillero(32, 20, 4).setTipo(TipoCasillero.RAMPA); // Rampa 4->5
        tablero.getCasillero(7, 20, 4).setCarta(new CartaInvisibilidad());

        // --- Nivel 5: Caverna de Agua ---
        tallarHabitacion(tablero, 30, 18, 33, 21, 5);
        tablero.getCasillero(32, 20, 5).setTipo(TipoCasillero.RAMPA); // Rampa 5->4
        tallarHabitacion(tablero, 5, 5, 35, 20, 5);
        tallarHabitacion(tablero, 10, 8, 30, 17, 5, TipoCasillero.AGUA);
        tallarPasilloHorizontal(tablero, 25, 30, 19, 5);
        tablero.getCasillero(7, 7, 5).setTipo(TipoCasillero.RAMPA); // Rampa 5->6
        tablero.getCasillero(11, 8, 5).setCarta(new CartaEscudo()); // En la orilla

        // --- Nivel 6: Corazón de la Montaña ---
        tallarHabitacion(tablero, 5, 5, 35, 20, 6);
        tablero.getCasillero(7, 7, 6).setTipo(TipoCasillero.RAMPA); // Rampa 6->5
        tallarHabitacion(tablero, 10, 8, 30, 17, 6, TipoCasillero.ROCA); // Pilar de roca
        tallarHabitacion(tablero, 15, 11, 25, 14, 6); // Túnel a través del pilar
        tablero.getCasillero(33, 12, 6).setTipo(TipoCasillero.RAMPA); // Rampa 6->7
        
        // --- Nivel 7: El Laberinto ---
        tallarHabitacion(tablero, 31, 10, 35, 14, 7);
        tablero.getCasillero(33, 12, 7).setTipo(TipoCasillero.RAMPA); // Rampa 7->6
        tallarHabitacion(tablero, 5, 5, 10, 10, 7);
        tallarHabitacion(tablero, 5, 15, 10, 20, 7);
        tallarHabitacion(tablero, 15, 10, 20, 15, 7);
        tallarPasilloVertical(tablero, 7, 10, 15, 7);
        tallarPasilloHorizontal(tablero, 10, 15, 12, 7);
        tallarPasilloVertical(tablero, 17, 15, 22, 7);
        tallarPasilloHorizontal(tablero, 17, 31, 22, 7);
        tallarPasilloVertical(tablero, 31, 14, 22, 7);
        tablero.getCasillero(7, 18, 7).setTipo(TipoCasillero.RAMPA); // Rampa 7->8
        tablero.getCasillero(16, 11, 7).setCarta(new CartaAtaqueDoble());
        
        // --- Nivel 8: El Nido ---
        tallarHabitacion(tablero, 5, 15, 10, 20, 8);
        tablero.getCasillero(7, 18, 8).setTipo(TipoCasillero.RAMPA); // Rampa 8->7
        tallarHabitacion(tablero, 15, 5, 25, 20, 8);
        tallarPasilloHorizontal(tablero, 10, 15, 18, 8);
        tablero.getCasillero(20, 7, 8).setTipo(TipoCasillero.RAMPA); // Rampa 8->9
        
        // --- Nivel 9: Trono del Jefe ---
        tallarHabitacion(tablero, 15, 5, 25, 10, 9);
        tablero.getCasillero(20, 7, 9).setTipo(TipoCasillero.RAMPA); // Rampa 9->8
        tallarHabitacion(tablero, 10, 15, 30, 23, 9);
        tallarPasilloVertical(tablero, 20, 10, 15, 9);
        tablero.getCasillero(12, 21, 9).setCarta(new CartaCuracionAliado(10));
        tablero.getCasillero(28, 21, 9).setCarta(new CartaCuracionAliado(10));

        // --- Distribuir al menos una de cada tipo de carta en niveles aleatorios (niveles distintos)
        java.util.List<Carta> cartas = new java.util.ArrayList<>();
        cartas.add(new CartaAtaqueDoble());
        cartas.add(new CartaAumentoVida(20));
        cartas.add(new CartaCuracionAliado(30));
        cartas.add(new CartaCuracionTotal());
        cartas.add(new CartaDobleMovimiento());
        cartas.add(new CartaEscudo());
        cartas.add(new CartaEsquivarDanio(0.6));
        cartas.add(new CartaInvisibilidad());
        cartas.add(new CartaRoboDeCarta());
        // Teletransportación apunta al centro del tablero por defecto
        cartas.add(new CartaTeletransportacion(ANCHO_TABLERO/2, ALTO_TABLERO/2, 0));

        Random rnd = new Random();
        java.util.List<Integer> niveles = new java.util.ArrayList<>();
        for (int z = 0; z < NIVELES_TABLERO; z++) niveles.add(z);
        Collections.shuffle(niveles, rnd);

        for (int i = 0; i < cartas.size() && i < niveles.size(); i++) {
            int z = niveles.get(i);
            boolean placed = false;
            // Intentos aleatorios
            for (int attempt = 0; attempt < 500 && !placed; attempt++) {
                int x = rnd.nextInt(ANCHO_TABLERO);
                int y = rnd.nextInt(ALTO_TABLERO);
                if (!tablero.esCoordenadaValida(x, y, z)) continue;
                Casillero c = tablero.getCasillero(x, y, z);
                if (c.getTipo() == TipoCasillero.ROCA) continue;
                if (c.getCarta() != null) continue;
                c.setCarta(cartas.get(i));
                placed = true;
            }
            // Fallback: buscar la primera celda disponible en el nivel
            if (!placed) {
                outer:
                for (int yy = 0; yy < ALTO_TABLERO; yy++) {
                    for (int xx = 0; xx < ANCHO_TABLERO; xx++) {
                        if (!tablero.esCoordenadaValida(xx, yy, z)) continue;
                        Casillero c2 = tablero.getCasillero(xx, yy, z);
                        if (c2.getTipo() != TipoCasillero.ROCA && c2.getCarta() == null) {
                            c2.setCarta(cartas.get(i));
                            placed = true;
                            break outer;
                        }
                    }
                }
            }
        }

        // --- Asegurar al menos 4 cartas por nivel (aleatorias) ---
        final int MIN_CARTAS_POR_NIVEL = 4;
        for (int z = 0; z < NIVELES_TABLERO; z++) {
            // Contar cartas ya presentes en este nivel
            int existentes = 0;
            for (int yy = 0; yy < ALTO_TABLERO; yy++) {
                for (int xx = 0; xx < ANCHO_TABLERO; xx++) {
                    if (!tablero.esCoordenadaValida(xx, yy, z)) continue;
                    Casillero cc = tablero.getCasillero(xx, yy, z);
                    if (cc.getCarta() != null) existentes++;
                }
            }

            int attempts = 0;
            while (existentes < MIN_CARTAS_POR_NIVEL && attempts < 5000) {
                attempts++;
                int x = rnd.nextInt(ANCHO_TABLERO);
                int y = rnd.nextInt(ALTO_TABLERO);
                if (!tablero.esCoordenadaValida(x, y, z)) continue;
                Casillero c = tablero.getCasillero(x, y, z);
                if (c.getTipo() == TipoCasillero.ROCA) continue;
                if (c.getCarta() != null) continue;

                // Elegir una carta aleatoria y crear una nueva instancia
                int choice = rnd.nextInt(10);
                Carta nueva;
                switch (choice) {
                    case 0: nueva = new CartaAtaqueDoble(); break;
                    case 1: nueva = new CartaAumentoVida(20); break;
                    case 2: nueva = new CartaCuracionAliado(30); break;
                    case 3: nueva = new CartaCuracionTotal(); break;
                    case 4: nueva = new CartaDobleMovimiento(); break;
                    case 5: nueva = new CartaEscudo(); break;
                    case 6: nueva = new CartaEsquivarDanio(0.6); break;
                    case 7: nueva = new CartaInvisibilidad(); break;
                    case 8: nueva = new CartaRoboDeCarta(); break;
                    default: nueva = new CartaTeletransportacion(ANCHO_TABLERO/2, ALTO_TABLERO/2, z); break;
                }

                c.setCarta(nueva);
                existentes++;
            }

            // Si no se colocaron suficientes por azar, buscar secuencialmente
            if (existentes < MIN_CARTAS_POR_NIVEL) {
                outer2:
                for (int yy = 0; yy < ALTO_TABLERO; yy++) {
                    for (int xx = 0; xx < ANCHO_TABLERO; xx++) {
                        if (!tablero.esCoordenadaValida(xx, yy, z)) continue;
                        Casillero c2 = tablero.getCasillero(xx, yy, z);
                        if (c2.getTipo() != TipoCasillero.ROCA && c2.getCarta() == null) {
                            // Añadir cartas hasta alcanzar el mínimo
                            int falta = MIN_CARTAS_POR_NIVEL - existentes;
                            for (int k = 0; k < falta; k++) {
                                // Buscar la siguiente celda libre secuencialmente
                                boolean placed = false;
                                for (int y2 = yy; y2 < ALTO_TABLERO && !placed; y2++) {
                                    for (int x2 = (y2 == yy ? xx : 0); x2 < ANCHO_TABLERO; x2++) {
                                        if (!tablero.esCoordenadaValida(x2, y2, z)) continue;
                                        Casillero c3 = tablero.getCasillero(x2, y2, z);
                                        if (c3.getTipo() != TipoCasillero.ROCA && c3.getCarta() == null) {
                                            int choice2 = rnd.nextInt(10);
                                            Carta nueva2;
                                            switch (choice2) {
                                                case 0: nueva2 = new CartaAtaqueDoble(); break;
                                                case 1: nueva2 = new CartaAumentoVida(20); break;
                                                case 2: nueva2 = new CartaCuracionAliado(30); break;
                                                case 3: nueva2 = new CartaCuracionTotal(); break;
                                                case 4: nueva2 = new CartaDobleMovimiento(); break;
                                                case 5: nueva2 = new CartaEscudo(); break;
                                                case 6: nueva2 = new CartaEsquivarDanio(0.6); break;
                                                case 7: nueva2 = new CartaInvisibilidad(); break;
                                                case 8: nueva2 = new CartaRoboDeCarta(); break;
                                                default: nueva2 = new CartaTeletransportacion(ANCHO_TABLERO/2, ALTO_TABLERO/2, z); break;
                                            }
                                            c3.setCarta(nueva2);
                                            existentes++;
                                            placed = true;
                                            if (existentes >= MIN_CARTAS_POR_NIVEL) break outer2;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Métodos de ayuda para tallar el mundo ---

    private static void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z) {
        tallarHabitacion(tablero, x1, y1, x2, y2, z, TipoCasillero.VACIO);
    }

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
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
    
    private static void tallarPasilloVertical(Tablero tablero, int x, int y1, int y2, int z) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
}