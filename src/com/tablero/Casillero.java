package com.tablero;

import com.items.Carta;

public class Casillero {

    private TipoCasillero tipo;
    private Carta carta;
    private boolean visitado;

    /**
     * post: inicializa el casillero como VACIO y sin carta.
     */
    public Casillero() {
        this.tipo = TipoCasillero.VACIO;
        this.carta = null;
        this.visitado = false; 
    }

    /**
     * post: devuelve el tipo de casillero.
     */
    public TipoCasillero getTipo() {
        return tipo;
    }

    /**
     * pre: tipo no es null.
     * post: establece el tipo de casillero.
     */
    public void setTipo(TipoCasillero tipo) {
        this.tipo = tipo;
    }

    /**
     * post: devuelve la carta en el casillero (o null si no hay).
     */
    public Carta getCarta() {
        return carta;
    }

    /**
     * pre: carta es una Carta válida o null.
     * post: establece la carta en el casillero.
     */
    public void setCarta(Carta carta) {
        this.carta = carta;
    }
    
    
    /**
     * post: devuelve true si el casillero ha sido visitado/descubierto.
     */
    public boolean isVisitado() {
        return visitado;
    }
    
    /**
     * post: marca el casillero como visitado.
     */
    public void marcarVisitado() {
        this.visitado = true;
    }
}