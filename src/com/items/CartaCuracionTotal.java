package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
public class CartaCuracionTotal extends Carta {
    public CartaCuracionTotal() {
        super("Curación Total", "Restaura la vida del personaje al 100%.");
        // --- LÍNEA ELIMINADA ---
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        usuario.setVida(100);
        System.out.println(usuario.getNombre() + " usó " + nombre + " y recuperó toda su vida!");
    }
}