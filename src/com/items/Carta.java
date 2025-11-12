package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;

/**
 * Clase abstracta que representa una carta de poder.
 * Cada carta tiene un nombre, una descripción y un efecto
 * que puede aplicarse sobre uno o más personajes.
 */
public abstract class Carta {

    protected String nombre;
    protected String descripcion;

    /**
     * post: inicializa la carta con su nombre y descripción.
     */
    public Carta(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * post: devuelve el nombre de la carta.
     */
    public String getNombre() { return nombre; }

    /**
     * post: devuelve la descripción de la carta.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * pre: el personaje usuario no es null. El objetivo puede ser null si la carta
     *      no requiere objetivo (por ejemplo, cartas de curación o movimiento).
     * post: aplica el efecto específico de la carta sobre el personaje usuario
     *       y/o su objetivo.
     */
    public abstract void aplicarEfecto(Personaje usuario, Entidad objetivo);

    @Override
    public String toString() {
        return nombre + ": " + descripcion;
    }
}
