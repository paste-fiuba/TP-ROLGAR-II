package com.entidades;

import java.util.Random;

/**
 * Clase que representa a un enemigo dentro del tablero.
 * Hereda de Entidad e incluye comportamiento automático.
 */
public class Enemigo extends Entidad {

    private boolean vivo;
    private String tipo;
    private Random random;

    /**
     * pre: nombre y tipo no son null, vida > 0, fuerza >= 0, vision >= 0, salud >= 0,
     *      y las coordenadas son válidas.
     * post: crea un enemigo del tipo indicado con los valores iniciales
     *       y lo marca como vivo.
     */
    public Enemigo(String nombre, String tipo, int vida, int posX, int posY, int posZ,
                   int fuerza, int vision, double salud) {
        super(nombre, vida, posX, posY, posZ, fuerza, vision, salud);
        this.tipo = tipo;
        this.vivo = true;
        this.random = new Random();
    }

    /**
     * pre: maxX, maxY, maxZ > 0.
     * post: mueve al enemigo aleatoriamente 1 casilla respetando los límites.
     */
    public void moverAleatorio(int maxX, int maxY, int maxZ) {
        boolean puedeMover;
        int dx;
        int dy;
        int dz;
        int nuevoX;
        int nuevoY;
        int nuevoZ;

        puedeMover = vivo;

        dx = 0;
        dy = 0;
        dz = 0;
        nuevoX = posX;
        nuevoY = posY;
        nuevoZ = posZ;

        if (puedeMover) {
            dx = random.nextInt(3) - 1;
            dy = random.nextInt(3) - 1;
            dz = random.nextInt(3) - 1;

            nuevoX = posX + dx;
            nuevoY = posY + dy;
            nuevoZ = posZ + dz;

            if (nuevoX >= 0 && nuevoX < maxX &&
                nuevoY >= 0 && nuevoY < maxY &&
                nuevoZ >= 0 && nuevoZ < maxZ) {

                setPosicion(nuevoX, nuevoY, nuevoZ);
                System.out.println(nombre + " (" + tipo + ") se movió a (" +
                                   nuevoX + "," + nuevoY + "," + nuevoZ + ").");
            }
        }
    }

    /**
     * pre: jugador != null y está vivo.
     * post: ataca al jugador restándole vida según fuerza.
     */
    public void atacarJugador(Personaje jugador) {
        boolean puedeAtacar;

        puedeAtacar = (vivo && jugador != null);

        if (puedeAtacar) {
            System.out.println(nombre + " (" + tipo + ") ataca a " + jugador.getNombre());
            jugador.recibirDanio(fuerza);
        }
    }

    /**
     * pre: danio >= 0.
     * post: reduce la vida del enemigo y si llega a 0 lo marca como muerto.
     */
    @Override
    public void recibirDanio(int danio) {
        boolean estabaVivo;

        estabaVivo = vivo;

        super.recibirDanio(danio);

        if (estabaVivo && vida <= 0) {
            vivo = false;
            System.out.println(nombre + " (" + tipo + ") ha sido derrotado.");
        }
    }

    /**
     * post: devuelve true si el enemigo está vivo.
     */
    public boolean estaVivo() {
        boolean resultado;
        resultado = vivo;
        return resultado;
    }

    /**
     * post: devuelve el tipo del enemigo.
     */
    public String getTipo() {
        String resultado;
        resultado = tipo;
        return resultado;
    }

    /**
     * post: devuelve una descripción textual del enemigo.
     */
    @Override
    public String toString() {
        String texto;
        String estado;

        estado = vivo ? " [VIVO]" : " [DERROTADO]";
        texto = "Enemigo " + tipo + " - " + super.toString() + estado;

        return texto;
    }
}
