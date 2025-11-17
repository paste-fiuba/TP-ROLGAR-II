package com.entidades;

/**
 * Clase abstracta que representa una entidad dentro del tablero.
 * Define atributos y comportamientos comunes de personajes y enemigos.
 */
public abstract class Entidad {

    protected String nombre;
    protected int vida;
    protected int posX;
    protected int posY;
    protected int posZ;
    protected int fuerza;
    protected int vision;
    protected double salud; 

    /**
     * pre: nombre != null, vida > 0, fuerza >= 0, vision >= 0, salud >= 0.
     * post: inicializa la entidad con los valores indicados.
     */
    public Entidad(String nombre, int vida, int posX, int posY, int posZ,
                   int fuerza, int vision, double salud) {
        this.nombre = nombre;
        this.vida = vida;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.fuerza = fuerza;
        this.vision = vision;
        this.salud = salud;
    }

    /**
     * pre: dx, dy, dz son desplazamientos válidos.
     * post: actualiza la posición sumando los valores recibidos.
     */
    public void mover(int dx, int dy, int dz) {
        int nuevoX;
        int nuevoY;
        int nuevoZ;

        nuevoX = posX + dx;
        nuevoY = posY + dy;
        nuevoZ = posZ + dz;

        posX = nuevoX;
        posY = nuevoY;
        posZ = nuevoZ;
    }

    /**
     * pre: danio >= 0.
     * post: disminuye la vida en la cantidad indicada (no menor a 0).
     */
    public void recibirDanio(int danio) {
        int nuevaVida;

        nuevaVida = vida - danio;

        if (nuevaVida < 0) {
            nuevaVida = 0;
        }

        vida = nuevaVida;
    }

    /**
     * pre: objetivo != null.
     * post: el objetivo recibe daño igual a la fuerza de esta entidad.
     */
    public void atacar(Entidad objetivo) {
        boolean puedeAtacar;

        puedeAtacar = (objetivo != null);

        if (puedeAtacar) {
            objetivo.recibirDanio(fuerza);
        }
    }

    /**
     * pre: salud >= 0.
     * post: aumenta la vida de la entidad según el porcentaje "salud".
     */
    public void recuperarEnergia() {
        int recupero;
        double porcentaje;

        porcentaje = salud / 100.0;
        recupero = (int) (vida * porcentaje);

        vida = vida + recupero;
    }

    // ================== GETTERS ===================

    /**
     * post: devuelve el nombre.
     */
    public String getNombre() {
        String resultado;
        resultado = nombre;
        return resultado;
    }

    /**
     * post: devuelve la vida actual.
     */
    public int getVida() {
        int resultado;
        resultado = vida;
        return resultado;
    }

    /**
     * post: devuelve X.
     */
    public int getPosX() {
        int resultado;
        resultado = posX;
        return resultado;
    }

    /**
     * post: devuelve Y.
     */
    public int getPosY() {
        int resultado;
        resultado = posY;
        return resultado;
    }

    /**
     * post: devuelve Z.
     */
    public int getPosZ() {
        int resultado;
        resultado = posZ;
        return resultado;
    }

    /**
     * post: devuelve la fuerza.
     */
    public int getFuerza() {
        int resultado;
        resultado = fuerza;
        return resultado;
    }

    /**
     * post: devuelve la visión.
     */
    public int getVision() {
        int resultado;
        resultado = vision;
        return resultado;
    }

    /**
     * post: devuelve el porcentaje de salud.
     */
    public double getSalud() {
        double resultado;
        resultado = salud;
        return resultado;
    }

    // ================== SETTERS ===================

    /**
     * pre: (x,y,z) válidos.
     * post: actualiza posición.
     */
    public void setPosicion(int x, int y, int z) {
        posX = x;
        posY = y;
        posZ = z;
    }

    /**
     * pre: vida >= 0.
     * post: establece vida.
     */
    public void setVida(int vida) {
        this.vida = vida;
    }

    /**
     * pre: fuerza >= 0.
     * post: actualiza la fuerza.
     */
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    /**
     * post: devuelve la descripción textual.
     */
    @Override
    public String toString() {
        String texto;

        texto = nombre + " [vida=" + vida +
                ", pos=(" + posX + "," + posY + "," + posZ + ")]";

        return texto;
    }
}