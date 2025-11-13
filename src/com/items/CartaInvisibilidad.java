package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;

/**
 * Hace invisible al jugador durante un turno.
 */
public class CartaInvisibilidad extends Carta {

    public CartaInvisibilidad() {
        super("Invisibilidad", "Hace invisible al jugador por un turno, evitando ataques.");
        this.imagen = cargarImagen("src/sprites/ceguera.png");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        usuario.setInvisible(true);
        System.out.println(usuario.getNombre() + " usó " + nombre + " y ahora es invisible por un turno.");
    }
}

