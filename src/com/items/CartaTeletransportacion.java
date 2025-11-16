package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
public class CartaTeletransportacion extends Carta {
    private int nuevaX, nuevaY, nuevaZ;
    public CartaTeletransportacion(int x, int y, int z) {
        super("Teletransportación", "Teletransporta al jugador a (" + x + "," + y + "," + z + ").");
        this.nuevaX = x;
        this.nuevaY = y;
        this.nuevaZ = z;
        // --- LÍNEA ELIMINADA ---
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        usuario.setPosicion(nuevaX, nuevaY, nuevaZ);
        System.out.println(usuario.getNombre() + " se teletransportó a (" + nuevaX + "," + nuevaY + "," + nuevaZ + ").");
    }
}