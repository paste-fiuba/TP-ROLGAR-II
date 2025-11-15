package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.items.*;
import com.tablero.Casillero;
import com.tablero.Tablero;
import com.tablero.TipoCasillero;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * TDA que encapsula la lógica de creación y configuración de una partida de Rolgar.
 * Se encarga de generar el tablero, los enemigos y los jugadores según la
 * dificultad seleccionada.
 */
public class PartidaDeRolgar {

    // --- Constantes del Mundo ---
    private static final int ANCHO_TABLERO = 40;
    private static final int ALTO_TABLERO = 25;
    private static final int NIVELES_TABLERO = 10;
    private static final int NIVEL_INICIAL = 0;

    public enum Dificultad { FACIL, NORMAL, DIFICIL }

    private Tablero tablero;
    private List<Personaje> jugadores;
    private List<Enemigo> enemigos;
    private Random random = new Random();

    /**
     * pre: -
     * post: Crea una instancia de PartidaDeRolgar, lista para ser cargada.
     */
    public PartidaDeRolgar() {
        this.tablero = null;
        this.jugadores = null;
        this.enemigos = null;
    }

    /**
     * pre: dificultad no es null, numJugadores está entre 1 y 4.
     * post: Carga el tablero, los enemigos y los jugadores en la instancia de la partida.
     * Los atributos (tablero, jugadores, enemigos) quedan listos para ser obtenidos.
     */
    public void cargarPartida(Dificultad dificultad, int numJugadores) {
        // 1. Crear Tablero basado en dificultad
        this.tablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO, NIVELES_TABLERO);
        crearMundoCoherente(this.tablero, dificultad);

        // 2. Crear Enemigos basados en dificultad
        this.enemigos = crearEnemigos(dificultad);

