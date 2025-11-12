package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.Random;

/**
 * Otorga una probabilidad de esquivar el próximo ataque recibido.
 */
public class CartaEsquivarDanio extends Carta {

    private double probabilidad;
    private Random random = new Random();

    /**
     * pre: probabilidad entre 0 y 1.
     * post: crea una carta que otorga chance de esquivar el próximo ataque.
     */
    public CartaEsquivarDanio(double probabilidad) {
        super("Evasión", "Otorga un " + (int)(probabilidad * 100) +
              "% de probabilidad de esquivar el próximo ataque.");
        this.probabilidad = probabilidad;
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        boolean exito = random.nextDouble() < probabilidad;
        usuario.setEvasionActiva(exito);
        if (exito)
            System.out.println(usuario.getNombre() + " activó Evasión y esquivará el próximo ataque!");
        else
            System.out.println(usuario.getNombre() + " intentó usar Evasión, pero falló...");
    }
}
