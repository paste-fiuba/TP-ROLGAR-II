package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.Random;

/**
 * pre: usuario != null.
 * post: intenta robar una carta al objetivo si es un personaje con inventario.
 */
public class CartaRoboDeCarta extends Carta {

    private Random random;

    /**
     * pre: -
     * post: inicializa la carta de robo.
     */
    public CartaRoboDeCarta() {
        super("Robo de Carta", "Roba una carta aleatoria del inventario de otro jugador.");
        this.random = new Random();
    }

    /**
     * pre: usuario != null.
     * post: roba una carta aleatoria de un objetivo válido si es posible.
     */
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {

        boolean usuarioValido;
        boolean objetivoValido;
        boolean objetivoEsPersonaje;
        Personaje objetivoJugador;
        int cantidad;
        int indice;
        Carta robada;

        usuarioValido = (usuario != null);
        objetivoValido = (objetivo != null);
        objetivoEsPersonaje = false;
        objetivoJugador = null;
        cantidad = 0;
        indice = 0;
        robada = null;

        if (usuarioValido && objetivoValido) {

            if (objetivo instanceof Personaje) {
                objetivoEsPersonaje = true;
                objetivoJugador = (Personaje) objetivo;
            }

            if (objetivoEsPersonaje) {

                cantidad = objetivoJugador.getInventario().cantidadDeCartas();

                if (cantidad > 0) {
                    indice = random.nextInt(cantidad);
                    robada = objetivoJugador.getInventario().getCarta(indice);

                    usuario.getInventario().agregarCarta(robada);
                    objetivoJugador.getInventario().eliminarCarta(indice);

                    System.out.println(usuario.getNombre() + " robó la carta '" +
                                       robada.getNombre() + "' de " + objetivoJugador.getNombre());
                } else {
                    System.out.println(objetivoJugador.getNombre() + " no tiene cartas para robar.");
                }

            } else {
                System.out.println("No puedes robarle cartas a un " + objetivo.getNombre());
            }

        } else {
            System.out.println("No es posible aplicar el efecto de robo de carta.");
        }
    }
}