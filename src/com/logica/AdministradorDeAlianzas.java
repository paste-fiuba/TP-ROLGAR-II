package com.logica;

import com.entidades.Alianza;
import com.entidades.Personaje;
import com.items.Carta;
import com.tablero.Tablero;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * pre: -
 * post: administra alianzas, propuestas y transferencias de cartas.
 */
public class AdministradorDeAlianzas {

    private List<Alianza> alianzas;
    private Map<Personaje, Personaje> propuestas;
    private Random random;
    private Consumer<String> logger;
    private static final double PROB_RUPTURA_ALIANZA = 0.15;

    /**
     * pre: logger != null.
     * post: inicializa el administrador.
     */
    public AdministradorDeAlianzas(Consumer<String> logger) {
        this.alianzas = new ArrayList<>();
        this.propuestas = new HashMap<>();
        this.random = new Random();
        this.logger = logger;
    }

    /**
     * pre: target != null.
     * post: devuelve el proponente asociado a target, o null.
     */
    public Personaje getPropuestaPara(Personaje target) {

        Personaje resultado;

        resultado = propuestas.get(target);

        return resultado;
    }

    /**
     * pre: proponente != null, objetivo != null.
     * post: registra una propuesta si es válida.
     */
    public void proponerAlianza(Personaje proponente, Personaje objetivo, Tablero tablero) {

        boolean datosValidos;
        boolean adyacentes;
        boolean yaAliados;
        Alianza alProponente;

        datosValidos = (proponente != null && objetivo != null && tablero != null);
        adyacentes = false;
        yaAliados = false;
        alProponente = null;

        if (datosValidos) {

            adyacentes = sonAdyacentes(proponente, objetivo, tablero);
            alProponente = proponente.getAlianza();
            yaAliados = (alProponente != null && proponente.estaAliadoCon(objetivo));

            if (!adyacentes) {
                logger.accept("No estás lo suficientemente cerca para proponer alianza.");
            } else if (yaAliados) {
                logger.accept("Ya estás aliado con " + objetivo.getNombre());
            } else {
                propuestas.put(objetivo, proponente);
                logger.accept(proponente.getNombre() + " propone alianza a " + objetivo.getNombre());
            }
        }
    }

    /**
     * pre: target != null.
     * post: acepta una alianza pendiente si existe.
     */
    public void aceptarPropuesta(Personaje target) {

        boolean valido;
        Personaje proponente;
        Alianza alProponente;
        Alianza alTarget;
        Alianza nueva;
        List<Personaje> miembros;
        int i;
        int n;
        Personaje p;

        valido = (target != null);

        proponente = null;
        alProponente = null;
        alTarget = null;
        nueva = null;
        miembros = null;
        i = 0;
        n = 0;
        p = null;

        if (valido) {

            proponente = propuestas.remove(target);

            if (proponente != null) {

                alProponente = proponente.getAlianza();
                alTarget = target.getAlianza();

                if (alProponente == null && alTarget == null) {

                    nueva = new Alianza("Alianza " + proponente.getNombre() + "-" + target.getNombre());
                    nueva.agregarMiembro(proponente);
                    nueva.agregarMiembro(target);
                    alianzas.add(nueva);

                } else if (alProponente != null && alTarget == null) {

                    alProponente.agregarMiembro(target);

                } else if (alProponente == null && alTarget != null) {

                    alTarget.agregarMiembro(proponente);

                } else if (alProponente != alTarget) {

                    miembros = alTarget.getMiembros();
                    i = 0;
                    n = miembros.size();

                    while (i < n) {
                        p = miembros.get(i);
                        alProponente.agregarMiembro(p);
                        i++;
                    }

                    alianzas.remove(alTarget);
                }

                logger.accept(target.getNombre() + " aceptó la alianza con " + proponente.getNombre());

            } else {
                logger.accept("No hay propuestas para aceptar.");
            }
        }
    }

    /**
     * pre: target != null.
     * post: rechaza una propuesta si existe.
     */
    public void rechazarPropuesta(Personaje target) {

        boolean valido;
        Personaje proponente;

        valido = (target != null);
        proponente = null;

        if (valido) {

            proponente = propuestas.remove(target);

            if (proponente != null) {
                logger.accept(target.getNombre() + " rechazó la alianza de " + proponente.getNombre());
            } else {
                logger.accept("No hay propuestas para rechazar.");
            }
        }
    }

