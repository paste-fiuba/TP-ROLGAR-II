package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;

/**
 * Cura puntos de vida a otro jugador aliado.
 */
public class CartaCuracionAliado extends Carta {

    private int cantidad;

    public CartaCuracionAliado(int cantidad) {
        super("Curación de Aliado", "Cura " + cantidad + " puntos de vida a un aliado.");
        this.cantidad = cantidad;
        this.imagen = cargarImagen("src/sprites/curar-aliado.png");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        // Determinar objetivo: si se pasó explícitamente, usarlo;
        // si no, intentar elegir un aliado de la misma alianza (adyacente preferido, sino el más cercano);
        // si no hay aliado posible, aplicar la curación al propio usuario.
        Personaje objetivoPersonaje = null;
        if (objetivo instanceof Personaje) {
            objetivoPersonaje = (Personaje) objetivo;
        }

        if (objetivoPersonaje == null && usuario != null) {
            com.entidades.Alianza alianza = usuario.getAlianza();
            if (alianza != null) {
                // Buscar aliado adyacente (misma Z, distancia Manhattan == 1)
                int ux = usuario.getPosX();
                int uy = usuario.getPosY();
                int uz = usuario.getPosZ();
                int mejorDist = Integer.MAX_VALUE;
                Personaje candidato = null;
                for (Personaje p : alianza.getMiembros()) {
                    if (p == null || p == usuario) continue;
                    if (p.getVida() <= 0) continue;
                    if (p.getPosZ() != uz) continue;
                    int dist = Math.abs(p.getPosX() - ux) + Math.abs(p.getPosY() - uy);
                    if (dist <= 1) {
                        candidato = p;
                        break;
                    }
                    if (dist < mejorDist) {
                        mejorDist = dist;
                        candidato = p;
                    }
                }
                objetivoPersonaje = candidato;
            }
        }

        if (objetivoPersonaje == null) {
            // No hay aliado válido: la carta no debe curar al propio usuario.
            System.out.println("No hay aliado válido para Curación de Aliado. La carta no tuvo efecto.");
            return;
        }

        int nuevaVida = Math.min(100, objetivoPersonaje.getVida() + cantidad);
        objetivoPersonaje.setVida(nuevaVida);
        System.out.println(usuario.getNombre() + " curó a " + objetivoPersonaje.getNombre() + " en " + cantidad + " puntos.");
    }
}