        // 3. Crear Jugadores
        this.jugadores = crearJugadores(numJugadores);
    }

    // --- Getters para el AdministradorDeJuego ---

    /**
     * pre: la partida debe haber sido cargada (cargarPartida).
     * post: devuelve el tablero generado.
     */
    public Tablero getTablero() {
        return this.tablero;
    }

    /**
     * pre: la partida debe haber sido cargada (cargarPartida).
     * post: devuelve la lista de jugadores generados.
     */
    public List<Personaje> getJugadores() {
        return this.jugadores;
    }

    /**
     * pre: la partida debe haber sido cargada (cargarPartida).
     * post: devuelve la lista de enemigos generados.
     */
    public List<Enemigo> getEnemigos() {
        return this.enemigos;
    }

    // --- Lógica de Creación (Movida de Main.java) ---

    /**
     * pre: numJugadores debe ser > 0 y <= 4.
     * post: Devuelve una lista de Personajes inicializados.
     */
    private List<Personaje> crearJugadores(int numJugadores) {
        List<Personaje> listaJugadores = new ArrayList<>();
        String[] nombres = {"Jugador1", "Jugador2", "Jugador3", "Jugador4"};
        // TODO: Cargar sprites diferentes para cada jugador
        // String[] sprites = {"personaje.png", "personaje2.png", "personaje3.png", "personaje4.png"}; 

        for (int i = 0; i < numJugadores; i++) {
            int startX = 8; // Posiciones iniciales separadas
            int startY = 8;
            if(i%2==0) {
            	startX = 5;
            }
            if(i<2) {
            	startY = 5;
            }
            
            Personaje p = new Personaje(nombres[i], 100, startX, startY, NIVEL_INICIAL, 10, 1, 5.0);
            try {
                // Dar a cada jugador cartas iniciales
                p.agregarCarta(new CartaAtaqueDoble());
                p.agregarCarta(new CartaEscudo());
            } catch (Exception ex) {
                System.err.println("Error al agregar cartas iniciales a " + p.getNombre());
            }
            listaJugadores.add(p);
        }
        return listaJugadores;
    }

    /**
     * pre: dificultad no es null.
     * post: Devuelve una lista de Enemigos configurada para la dificultad.
     */
    private List<Enemigo> crearEnemigos(Dificultad dificultad) {
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

        if (dificultad == Dificultad.FACIL) {
            // En fácil, quitar la mitad de los enemigos (excepto el jefe)
            enemigos.removeIf(e -> !e.getNombre().equals("REY MAGO") && random.nextBoolean());
        } else if (dificultad == Dificultad.DIFICIL) {
            // En difícil, duplicar la vida de todos
            for (Enemigo e : enemigos) {
                e.setVida(e.getVida() * 2);
                // NOTA: Para modificar la fuerza, la clase Entidad necesitaría un setFuerza()
                // e.setFuerza(e.getFuerza() * 2); 
            }
        }
        return enemigos;
    }

    /**
     * pre: tablero no es null, dificultad no es null.
     * post: Modifica el tablero, tallando el mundo, pasillos, rampas y distribuyendo cartas.
     */
    private void crearMundoCoherente(Tablero tablero, Dificultad dificultad) {
        
        // 1. Rellenar todo de roca
        for (int z = 0; z < NIVELES_TABLERO; z++) {
            for (int y = 0; y < ALTO_TABLERO; y++) {
                for (int x = 0; x < ANCHO_TABLERO; x++) {
                    if (tablero.esCoordenadaValida(x, y, z)) {
                         tablero.getCasillero(x, y, z).setTipo(TipoCasillero.ROCA);
                    }
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
        
        // --- Nivel 1: El Cruce ---
        tallarHabitacion(tablero, 18, 10, 22, 14, 1);
        tablero.getCasillero(20, 12, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->0
        tallarHabitacion(tablero, 3, 3, 10, 10, 1);
        tablero.getCasillero(5, 5, 1).setTipo(TipoCasillero.RAMPA); // Rampa 1->2
        tallarPasilloHorizontal(tablero, 10, 18, 10, 1);
        tallarPasilloVertical(tablero, 10, 5, 10, 1);
        
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

        // --- Nivel 5: Caverna de Agua ---
        tallarHabitacion(tablero, 30, 18, 33, 21, 5);
        tablero.getCasillero(32, 20, 5).setTipo(TipoCasillero.RAMPA); // Rampa 5->4
        tallarHabitacion(tablero, 5, 5, 35, 20, 5);
        tallarHabitacion(tablero, 10, 8, 30, 17, 5, TipoCasillero.AGUA);
        tallarPasilloHorizontal(tablero, 25, 30, 19, 5);
        tablero.getCasillero(7, 7, 5).setTipo(TipoCasillero.RAMPA); // Rampa 5->6

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

        // --- Distribuir Cartas ---
        distribuirCartas(tablero, dificultad);
    }

    /**
     * pre: tablero no es null y ha sido tallado.
     * post: Distribuye cartas aleatorias por el tablero.
     */
    private void distribuirCartas(Tablero tablero, Dificultad dificultad) {
        
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
        cartas.add(new CartaTeletransportacion(ANCHO_TABLERO/2, ALTO_TABLERO/2, 0)); // TP al inicio

        java.util.List<Integer> niveles = new java.util.ArrayList<>();
        for (int z = 0; z < NIVELES_TABLERO; z++) niveles.add(z);
        Collections.shuffle(niveles, random);

        for (int i = 0; i < cartas.size() && i < niveles.size(); i++) {
            int z = niveles.get(i);
            boolean placed = false;
            // Intentos aleatorios
            for (int attempt = 0; attempt < 500 && !placed; attempt++) {
                int x = random.nextInt(ANCHO_TABLERO);
                int y = random.nextInt(ALTO_TABLERO);
                if (!tablero.esCoordenadaValida(x, y, z)) continue;
                Casillero c = tablero.getCasillero(x, y, z);
                if (c.getTipo() == TipoCasillero.ROCA || c.getCarta() != null) continue;
                c.setCarta(cartas.get(i));
                placed = true;
            }
        }

        // --- Asegurar MÍNIMO de cartas por nivel según dificultad ---
        int minCartasPorNivel = 4;
        if (dificultad == Dificultad.FACIL) {
            minCartasPorNivel = 6; // Más cartas en fácil
        } else if (dificultad == Dificultad.DIFICIL) {
            minCartasPorNivel = 2; // Menos cartas en difícil
        }
        
        final int MIN_CARTAS_POR_NIVEL = minCartasPorNivel;

        for (int z = 0; z < NIVELES_TABLERO; z++) {
            int existentes = 0;
            for (int yy = 0; yy < ALTO_TABLERO; yy++) {
                for (int xx = 0; xx < ANCHO_TABLERO; xx++) {
                    if (tablero.esCoordenadaValida(xx, yy, z) && tablero.getCasillero(xx, yy, z).getCarta() != null) {
                        existentes++;
                    }
                }
            }

            int attempts = 0;
            while (existentes < MIN_CARTAS_POR_NIVEL && attempts < 5000) {
                attempts++;
                int x = random.nextInt(ANCHO_TABLERO);
                int y = random.nextInt(ALTO_TABLERO);
                if (!tablero.esCoordenadaValida(x, y, z)) continue;
                Casillero c = tablero.getCasillero(x, y, z);
                if (c.getTipo() == TipoCasillero.ROCA || c.getCarta() != null) continue;

                c.setCarta(crearCartaAleatoria(z));
                existentes++;
            }
        }
    }

    /**
     * pre: z es un nivel válido.
     * post: Devuelve una nueva instancia de una carta aleatoria.
     */
    private Carta crearCartaAleatoria(int z) {
        int choice = random.nextInt(10);
        switch (choice) {
            case 0: return new CartaAtaqueDoble();
            case 1: return new CartaAumentoVida(20);
            case 2: return new CartaCuracionAliado(30);
            case 3: return new CartaCuracionTotal();
            case 4: return new CartaDobleMovimiento();
            case 5: return new CartaEscudo();
            case 6: return new CartaEsquivarDanio(0.6);
            case 7: return new CartaInvisibilidad();
            case 8: return new CartaRoboDeCarta();
            default: return new CartaTeletransportacion(ANCHO_TABLERO/2, ALTO_TABLERO/2, z);
        }
    }


    // --- Métodos de ayuda para tallar el mundo (Movidos de Main) ---

    /**
     * pre: tablero no es null, coordenadas válidas, z válido, tipo no es null.
     * post: Talla una habitación del tipo especificado en el tablero.
     */
    private void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z, TipoCasillero tipo) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if (tablero.esCoordenadaValida(x, y, z)) {
                    tablero.getCasillero(x, y, z).setTipo(tipo);
                }
            }
        }
    }

    /**
     * pre: tablero no es null, coordenadas válidas, z válido.
     * post: Talla una habitación de tipo VACIO.
     */
    private void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z) {
        tallarHabitacion(tablero, x1, y1, x2, y2, z, TipoCasillero.VACIO);
    }

    /**
     * pre: tablero no es null, coordenadas válidas, z válido.
     * post: Talla un pasillo horizontal de tipo VACIO.
     */
    private static void tallarPasilloHorizontal(Tablero tablero, int x1, int x2, int y, int z) {
        for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
    
    /**
     * pre: tablero no es null, coordenadas válidas, z válido.
     * post: Talla un pasillo vertical de tipo VACIO.
     */
    private static void tallarPasilloVertical(Tablero tablero, int x, int y1, int y2, int z) {
        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
}