package com.tablero;

/**
 * Clase auxiliar para almacenar una coordenada (X, Y, Z).
 */

public class Coordenada {

    private int x;
    private int y;
    private int z;

    /**
     * Constructor para crear un objeto Coordenada.
     * @param x Posición X
     * @param y Posición Y
     * @param z Posición Z
     */
    public Coordenada(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // --- Getters y Setters ---

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordenada that = (Coordenada) obj;
        return x == that.x && y == that.y && z == that.z;
    }
}
