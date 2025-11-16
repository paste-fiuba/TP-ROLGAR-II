package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.Random;
public class CartaRoboDeCarta extends Carta {
    private Random random = new Random();
    public CartaRoboDeCarta() {
        super("Robo de Carta", "Roba una carta aleatoria del inventario de otro jugador.");
    }
    
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        if (usuario == null || objetivo == null) return;
        if (objetivo instanceof Personaje) {
            Personaje objetivoJugador = (Personaje) objetivo;
            int cantidad = objetivoJugador.getInventario().cantidadDeCartas();
            if (cantidad > 0) {
                int indice = random.nextInt(cantidad); 
                Carta robada = objetivoJugador.getInventario().getCarta(indice);
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
    }
}