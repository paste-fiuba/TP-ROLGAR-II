package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.ArrayList;
import java.util.Collections; 

/**
 * TDA encargado de administrar la colección de cartas (inventario del jugador).
 */
public class Inventario {

    private ArrayList<Carta> cartas;
    private static final int MAX_CARTAS = 10;

    /**
     * pre: 
     * post: crea un inventario vacío con 10 slots (llenos de null).
     */
    public Inventario() {
        this.cartas = new ArrayList<>(Collections.nCopies(MAX_CARTAS, null));
    }

    /**
     * pre: carta no es null.
     * post: agrega la carta al primer slot vacío (null) que encuentra.
     */
    public void agregarCarta(Carta carta) {
        if (carta == null) return;

        for (int i = 0; i < MAX_CARTAS; i++) {
            if (cartas.get(i) == null) {
                cartas.set(i, carta);
                System.out.println("Carta agregada: " + carta.getNombre() + " en slot " + (i + 1));
                return;
            }
        }
        
        System.out.println("No se puede agregar más cartas. Inventario lleno.");
    }

    /**
     * pre: índice válido entre 0 y 9.
     * post: vacía el slot en la posición indicada (lo pone en null).
     */
    public void eliminarCarta(int indice) {
        if (indice >= 0 && indice < MAX_CARTAS) { 
            Carta eliminada = cartas.get(indice); 
            if (eliminada != null) {
                cartas.set(indice, null); 
                System.out.println("Carta eliminada: " + eliminada.getNombre());
            }
        }
    }

    /**
     * pre: índice válido y usuario no null.
     * post: aplica el efecto de la carta si existe una en ese slot.
     */
    public void usarCarta(int indice, Personaje usuario, Entidad objetivo) {
        if (indice >= 0 && indice < MAX_CARTAS && usuario != null) {
            Carta carta = cartas.get(indice);
            if (carta != null) { 
                carta.aplicarEfecto(usuario, objetivo);
            }
        }
    }

    /**
     * post: devuelve la cantidad de cartas reales (no-null) en el inventario.
     */
    public int cantidadDeCartas() {

        int count = 0;
        for (Carta c : cartas) {
            if (c != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * post: devuelve una copia de la lista de cartas (solo las no null).
     */
    public ArrayList<Carta> getCartas() {
        ArrayList<Carta> soloCartas = new ArrayList<>();
        for (Carta c : cartas) {
            if (c != null) {
                soloCartas.add(c);
            }
        }
        return soloCartas;
    }


    public Carta getCarta(int indice) {
        // --- CAMBIO ---
        // Ahora .get(indice) puede devolver null, lo cual es correcto.
        if (indice >= 0 && indice < MAX_CARTAS) {
            return cartas.get(indice); 
        }
        return null;
    }

    
    
    /**
     * post: devuelve una representación textual de los 10 slots.
     */
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("Inventario:\n");
        for (int i = 0; i < MAX_CARTAS; i++) {
            sb.append("[").append(i + 1).append("] "); // Slots 1-10
            Carta c = cartas.get(i);
            if (c != null) {
                sb.append(c.getNombre()).append("\n");
            } else {
                sb.append("- Vacío -\n");
            }
        }
        return sb.toString();
    }
}