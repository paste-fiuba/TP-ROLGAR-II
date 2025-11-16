package com.logica;

public class Turno {

	private int indiceJugadorActual = 0;
	private int ronda = 1;

	public int getIndiceJugadorActual() { return indiceJugadorActual; }
	public int getRonda() { return ronda; }

    /**
     * Avanza al siguiente jugador. Si llega al final, vuelve a 0 y suma una ronda.
     */
	public void nextPlayer(int totalJugadores) {
		indiceJugadorActual++;
		if (indiceJugadorActual >= totalJugadores) {
			indiceJugadorActual = 0;
			ronda++;
		}
	}

    /**
     * Establece forzosamente el índice del jugador.
     */
	public void setIndiceJugadorActual(int idx) { this.indiceJugadorActual = idx; }
}