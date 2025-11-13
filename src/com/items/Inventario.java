package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.ArrayList;

/**
 * Representa el conjunto de cartas que posee un personaje.
 * TDA encargado de administrar la colección de cartas (inventario del jugador).
 */
public class Inventario {

    private ArrayList<Carta> cartas;
    private static final int MAX_CARTAS = 10;

    /**
     * pre: -
     * post: crea un inventario vacío con capacidad máxima de 10 cartas.
     */
    public Inventario() {
        this.cartas = new ArrayList<>();
    }

    /**
     * pre: carta no es null.
     * post: agrega la carta al inventario si aún no alcanzó el límite máximo.
     *       si ya hay 10 cartas, no se agrega y se informa por consola.
     */
    public void agregarCarta(Carta carta) {
        if (carta == null) return;

        if (cartas.size() < MAX_CARTAS) {
            cartas.add(carta);
            System.out.println("Carta agregada: " + carta.getNombre());
        } else {
            System.out.println("No se puede agregar más cartas. Inventario lleno.");
        }
    }

    /**
     * pre: índice válido entre 0 y cantidadDeCartas()-1.
     * post: elimina la carta en la posición indicada del inventario.
     */
    public void eliminarCarta(int indice) {
        if (indice >= 0 && indice < cartas.size()) {
            Carta eliminada = cartas.remove(indice);
            System.out.println("Carta eliminada: " + eliminada.getNombre());
        }
    }

    /**
     * pre: índice válido y usuario no null.
     * post: aplica el efecto de la carta seleccionada sobre el objetivo (puede ser null).
     */
    public void usarCarta(int indice, Personaje usuario, Entidad objetivo) {
        if (indice >= 0 && indice < cartas.size() && usuario != null) {
            Carta carta = cartas.get(indice);
            carta.aplicarEfecto(usuario, objetivo);
        }
    }

    /**
     * post: devuelve la cantidad actual de cartas del inventario.
     */
    public int cantidadDeCartas() {
        return cartas.size();
    }

    /**
     * post: devuelve una copia de la lista de cartas.
     */
    public ArrayList<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }

    /**
     * Devuelve la carta en una posición específica del inventario.
     * (¡Necesario para que el Administrador lea el nombre de la carta!)
     *
     * @param indice El slot del inventario (0 a 9).
     * @return La Carta en ese slot, o null si está vacío.
     */
    public Carta getCarta(int indice) {
        if (indice >= 0 && indice < cartas.size()) { // Asumiendo que tu lista se llama 'cartas'
            return cartas.get(indice);
        }
        return null;
    }

    
    
    /**
     * post: devuelve una representación textual del inventario.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Inventario (" + cartas.size() + " cartas):\n");
        for (int i = 0; i < cartas.size(); i++) {
            sb.append("[").append(i).append("] ").append(cartas.get(i).getNombre()).append("\n");
        }
        return sb.toString();
    }
}
