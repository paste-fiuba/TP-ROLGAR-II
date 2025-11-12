package com.entidades;

import com.items.Carta;
import com.items.Inventario;

/**
 * Representa a un jugador dentro del tablero.
 * Hereda de Entidad e incorpora un inventario de cartas y efectos especiales.
 */
public class Personaje extends Entidad {

    private Inventario inventario;

    // Estados especiales
    private boolean escudoActivo;
    private int reduccionEscudo;
    private boolean invisible;
    private boolean evasionActiva;
    private int movimientosExtra;

    /**
     * pre: nombre no es null, vida > 0, fuerza >= 0, vision >= 0, salud >= 0.
     * post: crea un personaje con los atributos indicados,
     *       un inventario vacío y sin efectos activos.
     */
    public Personaje(String nombre, int vida, int posX, int posY, int posZ,
                     int fuerza, int vision, double salud) {
        super(nombre, vida, posX, posY, posZ, fuerza, vision, salud);
        this.inventario = new Inventario();
        this.escudoActivo = false;
        this.invisible = false;
        this.evasionActiva = false;
        this.movimientosExtra = 0;
    }

    /* ===============================
       INVENTARIO (delegación)
       =============================== */

    /**
     * pre: carta no es null.
     * post: agrega la carta al inventario del personaje.
     */
    public void agregarCarta(Carta carta) {
        inventario.agregarCarta(carta);
    }

    /**
     * pre: índice válido (0 ≤ indice < cantidadDeCartas()).
     * post: elimina la carta de esa posición.
     */
    public void eliminarCarta(int indice) {
        inventario.eliminarCarta(indice);
    }

    /**
     * pre: índice válido y usuario no null.
     * post: aplica el efecto de la carta sobre el objetivo.
     */
    public void usarCarta(int indice, Entidad objetivo) {
        inventario.usarCarta(indice, this, objetivo);
    }

    /**
     * post: devuelve el inventario completo del personaje.
     */
    public Inventario getInventario() {
        return inventario;
    }

    /* ===============================
       EFECTOS ESPECIALES
       =============================== */

    /**
     * pre: porcentaje entre 0 y 100.
     * post: activa un escudo que reduce el daño recibido según el porcentaje.
     */
    public void setEscudoActivo(boolean estado, int porcentaje) {
        this.escudoActivo = estado;
        this.reduccionEscudo = porcentaje;
    }

    /**
     * pre: estado puede ser true o false.
     * post: actualiza el estado de invisibilidad del personaje.
     */
    public void setInvisible(boolean estado) {
        this.invisible = estado;
    }

    /**
     * pre: estado puede ser true o false.
     * post: activa o desactiva la evasión para el próximo ataque.
     */
    public void setEvasionActiva(boolean estado) {
        this.evasionActiva = estado;
    }

    /**
     * pre: cantidad >= 0.
     * post: establece cuántos movimientos extra tendrá el personaje.
     */
    public void setMovimientosExtra(int cantidad) {
        this.movimientosExtra = cantidad;
    }

    /**
     * post: devuelve cuántos movimientos extra tiene el personaje actualmente.
     */
    public int getMovimientosExtra() {
        return movimientosExtra;
    }

    /* ===============================
       COMBATE Y DAÑO
       =============================== */

    /**
     * pre: danio >= 0.
     * post: aplica las condiciones activas (invisibilidad, evasión, escudo)
     *       antes de restar la vida. Si invisibilidad o evasión están activas,
     *       el daño puede anularse completamente.
     */
    @Override
    public void recibirDanio(int danio) {
        // Invisibilidad
        if (invisible) {
            System.out.println(nombre + " era invisible y evitó el ataque!");
            invisible = false;
            return;
        }

        // Evasión
        if (evasionActiva) {
            System.out.println(nombre + " esquivó el ataque gracias a su evasión!");
            evasionActiva = false;
            return;
        }

        // Escudo
        if (escudoActivo) {
            int danioReducido = danio - (danio * reduccionEscudo / 100);
            super.recibirDanio(danioReducido);
            System.out.println(nombre + " recibió " + danioReducido +
                               " de daño (escudo activo: -" + reduccionEscudo + "%).");
            escudoActivo = false;
        } else {
            super.recibirDanio(danio);
            System.out.println(nombre + " recibió " + danio + " de daño.");
        }
    }

    /* ===============================
       REPRESENTACIÓN TEXTUAL
       =============================== */

    /**
     * post: devuelve una cadena con el estado completo del personaje.
     */
    @Override
    public String toString() {
        return "Jugador " + nombre +
               " [vida=" + vida +
               ", pos=(" + posX + "," + posY + "," + posZ + ")" +
               ", escudo=" + (escudoActivo ? reduccionEscudo + "%" : "no") +
               ", invisible=" + invisible +
               ", evasion=" + evasionActiva +
               ", movimientosExtra=" + movimientosExtra +
               ", cartas=" + inventario.cantidadDeCartas() + "]";
    }
}
