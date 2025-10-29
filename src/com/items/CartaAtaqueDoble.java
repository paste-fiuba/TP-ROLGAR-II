package com.items;

import com.entidades.Personaje;

/**
 * Permite atacar dos veces consecutivas.
 */
public class CartaAtaqueDoble extends Carta {

    public CartaAtaqueDoble() {
        super("Ataque Doble", "Permite atacar dos veces seguidas al mismo objetivo.");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        if (usuario != null && objetivo != null) {
            usuario.atacar(objetivo);
            usuario.atacar(objetivo);
            System.out.println(usuario.getNombre() + " usó " + nombre + " y atacó dos veces a " + objetivo.getNombre());
        }
    }
}
