package com.entidades;

import java.util.Random;

/**
 * Clase que representa a un enemigo dentro del tablero.
 * Hereda de Entidad e incluye comportamiento automático.
 */
public class Enemigo extends Entidad {

    private boolean vivo;
    private String tipo;
    private Random random = new Random();

    /**
     * pre: nombre y tipo no son null, vida > 0, fuerza >= 0, vision >= 0, salud >= 0,
     *      y las coordenadas son válidas dentro del tablero.
     * post: crea un enemigo del tipo indicado con los valores iniciales dados
     *       y lo marca como "vivo".
     */
    public Enemigo(String nombre, String tipo, int vida, int posX, int posY, int posZ,
                   int fuerza, int vision, double salud) {
        super(nombre, vida, posX, posY, posZ, fuerza, vision, salud);
        this.tipo = tipo;
        this.vivo = true;
    }

    /**
     * pre: maxX, maxY, maxZ > 0 (tamaño del tablero válido).
     * post: mueve al enemigo aleatoriamente una casilla en alguna dirección del tablero,
     *       respetando los límites dados por maxX, maxY y maxZ.
     */
    public void moverAleatorio(int maxX, int maxY, int maxZ) {
        if (!vivo) return;

        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        int dz = random.nextInt(3) - 1;

        int nuevoX = posX + dx;
        int nuevoY = posY + dy;
        int nuevoZ = posZ + dz;

        // Mantiene dentro de los límites del tablero
        if (nuevoX >= 0 && nuevoX < maxX &&
            nuevoY >= 0 && nuevoY < maxY &&
            nuevoZ >= 0 && nuevoZ < maxZ) {
            setPosicion(nuevoX, nuevoY, nuevoZ);
            System.out.println(nombre + " (" + tipo + ") se movió a (" +
                               nuevoX + "," + nuevoY + "," + nuevoZ + ").");
        }
    }

    /**
     * pre: jugador no es null y está vivo.
     * post: realiza un ataque al jugador, reduciendo su vida según la fuerza del enemigo.
     */
    public void atacarJugador(Personaje jugador) {
        if (!vivo || jugador == null) return;

        System.out.println(nombre + " (" + tipo + ") ataca a " + jugador.getNombre());
        jugador.recibirDanio(fuerza);
    }

    /**
     * pre: danio >= 0.
     * post: reduce la vida del enemigo en la cantidad de daño recibida.
     *       si la vida llega a 0 o menos, lo marca como "muerto".
     */
    @Override
    public void recibirDanio(int danio) {
        super.recibirDanio(danio);
        if (vida <= 0) {
            vivo = false;
            System.out.println(nombre + " (" + tipo + ") ha sido derrotado.");
        }
    }

    /**
     * post: devuelve true si el enemigo sigue con vida, false si fue derrotado.
     */
    public boolean estaVivo() {
        return vivo;
    }

    /**
     * post: devuelve el tipo del enemigo (por ejemplo, "Orco", "Mago", etc.).
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * post: devuelve una descripción textual del enemigo (nombre, tipo, vida y posición).
     */
    @Override
    public String toString() {
        return "Enemigo " + tipo + " - " + super.toString() + (vivo ? " [VIVO]" : " [DERROTADO]");
    }
}
