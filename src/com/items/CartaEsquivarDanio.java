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
        this.imagen = cargarImagen("src/sprites/esquivar.png");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        // Establecer probabilidad de esquivar el próximo ataque (se consumirá al intentar recibir daño)
        usuario.setProbabilidadEsquiva(probabilidad);
        System.out.println(usuario.getNombre() + " usó " + nombre + " y tiene " + (int)(probabilidad * 100) + "% de chance de esquivar el próximo ataque.");
    }
}
