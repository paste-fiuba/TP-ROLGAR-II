package com.entidades;

import com.items.Carta;
import java.util.ArrayList;

/**
 * Clase que representa a un jugador dentro del tablero.
 * Hereda de Entidad e incorpora un inventario de cartas de poder.
 */
public class Personaje extends Entidad {

    private ArrayList<Carta> poderes;

    /**
     * pre: los parámetros nombre no es null, vida > 0, fuerza >= 0,
     *      vision >= 0, salud >= 0, y las coordenadas son válidas en el tablero.
     * post: crea un personaje con los valores iniciales dados y una lista vacía de cartas.
     */
    public Personaje(String nombre, int vida, int posX, int posY, int posZ,
                     int fuerza, int vision, double salud) {
        super(nombre, vida, posX, posY, posZ, fuerza, vision, salud);
        this.poderes = new ArrayList<>();
    }

    /**
     * pre: carta no es null.
     * post: agrega la carta al inventario si el personaje tiene menos de 10 cartas.
     *       si ya tiene 10 cartas, no se agrega y se informa por consola.
     */
    public void agregarCarta(Carta carta) {
        if (carta == null) return;

        if (poderes.size() < 10) {
            poderes.add(carta);
            System.out.println(nombre + " obtuvo la carta: " + carta.getNombre());
        } else {
            System.out.println(nombre + " no puede tener más de 10 cartas.");
        }
    }

    /**
     * pre: el personaje tiene al menos una carta en su inventario.
     * post: devuelve la lista actual de cartas del personaje (puede estar vacía si no posee cartas).
     */
    public ArrayList<Carta> getPoderes() {
        return poderes;
    }

    /**
     * pre: el índice está entre 0 y poderes.size() - 1.
     * post: elimina la carta de la posición indicada.
     */
    public void eliminarCarta(int indice) {
        if (indice >= 0 && indice < poderes.size()) {
            Carta eliminada = poderes.remove(indice);
            System.out.println(nombre + " descartó la carta: " + eliminada.getNombre());
        }
    }

    /**
     * pre: el personaje tiene al menos una carta y el índice es válido.
     * post: usa la carta seleccionada contra un objetivo.
     */
    public void usarCarta(int indice, Personaje objetivo) {
        if (indice >= 0 && indice < poderes.size()) {
            Carta carta = poderes.get(indice);
            carta.aplicarEfecto(this, objetivo);
        }
    }

    /**
     * post: devuelve una representación textual del personaje con su nombre y estado actual.
     */
    @Override
    public String toString() {
        return "Jugador " + super.toString() + " | Cartas: " + poderes.size();
    }
}
