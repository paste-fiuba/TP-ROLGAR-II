package com.items;

import com.entidades.Personaje;

/**
 * Cura puntos de vida a otro jugador aliado.
 */
public class CartaCuracionAliado extends Carta {

    private int cantidad;

    public CartaCuracionAliado(int cantidad) {
        super("Curación de Aliado", "Cura " + cantidad + " puntos de vida a un aliado.");
        this.cantidad = cantidad;
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        if (objetivo != null) {
            objetivo.setVida(objetivo.getVida() + cantidad);
            System.out.println(usuario.getNombre() + " curó a " + objetivo.getNombre() + " en " + cantidad + " puntos.");
        }
    }
}
