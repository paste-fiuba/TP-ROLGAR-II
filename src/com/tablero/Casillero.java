package com.tablero;

import com.items.Carta; // <-- Importa la clase Carta

public class Casillero {

    private TipoCasillero tipo;
    private Carta carta; 

    public Casillero() {
        this.tipo = TipoCasillero.VACIO;
        this.carta = null; 
    }

    // Getters y Setters
    
    public TipoCasillero getTipo() {
        return tipo;
    }

    public void setTipo(TipoCasillero tipo) {
        this.tipo = tipo;
    }

    public Carta getCarta() {
        return this.carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public boolean esTransitable() {
        return this.tipo != TipoCasillero.ROCA;
    }
}
