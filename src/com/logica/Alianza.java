package com.logica;

import com.entidades.Personaje;
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
     * pre: personaje != null.
     * post: agrega el personaje a la alianza si no pertenece ya.
     */
    public void agregarMiembro(Personaje p) {
        if (p == null) return;
        if (!miembros.contains(p)) {
            miembros.add(p);
            System.out.println(p.getNombre() + " se unió a la alianza " + nombre);
        } else {
            System.out.println(p.getNombre() + " ya pertenece a la alianza " + nombre);
        }
    }

    /**
     * pre: personaje != null.
     * post: elimina el personaje si pertenece a la alianza.
     */
    public void eliminarMiembro(Personaje p) {
        if (p != null && miembros.contains(p)) {
            miembros.remove(p);
            System.out.println(p.getNombre() + " salió de la alianza " + nombre);
        } else {
            System.out.println("El personaje no pertenece a la alianza.");
        }
    }

    /**
     * post: devuelve true si el personaje pertenece a la alianza.
     */
    public boolean pertenece(Personaje p) {
        return miembros.contains(p);
    }

    /**
     * post: devuelve la cantidad actual de miembros.
     */
    public int cantidadMiembros() {
        return miembros.size();
    }

    /**
     * pre: cantidad > 0.
     * post: todos los miembros de la alianza recuperan vida en la cantidad indicada.
     *       Si un miembro supera 100, se limita a 100.
     */
    public void curarAliados(int cantidad) {
        for (Personaje p : miembros) {
            int nuevaVida = Math.min(p.getVida() + cantidad, 100);
            p.setVida(nuevaVida);
            System.out.println(p.getNombre() + " fue curado por la alianza +" + cantidad + " vida.");
        }
    }

    /**
     * pre: daño >= 0.
     * post: todos los miembros de la alianza reciben daño reducido (trabajo en equipo).
     */
    public void recibirDanioGrupal(int danio) {
        for (Personaje p : miembros) {
            int danioReducido = danio / 2; // daño reducido a la mitad
            p.recibirDanio(danioReducido);
            System.out.println(p.getNombre() + " recibió daño reducido por estar en alianza.");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Alianza " + nombre + " [Miembros: ");
        for (Personaje p : miembros) {
            sb.append(p.getNombre()).append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
