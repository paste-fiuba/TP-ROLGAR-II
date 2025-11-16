package com.logica;

import com.entidades.Enemigo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * TDA que encapsula la colección de enemigos y sus operaciones.
 */
public class EnemigoManager {

    private final List<Enemigo> enemigos;

    public EnemigoManager() {
        this.enemigos = new ArrayList<>();
    }

    public EnemigoManager(List<Enemigo> inicial) {
        this.enemigos = (inicial == null) ? new ArrayList<>() : new ArrayList<>(inicial);
    }

    public List<Enemigo> getEnemigos() {
        return this.enemigos;
    }

    public void add(Enemigo e) {
        if (e != null) enemigos.add(e);
    }

    public int countAlive() {
        int c = 0;
        for (Enemigo e : enemigos) if (e != null && e.estaVivo()) c++;
        return c;
    }

    public boolean allDead() {
        return countAlive() == 0;
    }

    public void markDead(Enemigo e) {
        if (e == null) return;
        // ensure the enemy's vida/vivo reflect death
        e.setVida(0);
        e.recibirDanio(0);
    }

    public void removeDead() {
        Iterator<Enemigo> it = enemigos.iterator();
        while (it.hasNext()) {
            Enemigo e = it.next();
            if (e == null || !e.estaVivo()) {
                it.remove();
            }
        }
    }
}
