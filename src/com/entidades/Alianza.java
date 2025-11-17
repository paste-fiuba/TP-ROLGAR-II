package com.entidades;

import java.util.ArrayList;

/**
 * Representa una alianza entre personajes.
 * Los personajes aliados cooperan y pueden curarse entre sí.
 */
public class Alianza {

    private String nombre;
    private ArrayList<Personaje> miembros;

    /**
     * pre: nombre != null.
     * post: crea una alianza vacía con el nombre indicado.
     */
    public Alianza(String nombre) {
        this.nombre = nombre;
        this.miembros = new ArrayList<>();
    }

    /**
     * pre: p != null.
     * post: agrega el personaje a la alianza si no pertenece ya.
     */
    public void agregarMiembro(Personaje p) {
        boolean puedeAgregar;
        boolean yaPertenece;

        puedeAgregar = (p != null);
        yaPertenece = false;

        if (puedeAgregar) {
            yaPertenece = miembros.contains(p);
        }

        if (puedeAgregar && !yaPertenece) {
            miembros.add(p);
            p.setAlianza(this);
            System.out.println(p.getNombre() + " se unió a la alianza " + nombre);
        } else if (puedeAgregar) {
            System.out.println(p.getNombre() + " ya pertenece a la alianza " + nombre);
        }
    }

    /**
     * pre: p != null.
     * post: elimina el personaje si pertenece a la alianza.
     */
    public void eliminarMiembro(Personaje p) {
        boolean puedeEliminar;
        boolean pertenece;

        puedeEliminar = (p != null);
        pertenece = false;

        if (puedeEliminar) {
            pertenece = miembros.contains(p);
        }

        if (puedeEliminar && pertenece) {
            miembros.remove(p);
            p.setAlianza(null);
            System.out.println(p.getNombre() + " salió de la alianza " + nombre);
        } else {
            System.out.println("El personaje no pertenece a la alianza.");
        }
    }

    /**
     * post: devuelve true si el personaje pertenece a la alianza.
     */
    public boolean pertenece(Personaje p) {
        boolean pertenece;
        pertenece = miembros.contains(p);
        return pertenece;
    }

    /**
     * post: devuelve la cantidad actual de miembros.
     */
    public int cantidadMiembros() {
        int cantidad;
        cantidad = miembros.size();
        return cantidad;
    }

    /**
     * post: devuelve la lista de miembros (lectura).
     */
    public ArrayList<Personaje> getMiembros() {
        ArrayList<Personaje> copia;
        copia = new ArrayList<>(miembros);
        return copia;
    }

    /**
     * pre: cantidad > 0.
     * post: todos los miembros recuperan vida en la cantidad indicada (máx 100).
     */
    public void curarAliados(int cantidad) {
        Personaje actual;
        int vidaActual;
        int nuevaVida;
        int i;
        int tope;

        i = 0;
        tope = miembros.size();

        while (i < tope) {
            actual = miembros.get(i);
            vidaActual = actual.getVida();
            nuevaVida = vidaActual + cantidad;

            if (nuevaVida > 100) {
                nuevaVida = 100;
            }

            actual.setVida(nuevaVida);
            System.out.println(actual.getNombre() + " fue curado por la alianza +" + cantidad + " vida.");
            i++;
        }
    }

    /**
     * pre: danio >= 0.
     * post: todos reciben daño reducido.
     */
    public void recibirDanioGrupal(int danio) {
        Personaje actual;
        int danioReducido;
        int i;
        int tope;

        danioReducido = danio / 2;
        i = 0;
        tope = miembros.size();

        while (i < tope) {
            actual = miembros.get(i);
            actual.recibirDanio(danioReducido);
            System.out.println(actual.getNombre() + " recibió daño reducido por estar en alianza.");
            i++;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb;
        Personaje actual;
        int i;
        int tope;

        sb = new StringBuilder("Alianza " + nombre + " [Miembros: ");
        i = 0;
        tope = miembros.size();

        while (i < tope) {
            actual = miembros.get(i);
            sb.append(actual.getNombre()).append(", ");
            i++;
        }

        sb.append("]");
        return sb.toString();
    }
}
