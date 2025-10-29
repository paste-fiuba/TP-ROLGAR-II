package com.items;

import com.entidades.Personaje;

/**
 * Reduce el daño recibido durante el siguiente ataque.
 */
public class CartaEscudo extends Carta {

    private int reduccion; // porcentaje

    /**
     * pre: reduccion entre 0 y 100
     * post: crea una carta que reduce el daño recibido por el usuario.
     */
    public CartaEscudo(int reduccion) {
        super("Escudo", "Reduce el daño recibido en un " + reduccion + "% durante el próximo ataque.");
        this.reduccion = reduccion;
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        usuario.setEscudoActivo(true, reduccion);
        System.out.println(usuario.getNombre() + " activó un escudo de " + reduccion + "% de reducción de daño.");
    }
}
