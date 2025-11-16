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

    // --- Constantes de Dificultad (lo único que queda) ---
    public enum Dificultad { FACIL, NORMAL, DIFICIL }

    // --- Estado de la Partida ---
    private Tablero tablero;
    private List<Personaje> jugadores;
    private List<Enemigo> enemigos;
    private Dificultad dificultadActual; 

    // --- Generador (Delegación) ---
    private GeneradorDeMundo generador;

    /**
     * pre: -
     * post: Crea una instancia de PartidaDeRolgar, lista para ser cargada.
     */
    public PartidaDeRolgar() {
        this.tablero = null;
        this.jugadores = null;
        this.enemigos = null;
        this.dificultadActual = Dificultad.NORMAL; // Default
        this.generador = new GeneradorDeMundo(); // Instancia el generador
    }

    /**
     * pre: dificultad no es null, numJugadores está entre 1 y 4.
     * post: Carga el tablero, los enemigos y los jugadores en la instancia de la partida.
     * Los atributos (tablero, jugadores, enemigos) quedan listos para ser obtenidos.
     */
    public void cargarPartida(Dificultad dificultad, int numJugadores) {
        this.dificultadActual = dificultad; 
        
        // 1. Crear Tablero
        this.tablero = generador.crearTablero();

        // 2. Crear Enemigos
        this.enemigos = generador.crearEnemigos(dificultad);

        // 3. Crear Jugadores
        this.jugadores = generador.crearJugadores(numJugadores);
        
        // 4. Poblar el tablero (tallar pasillos y poner cartas)
        generador.poblarTablero(this.tablero, dificultad);
    }

    // --- Getters para el AdministradorDeJuego ---

    public Dificultad getDificultad() { 
        return this.dificultadActual;
    }
    
    public Tablero getTablero() {
        return this.tablero;
    }

    public List<Personaje> getJugadores() {
        return this.jugadores;
    }

    public List<Enemigo> getEnemigos() {
        return this.enemigos;
    }
    
    // --- Todos los métodos privados de creación (crearMundoCoherente, distribuirCartas, etc.)
    // --- han sido movidos a GeneradorDeMundo.java ---
}