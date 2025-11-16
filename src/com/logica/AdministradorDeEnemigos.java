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
     * (Anteriormente en AdministradorDeJuego.procesarTurnoEnemigos)
     * * @param jugadores Lista de jugadores vivos para buscar objetivos.
     */
    public void procesarTurnos(List<Personaje> jugadores) {
        if (enemigos == null || jugadores == null || jugadores.isEmpty()) return;

        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;

            Personaje objetivo = encontrarJugadorMasCercanoA(enemigo, jugadores);
            if (objetivo == null) continue; // No hay jugadores vivos en el mismo piso
            if (enemigo.getPosZ() != objetivo.getPosZ()) continue; // En diferente piso

            // Lógica de IA: si está adyacente, ataca.
            int dist = Math.abs(enemigo.getPosX() - objetivo.getPosX()) + Math.abs(enemigo.getPosY() - objetivo.getPosY());
            if (dist <= 1) {
                // ¡Importante! El jugador (que es el objetivo) es el primer parámetro
                controlador.iniciarCombate(objetivo, enemigo);
                
                // Rompemos el bucle para que solo un enemigo inicie combate por turno
                // Esto evita que 4 enemigos ataquen a la vez y saturen al jugador
                break; 
            }
            
            // (Aquí se podría agregar lógica de movimiento aleatorio si no ataca)
            // Por ejemplo:
            // else if (dist < enemigo.getVision()) {
            //     // Moverse hacia el jugador (lógica más compleja)
            // } else {
            //     // Mover aleatorio (requeriría el tablero)
            // }
        }
    }

    /**
     * Encuentra al jugador vivo más cercano a un enemigo específico
     * QUE ESTÉ EN EL MISMO PISO.
     * (Anteriormente en AdministradorDeJuego)
     */
    private Personaje encontrarJugadorMasCercanoA(Enemigo enemigo, List<Personaje> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) return null;
        Personaje masCercano = null;
        int menor = Integer.MAX_VALUE;
        for (Personaje p : jugadores) {
            if (p.getVida() <= 0) continue;
            
            // Solo buscar objetivos en el mismo nivel
            if (p.getPosZ() != enemigo.getPosZ()) continue; 
            
            int dist = Math.abs(enemigo.getPosX() - p.getPosX()) + Math.abs(enemigo.getPosY() - p.getPosY());
            if (dist < menor) {
                menor = dist;
                masCercano = p;
            }
        }
        return masCercano; // Puede ser null si no hay jugadores en el mismo piso
    }
}