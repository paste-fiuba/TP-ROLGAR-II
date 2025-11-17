package com.logica;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import java.util.List;

/**
 * pre: enemigos != null.
 * post: gestiona la IA de enemigos.
 */
public class AdministradorDeEnemigos {

    private List<Enemigo> enemigos;
    private ControladorJuego controlador;

    /**
     * pre: enemigos != null y controlador != null.
     * post: inicializa el administrador.
     */
    public AdministradorDeEnemigos(List<Enemigo> enemigos, ControladorJuego controlador) {
        this.enemigos = enemigos;
        this.controlador = controlador;
    }

    /**
     * pre: jugadores != null.
     * post: procesa los turnos de todos los enemigos vivos.
     */
    public void procesarTurnos(List<Personaje> jugadores) {

        boolean datosValidos;
        int i;
        int n;
        Enemigo enemigo;
        boolean enemigoVivo;
        Personaje objetivo;
        boolean objetivoValido;
        boolean igualZ;
        int dist;
        boolean distanciaCercana;
        boolean combateIniciado;

        datosValidos = (enemigos != null && jugadores != null && !jugadores.isEmpty());
        i = 0;
        n = 0;
        enemigo = null;
        enemigoVivo = false;
        objetivo = null;
        objetivoValido = false;
        igualZ = false;
        dist = 0;
        distanciaCercana = false;
        combateIniciado = false;

        if (datosValidos) {

            n = enemigos.size();

            while (i < n && !combateIniciado) {

                enemigo = enemigos.get(i);
                enemigoVivo = (enemigo != null && enemigo.estaVivo());

                if (enemigoVivo) {

                    objetivo = encontrarJugadorMasCercanoA(enemigo, jugadores);
                    objetivoValido = (objetivo != null);

                    if (objetivoValido) {

                        igualZ = (enemigo.getPosZ() == objetivo.getPosZ());

                        if (igualZ) {
                            dist = Math.abs(enemigo.getPosX() - objetivo.getPosX()) +
                                   Math.abs(enemigo.getPosY() - objetivo.getPosY());

                            distanciaCercana = (dist <= 1);

                            if (distanciaCercana) {
                                controlador.iniciarCombate(objetivo, enemigo);
                                combateIniciado = true;
                            }
                        }
                    }
                }

                i++;
            }
        }
    }

    /**
     * pre: enemigo != null, jugadores != null.
     * post: devuelve el jugador vivo más cercano o null.
     */
    private Personaje encontrarJugadorMasCercanoA(Enemigo enemigo, List<Personaje> jugadores) {

        boolean datosValidos;
        Personaje masCercano;
        int menor;
        int i;
        int n;
        Personaje p;
        boolean jugadorVivo;
        boolean mismoZ;
        int dist;
        boolean mejorQueActual;

        datosValidos = (enemigo != null && jugadores != null && !jugadores.isEmpty());
        masCercano = null;
        menor = Integer.MAX_VALUE;
        i = 0;
        n = 0;
        p = null;
        jugadorVivo = false;
        mismoZ = false;
        dist = 0;
        mejorQueActual = false;

        if (datosValidos) {

            n = jugadores.size();

            while (i < n) {

                p = jugadores.get(i);

                if (p != null) {

                    jugadorVivo = (p.getVida() > 0);
                    mismoZ = (p.getPosZ() == enemigo.getPosZ());

                    if (jugadorVivo && mismoZ) {

                        dist = Math.abs(enemigo.getPosX() - p.getPosX()) +
                               Math.abs(enemigo.getPosY() - p.getPosY());

                        mejorQueActual = (dist < menor);

                        if (mejorQueActual) {
                            menor = dist;
                            masCercano = p;
                        }
                    }
                }

                i++;
            }
        }

        return masCercano;
    }
}