package com.tablero;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el tablero de juego (X, Y, Z).
 *
 * Z = Niveles
 * Y = Filas
 * X = Columnas
 */
public class Tablero {

    private List<List<List<Casillero>>> niveles;

    private final int dimensionX;
    private final int dimensionY;
    private final int dimensionZ;

    /**
     * Construye un nuevo tablero con las dimensiones dadas.
     *
     * @param dimX Tamaño de la dimensión X (columnas)
     * @param dimY Tamaño de la dimensión Y (filas)
     * @param dimZ Tamaño de la dimensión Z (niveles)
     */
    public Tablero(int dimX, int dimY, int dimZ) {
    	
        if (dimX <= 0 || dimY <= 0 || dimZ <= 0) {
            throw new IllegalArgumentException("Las dimensiones deben ser positivas.");
        }
        
        this.dimensionX = dimX;
        this.dimensionY = dimY;
        this.dimensionZ = dimZ;

        
        this.niveles = new ArrayList<>(dimZ); 

        for (int z = 0; z < dimZ; z++) {
            List<List<Casillero>> planoY = new ArrayList<>(dimY); 
            for (int y = 0; y < dimY; y++) {
                List<Casillero> filaX = new ArrayList<>(dimX); 
                for (int x = 0; x < dimX; x++) {
                    filaX.add(new Casillero());
                }
                planoY.add(filaX); 
            }
            this.niveles.add(planoY);
        }
    }

    /**
     * Obtiene el Casillero en una coordenada específica.
     *
     * @param x Coordenada X
     * @param y Coordenada Y
     * @param z Coordenada Z
     * @return El objeto Casillero en esa posición, o null si está fuera de límites.
     */
    public Casillero getCasillero(int x, int y, int z) {
        if (esCoordenadaValida(x, y, z)) {
            return this.niveles.get(z).get(y).get(x);
        }
        return null;
    }

    /**
     * Verifica si una coordenada está dentro de
     * los límites del tablero.
     */
    public boolean esCoordenadaValida(int x, int y, int z) {
        return z >= 0 && z < this.dimensionZ &&
               y >= 0 && y < this.dimensionY &&
               x >= 0 && x < this.dimensionX;
    }

    /**
     * Obtiene el Casillero usando un objeto Coordenada.
     *
     * @param coord El objeto Coordenada.
     * @return El objeto Casillero en esa posición, o null si está fuera de límites.
     */
    public Casillero getCasillero(Coordenada coord) {
        if (coord == null) {
            return null;
        }
        // Reutilizamos el método que ya teníamos
        return getCasillero(coord.getX(), coord.getY(), coord.getZ());
    }

    /**
     * Verifica si una Coordenada está dentro de los límites del tablero.
     *
     * @param coord El objeto Coordenada.
     * @return true si la coordenada es válida, false en caso contrario.
     */
    public boolean esCoordenadaValida(Coordenada coord) {
        if (coord == null) {
            return false;
        }
        // Reutilizamos el método que ya teníamos
        return esCoordenadaValida(coord.getX(), coord.getY(), coord.getZ());
    }

    public int getDimensionX() {
        return dimensionX;
    }

    public int getDimensionY() {
        return dimensionY;
    }

    public int getDimensionZ() {
        return dimensionZ;
    }
}
