package com.items;

import com.entidades.Personaje;

/**
 * Permite al jugador moverse dos veces en un turno.
 */
public class CartaDobleMovimiento extends Carta {

    public CartaDobleMovimiento() {
        super("Doble Movimiento", "Permite moverse dos veces en el siguiente turno.");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        usuario.setMovimientosExtra(1);
        System.out.println(usuario.getNombre() + " podrá moverse dos veces en el próximo turno.");
    }
}
