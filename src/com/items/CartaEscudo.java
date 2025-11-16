package com.items; 
import com.entidades.Entidad;
import com.entidades.Personaje;
public class CartaEscudo extends Carta {
    private static final int VALOR_ESCUDO = 30; 
    public CartaEscudo() {
        super("Escudo", "Otorga " + VALOR_ESCUDO + " de vida temporal.");
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        if (usuario != null) {
            usuario.agregarVidaEscudo(VALOR_ESCUDO);
        }
    }
}