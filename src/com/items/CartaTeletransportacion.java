package com.items;

import com.entidades.Personaje;

/**
 * Mueve instantáneamente al jugador a una nueva posición.
 */
public class CartaTeletransportacion extends Carta {

    private int nuevaX, nuevaY, nuevaZ;

    /**
     * pre: coordenadas válidas en el tablero.
     * post: mueve al usuario a la posición indicada.
     */
    public CartaTeletransportacion(int x, int y, int z) {
        super("Teletransportación", "Teletransporta al jugador a (" + x + "," + y + "," + z + ").");
        this.nuevaX = x;
        this.nuevaY = y;
        this.nuevaZ = z;
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        usuario.setPosicion(nuevaX, nuevaY, nuevaZ);
        System.out.println(usuario.getNombre() + " se teletransportó a (" + nuevaX + "," + nuevaY + "," + nuevaZ + ").");
    }
}
