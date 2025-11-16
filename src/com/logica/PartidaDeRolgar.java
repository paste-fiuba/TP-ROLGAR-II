package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import com.tablero.Tablero;
import java.util.List;


public class PartidaDeRolgar {

    public enum Dificultad { FACIL, NORMAL, DIFICIL }

    private Tablero tablero;
    private List<Personaje> jugadores;
    private List<Enemigo> enemigos;
    private Dificultad dificultadActual;

    private GeneradorDeMundo generador;

    public PartidaDeRolgar() {
        this.dificultadActual = Dificultad.NORMAL;
        this.generador = new GeneradorDeMundo();
    }

    public void cargarPartida(Dificultad dificultad, int numJugadores) {
        this.dificultadActual = dificultad;
        this.tablero = generador.crearTablero();
        this.enemigos = generador.crearEnemigos(dificultad);
        this.jugadores = generador.crearJugadores(numJugadores);
        generador.poblarTablero(this.tablero, dificultad);
    }

    public Dificultad getDificultad() { return this.dificultadActual; }
    public Tablero getTablero() { return this.tablero; }
    public List<Personaje> getJugadores() { return this.jugadores; }
    public List<Enemigo> getEnemigos() { return this.enemigos; }
}