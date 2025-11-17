package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.ArrayList;
import java.util.Collections;

/**
 * pre: -
 * post: administra una colección de cartas con 10 slots.
 */
public class Inventario {

    private ArrayList<Carta> cartas;
    private static final int MAX_CARTAS = 10;

    /**
     * pre: -
     * post: crea un inventario vacío con 10 slots en null.
     */
    public Inventario() {
        this.cartas = new ArrayList<>(Collections.nCopies(MAX_CARTAS, null));
    }

    /**
     * pre: carta != null.
     * post: la agrega al primer slot vacío disponible.
     */
    public void agregarCarta(Carta carta) {

        boolean cartaValida;
        int i;
        Carta actual;
        boolean colocada;

        cartaValida = (carta != null);
        i = 0;
        colocada = false;

        if (cartaValida) {

            while (i < MAX_CARTAS && !colocada) {
                actual = cartas.get(i);

                if (actual == null) {
                    cartas.set(i, carta);
                    System.out.println("Carta agregada: " + carta.getNombre() + " en slot " + (i + 1));
                    colocada = true;
                }

                i++;
            }

            if (!colocada) {
                System.out.println("No se puede agregar más cartas. Inventario lleno.");
            }
        }
    }

    /**
     * pre: 0 <= indice < 10.
     * post: elimina la carta del slot indicado.
     */
    public void eliminarCarta(int indice) {

        boolean indiceValido;
        Carta eliminada;

        indiceValido = (indice >= 0 && indice < MAX_CARTAS);
        eliminada = null;

        if (indiceValido) {
            eliminada = cartas.get(indice);

            if (eliminada != null) {
                cartas.set(indice, null);
                System.out.println("Carta eliminada: " + eliminada.getNombre());
            }
        }
    }

    /**
     * pre: usuario != null y 0 <= indice < 10.
     * post: aplica el efecto de la carta si existe una en ese slot.
     */
    public void usarCarta(int indice, Personaje usuario, Entidad objetivo) {

        boolean datosValidos;
        Carta carta;

        datosValidos = (usuario != null && indice >= 0 && indice < MAX_CARTAS);
        carta = null;

        if (datosValidos) {
            carta = cartas.get(indice);

            if (carta != null) {
                carta.aplicarEfecto(usuario, objetivo);
            }
        }
    }

    /**
     * pre: -
     * post: devuelve la cantidad de cartas no nulas.
     */
    public int cantidadDeCartas() {

        int contador;
        int i;
        Carta c;

        contador = 0;
        i = 0;

        while (i < MAX_CARTAS) {
            c = cartas.get(i);

            if (c != null) {
                contador = contador + 1;
            }

            i++;
        }

        return contador;
    }

    /**
     * pre: -
     * post: devuelve una lista con solo las cartas no nulas.
     */
    public ArrayList<Carta> getCartas() {

        ArrayList<Carta> lista;
        int i;
        Carta c;

        lista = new ArrayList<>();
        i = 0;

        while (i < MAX_CARTAS) {
            c = cartas.get(i);

            if (c != null) {
                lista.add(c);
            }

            i++;
        }

        return lista;
    }

    /**
     * pre: 0 <= indice < 10.
     * post: devuelve la carta en el slot o null.
     */
    public Carta getCarta(int indice) {

        boolean valido;
        Carta resultado;

        valido = (indice >= 0 && indice < MAX_CARTAS);
        resultado = null;

        if (valido) {
            resultado = cartas.get(indice);
        }

        return resultado;
    }

    /**
     * pre: -
     * post: devuelve la representación textual del inventario.
     */
    @Override
    public String toString() {

        StringBuilder sb;
        int i;
        Carta c;
        String texto;

        sb = new StringBuilder("Inventario:\n");
        i = 0;

        while (i < MAX_CARTAS) {
            sb.append("[").append(i + 1).append("] ");
            c = cartas.get(i);

            if (c != null) {
                sb.append(c.getNombre()).append("\n");
            } else {
                sb.append("- Vacío -\n");
            }

            i++;
        }

        texto = sb.toString();
        return texto;
    }
}