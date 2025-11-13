package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;

public class CartaAtaqueDoble extends Carta {

    public CartaAtaqueDoble() {
        super("Ataque Doble", "Duplica el daño del próximo ataque.");
        this.imagen = cargarImagen("src/sprites/dobleAtaque.png");
    }

    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        if (usuario != null) {
            usuario.setAtaqueDobleActivo(true);
        }
    }
    
}