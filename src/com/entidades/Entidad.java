package com.entidades;

/**
 * Clase abstracta que representa una entidad dentro del tablero.
 * Define los atributos y comportamientos comunes de personajes y enemigos.
 */
public abstract class Entidad {

    // ==== Atributos comunes ====
    protected String nombre;
    protected int vida;
    protected int posX;
    protected int posY;
    protected int posZ;
    protected int fuerza;
    protected int vision;
    protected double salud; // Porcentaje de recuperación por turno

    /**
     * pre: nombre no es null, vida > 0, fuerza >= 0, vision >= 0, salud >= 0.
     *      Las coordenadas (posX, posY, posZ) son válidas dentro del tablero.
     * post: crea una entidad inicializando todos los atributos con los valores indicados.
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
     * pre: dx, dy y dz representan desplazamientos válidos (por ejemplo, -1, 0 o 1)
     *      y el movimiento resultante debe mantenerse dentro del tablero.
     * post: actualiza la posición de la entidad sumando (dx, dy, dz) a su posición actual.
     */
    public void mover(int dx, int dy, int dz) {
        this.posX += dx;
        this.posY += dy;
        this.posZ += dz;
    }

    /**
     * pre: danio >= 0.
     * post: disminuye la vida de la entidad en "danio" unidades.
     *       si la vida es menor a 0, se ajusta a 0.
     */
    public void recibirDanio(int danio) {
        this.vida -= danio;
        if (vida < 0) vida = 0;
    }

    /**
     * pre: objetivo no es null y se encuentra vivo.
     * post: el objetivo pierde una cantidad de vida igual a la fuerza de esta entidad.
     */
    public void atacar(Entidad objetivo) {
        if (objetivo != null) {
            objetivo.recibirDanio(fuerza);
        }
    }

    /**
     * pre: salud > 0.
     * post: aumenta la vida de la entidad en el porcentaje indicado por "salud".
     */
    public void recuperarEnergia() {
        int recupero = (int) (vida * (salud / 100));
        this.vida += recupero;
    }

    // ==== Getters y Setters ====

    /**
     * post: devuelve el nombre de la entidad.
     */
    public String getNombre() { return nombre; }

    /**
     * post: devuelve la vida actual de la entidad.
     */
    public int getVida() { return vida; }

    /**
     * post: devuelve la coordenada X actual.
     */
    public int getPosX() { return posX; }

    /**
     * post: devuelve la coordenada Y actual.
     */
    public int getPosY() { return posY; }

    /**
     * post: devuelve la coordenada Z actual.
     */
    public int getPosZ() { return posZ; }

    /**
     * post: devuelve la fuerza de ataque de la entidad.
     */
    public int getFuerza() { return fuerza; }

    /**
     * post: devuelve el rango de visión de la entidad.
     */
    public int getVision() { return vision; }

    /**
     * post: devuelve el porcentaje de regeneración de vida por turno.
     */
    public double getSalud() { return salud; }

    /**
     * pre: las coordenadas (x, y, z) son válidas dentro del tablero.
     * post: actualiza la posición de la entidad con las coordenadas indicadas.
     */
    public void setPosicion(int x, int y, int z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    /**
     * pre: vida >= 0.
     * post: establece la vida actual de la entidad al valor indicado.
     */
    public void setVida(int vida) { this.vida = vida; }

    /**
     * post: devuelve una representación textual de la entidad
     *       con su nombre, vida y posición actual.
     */
    @Override
    public String toString() {
        return nombre + " [vida=" + vida +
               ", pos=(" + posX + "," + posY + "," + posZ + ")]";
    }
}
