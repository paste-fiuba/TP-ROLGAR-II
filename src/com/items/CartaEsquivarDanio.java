package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
import java.util.Random;
public class CartaEsquivarDanio extends Carta {
    private double probabilidad;
    private Random random = new Random();
    public CartaEsquivarDanio(double probabilidad) {
        super("Evasión", "Otorga un " + (int)(probabilidad * 100) +
              "% de probabilidad de esquivar el próximo ataque.");
        this.probabilidad = probabilidad;
        // --- LÍNEA ELIMINADA ---
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        usuario.setProbabilidadEsquiva(probabilidad);
        System.out.println(usuario.getNombre() + " usó " + nombre + " y tiene " + (int)(probabilidad * 100) + "% de chance de esquivar el próximo ataque.");
    }
}