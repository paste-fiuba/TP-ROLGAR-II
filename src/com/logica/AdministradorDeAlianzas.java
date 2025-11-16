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
import java.util.function.Consumer; // Para poder "loguear"

/**
 * TDA para gestionar la lógica de alianzas, propuestas y rupturas.
 */
public class AdministradorDeAlianzas {

    private List<Alianza> alianzas;
    private Map<Personaje, Personaje> propuestas;
    private Random random;
    private Consumer<String> logger; // Una referencia al método logBatalla

    private static final double PROB_RUPTURA_ALIANZA = 0.15;

    public AdministradorDeAlianzas(Consumer<String> logger) {
        this.alianzas = new ArrayList<>();
        this.propuestas = new HashMap<>();
        this.random = new Random();
        this.logger = logger;
    }

    /**
     * Devuelve el personaje que le propuso una alianza al 'target'.
     */
    public Personaje getPropuestaPara(Personaje target) {
        return propuestas.get(target);
    }

    /**
     * Un jugador 'proponente' le ofrece una alianza a 'objetivo'.
     */
    public void proponerAlianza(Personaje proponente, Personaje objetivo, Tablero tablero) {
        if (proponente == null || objetivo == null) return;
        if (!sonAdyacentes(proponente, objetivo, tablero)) {
            logger.accept("No estás lo suficientemente cerca para proponer alianza.");
            return;
        }
        if (proponente.getAlianza() != null && proponente.estaAliadoCon(objetivo)) {
            logger.accept("Ya estás aliado con " + objetivo.getNombre());
            return;
        }
        propuestas.put(objetivo, proponente);
        logger.accept(proponente.getNombre() + " propone alianza a " + objetivo.getNombre());
    }

    /**
     * El 'target' acepta la propuesta pendiente.
     */
    public void aceptarPropuesta(Personaje target) {
        if (target == null) return;
        Personaje proponente = propuestas.remove(target);
        if (proponente == null) {
            logger.accept("No hay propuestas para aceptar.");
            return;
        }
        
        // Lógica para unir o crear alianzas
        Alianza alianzaProponente = proponente.getAlianza();
        Alianza alianzaTarget = target.getAlianza();

        if (alianzaProponente == null && alianzaTarget == null) {
            // Ninguno tiene alianza: crear una nueva
            Alianza nueva = new Alianza("Alianza " + proponente.getNombre() + "-" + target.getNombre());
            nueva.agregarMiembro(proponente);
            nueva.agregarMiembro(target);
            alianzas.add(nueva);
        } else if (alianzaProponente != null && alianzaTarget == null) {
            // El proponente ya tiene alianza: unir al target
            alianzaProponente.agregarMiembro(target);
        } else if (alianzaProponente == null && alianzaTarget != null) {
            // El target ya tiene alianza: unir al proponente
            alianzaTarget.agregarMiembro(proponente);
        } else if (alianzaProponente != alianzaTarget) {
            // Ambos tienen alianzas distintas: fusionarlas
            for (Personaje p : alianzaTarget.getMiembros()) {
                alianzaProponente.agregarMiembro(p);
            }
            // Eliminar la alianza vacía
            alianzas.remove(alianzaTarget);
        }
        
        logger.accept(target.getNombre() + " aceptó la alianza con " + proponente.getNombre());
    }

    /**
     * El 'target' rechaza la propuesta pendiente.
     */
    public void rechazarPropuesta(Personaje target) {
        if (target == null) return;
        Personaje proponente = propuestas.remove(target);
        if (proponente != null) {
            logger.accept(target.getNombre() + " rechazó la alianza de " + proponente.getNombre());
        } else {
            logger.accept("No hay propuestas para rechazar.");
        }
    }

    /**
     * Transfiere una carta de 'from' a 'to'.
     */
    public boolean transferirCarta(Personaje from, Personaje to, int slotIndex, Tablero tablero) {
        if (from == null || to == null) return false;
        if (!sonAdyacentes(from, to, tablero)) return false;
        if (from.getAlianza() == null || !from.estaAliadoCon(to)) return false;
        Carta carta = from.getInventario().getCarta(slotIndex);
        if (carta == null) return false;
        if (to.getInventario().cantidadDeCartas() >= 10) return false;
        to.agregarCarta(carta);
        from.eliminarCarta(slotIndex);
        logger.accept(from.getNombre() + " transfirió " + carta.getNombre() + " a " + to.getNombre());
        return true;
    }

    /**
     * Procesa la probabilidad de que las alianzas se rompan.
     */
    public void procesarRupturaAlianzas() {
        if (alianzas == null || alianzas.isEmpty()) return;

        java.util.List<Alianza> copia = new java.util.ArrayList<>(alianzas);
        for (Alianza al : copia) {
            if (al == null) continue;
            if (random.nextDouble() < PROB_RUPTURA_ALIANZA) {
                java.util.List<Personaje> miembros = al.getMiembros();
                if (miembros == null || miembros.isEmpty()) {
                    alianzas.remove(al);
                    continue;
                }
                int idx = random.nextInt(miembros.size());
                Personaje expulsado = miembros.get(idx);
                if (expulsado != null) {
                    al.eliminarMiembro(expulsado);
                    logger.accept("La alianza '" + al.toString() + "' se rompió: " + expulsado.getNombre() + " fue expulsado.");
                }
                java.util.List<Personaje> despues = al.getMiembros();
                if (despues.size() <= 1) {
                    for (Personaje p : despues) {
                        if (p != null) p.setAlianza(null);
                    }
                    alianzas.remove(al);
                    logger.accept("La alianza se disolvió por falta de miembros.");
                }
            }
        }
    }

    /**
     * Verifica si dos personajes son adyacentes (no diagonal).
     */
    public boolean sonAdyacentes(Personaje a, Personaje b, Tablero tablero) {
        if (a == null || b == null || tablero == null) return false;
        if (a.getPosZ() != b.getPosZ()) return false;
        // Chequeo de distancia Manhattan
        int dist = Math.abs(a.getPosX() - b.getPosX()) + Math.abs(a.getPosY() - b.getPosY());
        return dist == 1;
    }
}