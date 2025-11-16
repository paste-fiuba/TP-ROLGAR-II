package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import java.util.List;

/**
 * TDA para gestionar la lógica de IA y el comportamiento de los enemigos.
 */
public class AdministradorDeEnemigos {

    private List<Enemigo> enemigos;
    private ControladorJuego controlador; // Para iniciar combate

    public AdministradorDeEnemigos(List<Enemigo> enemigos, ControladorJuego controlador) {
        this.enemigos = enemigos;
        this.controlador = controlador;
    }

    /**
     * Procesa el turno de todos los enemigos vivos.
     */
    public void procesarTurnos(List<Personaje> jugadores) {
        if (enemigos == null || jugadores == null || jugadores.isEmpty()) return;

        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;

            Personaje objetivo = encontrarJugadorMasCercanoA(enemigo, jugadores);
            if (objetivo == null) continue; 
            if (enemigo.getPosZ() != objetivo.getPosZ()) continue; 

            int dist = Math.abs(enemigo.getPosX() - objetivo.getPosX()) + Math.abs(enemigo.getPosY() - objetivo.getPosY());
            if (dist <= 1) {
                controlador.iniciarCombate(objetivo, enemigo);
         
                break; 
            }
            
        }
    }


    private Personaje encontrarJugadorMasCercanoA(Enemigo enemigo, List<Personaje> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) return null;
        Personaje masCercano = null;
        int menor = Integer.MAX_VALUE;
        for (Personaje p : jugadores) {
            if (p.getVida() <= 0) continue;
            
            if (p.getPosZ() != enemigo.getPosZ()) continue; 
            
            int dist = Math.abs(enemigo.getPosX() - p.getPosX()) + Math.abs(enemigo.getPosY() - p.getPosY());
            if (dist < menor) {
                menor = dist;
                masCercano = p;
            }
        }
        return masCercano; 
    }
}