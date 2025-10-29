package com.tablero;

/**
 * Representa una única celda en el tablero
 */

public class Casillero {

    private TipoCasillero tipo;

    /**
     * Constructor del Casillero
     */
    public Casillero() {
        this.tipo = TipoCasillero.VACIO;
    }

    public TipoCasillero getTipo() {
        return tipo;
    }

    public void setTipo(TipoCasillero tipo) {
        this.tipo = tipo;
    }

    public boolean esTransitable() {
        return this.tipo != TipoCasillero.ROCA;
    }
}
