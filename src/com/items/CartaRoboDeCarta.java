package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.Random;

/**
 * Roba una carta aleatoria del inventario de otro jugador.
 */
public class CartaRoboDeCarta extends Carta {

    private Random random = new Random();

    public CartaRoboDeCarta() {
        super("Robo de Carta", "Roba una carta aleatoria del inventario de otro jugador.");
    }

    /**
     * pre: usuario y objetivo no son null.
     * post: si el objetivo es un Personaje y tiene cartas, una de ellas es removida
     * y agregada al inventario del usuario.
     */
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) return;

        // 1. Verificar si el objetivo es un Personaje
        if (objetivo instanceof Personaje) {
            
            // 2. "Castear" la Entidad a Personaje para acceder al inventario
            Personaje objetivoJugador = (Personaje) objetivo;

            int cantidad = objetivoJugador.getInventario().cantidadDeCartas();
            
            if (cantidad > 0) {
                int indice = random.nextInt(cantidad); 
                Carta robada = objetivoJugador.getInventario().getCarta(indice);

                // Agregar al inventario del usuario
                usuario.getInventario().agregarCarta(robada);

                // Eliminar del inventario del objetivo
                objetivoJugador.getInventario().eliminarCarta(indice);

                System.out.println(usuario.getNombre() + " robó la carta '" +
                                   robada.getNombre() + "' de " + objetivoJugador.getNombre());
            } else {
                System.out.println(objetivoJugador.getNombre() + " no tiene cartas para robar.");
            }
        } else {
            // Si el objetivo es un Enemigo
            System.out.println("No puedes robarle cartas a un " + objetivo.getNombre());
        }
    }
}
