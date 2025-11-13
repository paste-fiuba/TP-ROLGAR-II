package com.items; 

import com.entidades.Entidad;
import com.entidades.Personaje;

public class CartaEscudo extends Carta {

    private static final int VALOR_ESCUDO = 30; 

    /**
     * Constructor.
     */
    public CartaEscudo() {
        super("Escudo", "Otorga " + VALOR_ESCUDO + " de vida temporal.");
        this.imagen = cargarImagen("src/sprites/escudo.png");
    }

    /**
     * Aplica el buff "Escudo" (HP temporal) al usuario.
     */
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        if (usuario != null) {
            usuario.agregarVidaEscudo(VALOR_ESCUDO);
        }
    }
}