package com.items;

import com.entidades.Personaje;
import java.util.Random;

/**
 * Roba una carta aleatoria del inventario de otro jugador.
 */
public class CartaRoboDeCarta extends Carta {

    private Random random = new Random();

    /**
     * post: crea una carta que roba una carta aleatoria del inventario del objetivo.
     */
    public CartaRoboDeCarta() {
        super("Robo de Carta", "Roba una carta aleatoria del inventario de otro jugador.");
    }

    /**
     * pre: usuario y objetivo no son null.
     * post: si el objetivo tiene cartas, una de ellas es removida al azar
     *       y agregada al inventario del usuario. Si no tiene cartas, no ocurre nada.
     */
    @Override
    public void aplicarEfecto(Personaje usuario, Personaje objetivo) {
        if (usuario == null || objetivo == null) return;

        int cantidad = objetivo.getInventario().cantidadDeCartas();
        if (cantidad > 0) {
            int indice = random.nextInt(cantidad); // elige una carta aleatoria
            Carta robada = objetivo.getInventario().getCarta(indice);

            // Agregar al inventario del usuario
            usuario.getInventario().agregarCarta(robada);

            // Eliminar del inventario del objetivo
            objetivo.getInventario().eliminarCarta(indice);

            System.out.println(usuario.getNombre() + " robó la carta '" +
                               robada.getNombre() + "' de " + objetivo.getNombre());
        } else {
            System.out.println(objetivo.getNombre() + " no tiene cartas para robar.");
        }
    }
}