    /**
     * pre: from != null, to != null.
     * post: transfiere una carta si es posible.
     */
    public boolean transferirCarta(Personaje from, Personaje to, int slotIndex, Tablero tablero) {

        boolean datosValidos;
        boolean adyacentes;
        boolean aliados;
        Carta carta;
        boolean toTieneLugar;
        boolean exito;

        datosValidos = (from != null && to != null && tablero != null);
        adyacentes = false;
        aliados = false;
        carta = null;
        toTieneLugar = false;
        exito = false;

        if (datosValidos) {

            adyacentes = sonAdyacentes(from, to, tablero);
            aliados = (from.getAlianza() != null && from.estaAliadoCon(to));
            carta = from.getInventario().getCarta(slotIndex);
            toTieneLugar = (to.getInventario().cantidadDeCartas() < 10);

            if (adyacentes && aliados && carta != null && toTieneLugar) {

                to.agregarCarta(carta);
                from.eliminarCarta(slotIndex);
                logger.accept(from.getNombre() + " transfirió " + carta.getNombre() + " a " + to.getNombre());
                exito = true;
            }
        }

        return exito;
    }

    /**
     * pre: -
     * post: procesa rupturas aleatorias en alianzas.
     */
    public void procesarRupturaAlianzas() {

        boolean hayAlianzas;
        List<Alianza> copia;
        int i;
        int n;
        Alianza al;
        boolean rompe;
        List<Personaje> miembros;
        boolean sinMiembros;
        int idx;
        Personaje expulsado;
        List<Personaje> despues;
        int j;
        int m;
        Personaje p;

        hayAlianzas = (alianzas != null && !alianzas.isEmpty());
        copia = null;
        i = 0;
        n = 0;
        al = null;
        rompe = false;
        miembros = null;
        sinMiembros = false;
        idx = 0;
        expulsado = null;
        despues = null;
        j = 0;
        m = 0;
        p = null;

        if (hayAlianzas) {

            copia = new ArrayList<>(alianzas);
            i = 0;
            n = copia.size();

            while (i < n) {

                al = copia.get(i);

                if (al != null) {

                    rompe = (random.nextDouble() < PROB_RUPTURA_ALIANZA);

                    if (rompe) {

                        miembros = al.getMiembros();
                        sinMiembros = (miembros == null || miembros.isEmpty());

                        if (sinMiembros) {
                            alianzas.remove(al);
                        } else {

                            idx = random.nextInt(miembros.size());
                            expulsado = miembros.get(idx);

                            if (expulsado != null) {
                                al.eliminarMiembro(expulsado);
                                logger.accept("La alianza '" + al.toString() + "' se rompió: " + expulsado.getNombre() + " fue expulsado.");
                            }

                            despues = al.getMiembros();

                            if (despues.size() <= 1) {

                                j = 0;
                                m = despues.size();

                                while (j < m) {
                                    p = despues.get(j);
                                    if (p != null) {
                                        p.setAlianza(null);
                                    }
                                    j++;
                                }

                                alianzas.remove(al);
                                logger.accept("La alianza se disolvió por falta de miembros.");
                            }
                        }
                    }
                }

                i++;
            }
        }
    }

    /**
     * pre: a != null, b != null.
     * post: devuelve true si están a distancia 1 en el mismo nivel.
     */
    public boolean sonAdyacentes(Personaje a, Personaje b, Tablero tablero) {

        boolean datosValidos;
        int zA;
        int zB;
        boolean mismoZ;
        int dist;
        boolean resultado;

        datosValidos = (a != null && b != null && tablero != null);
        zA = 0;
        zB = 0;
        mismoZ = false;
        dist = 0;
        resultado = false;

        if (datosValidos) {

            zA = a.getPosZ();
            zB = b.getPosZ();
            mismoZ = (zA == zB);

            if (mismoZ) {
                dist = Math.abs(a.getPosX() - b.getPosX()) +
                       Math.abs(a.getPosY() - b.getPosY());
                resultado = (dist == 1);
            }
        }

        return resultado;
    }
}