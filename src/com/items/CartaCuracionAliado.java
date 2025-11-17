package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import com.entidades.Alianza;

/**
 * pre: cantidad > 0.
 * post: crea una carta que cura a un aliado.
 */
public class CartaCuracionAliado extends Carta {

    private int cantidad;

    /**
     * pre: cantidad > 0.
     * post: inicializa la carta con el valor dado.
     */
    public CartaCuracionAliado(int cantidad) {
        super("Curación de Aliado", "Cura " + cantidad + " puntos de vida a un aliado.");
        this.cantidad = cantidad;
    }

    /**
     * pre: usuario != null.
     * post: cura al objetivo si es válido; si no, cura al aliado más cercano posible.
     */
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {

        Personaje objetivoPersonaje;
        Alianza alianzaUsuario;
        int ux;
        int uy;
        int uz;
        int mejorDist;
        Personaje candidato;
        int dist;
        boolean objetivoEsPersonaje;
        boolean tieneAlianza;
        boolean hayObjetivo;

        objetivoPersonaje = null;
        alianzaUsuario = null;
        ux = 0;
        uy = 0;
        uz = 0;
        mejorDist = Integer.MAX_VALUE;
        candidato = null;
        dist = 0;
        objetivoEsPersonaje = false;
        tieneAlianza = false;
        hayObjetivo = false;

        if (objetivo instanceof Personaje) {
            objetivoEsPersonaje = true;
        }

        if (objetivoEsPersonaje) {
            objetivoPersonaje = (Personaje) objetivo;
        }

        hayObjetivo = (objetivoPersonaje != null);

        if (!hayObjetivo && usuario != null) {

            alianzaUsuario = usuario.getAlianza();
            tieneAlianza = (alianzaUsuario != null);

            if (tieneAlianza) {

                ux = usuario.getPosX();
                uy = usuario.getPosY();
                uz = usuario.getPosZ();

                int i;
                int n;
                Personaje p;

                i = 0;
                n = alianzaUsuario.getMiembros().size();

                while (i < n) {

                    p = alianzaUsuario.getMiembros().get(i);

                    if (p != null) {
                        if (p != usuario) {
                            if (p.getVida() > 0) {
                                if (p.getPosZ() == uz) {

                                    dist = Math.abs(p.getPosX() - ux) +
                                           Math.abs(p.getPosY() - uy);

                                    if (dist <= 1) {
                                        candidato = p;
                                        mejorDist = dist;
                                    } else {
                                        if (dist < mejorDist) {
                                            mejorDist = dist;
                                            candidato = p;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    i++;
                }

                objetivoPersonaje = candidato;
            }
        }

        hayObjetivo = (objetivoPersonaje != null);

        if (!hayObjetivo) {
            System.out.println("No hay aliado válido para Curación de Aliado. La carta no tuvo efecto.");
        } else {
            int vidaActual;
            int nuevaVida;
            vidaActual = objetivoPersonaje.getVida();
            nuevaVida = vidaActual + cantidad;

            if (nuevaVida > 100) {
                nuevaVida = 100;
            }

            objetivoPersonaje.setVida(nuevaVida);
            System.out.println(usuario.getNombre() + " curó a " + objetivoPersonaje.getNombre() + " en " + cantidad + " puntos.");
        }
    }
}