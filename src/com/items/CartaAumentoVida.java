package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
public class CartaAumentoVida extends Carta {
    private int incremento;
    public CartaAumentoVida(int incremento) {
        super("Aumento de Vida", "Aumenta la vida del personaje en " + incremento + " puntos.");
        this.incremento = incremento;
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        if (usuario != null) {
            usuario.setVida(usuario.getVida() + incremento);
            System.out.println(usuario.getNombre() + " usó " + nombre + " y aumentó su vida en " + incremento + ".");
        }
    }
}