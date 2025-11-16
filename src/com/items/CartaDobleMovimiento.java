package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
public class CartaDobleMovimiento extends Carta {
    public CartaDobleMovimiento() {
        super("Doble Movimiento", "Permite moverse dos veces en el siguiente turno.");
        // --- LÍNEA ELIMINADA ---
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        usuario.setMovimientosExtra(1);
        System.out.println(usuario.getNombre() + " podrá moverse dos veces en el próximo turno.");
    }
}