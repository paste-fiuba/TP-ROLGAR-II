package com.items;

import com.entidades.Personaje;

/**
 * Cura toda la vida del jugador al 100%.
 */
public class CartaCuracionTotal extends Carta {

    public CartaCuracionTotal() {
        super("Curación Total", "Restaura la vida del personaje al 100%.");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        usuario.setVida(100);
        System.out.println(usuario.getNombre() + " usó " + nombre + " y recuperó toda su vida!");
    }
}
