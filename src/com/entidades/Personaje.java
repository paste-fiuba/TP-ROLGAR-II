
package com.entidades;

import com.items.Carta;
import com.items.Inventario;

public class Personaje extends Entidad {

    private Inventario inventario;

    // --- Lógica de Escudo (Modificada) ---
    private int vidaEscudo; // HP temporal del escudo
    
    // (Variables de otros buffs)
    private boolean invisible;
    private boolean evasionActiva;
    private double probabilidadEsquivaProximoAtaque;
    private int movimientosExtra;
    private boolean ataqueDobleActivo; 
    // Alianza actual (si pertenece a una)
    private Alianza alianza;

    public Personaje(String nombre, int vida, int posX, int posY, int posZ,
                     int fuerza, int vision, double salud) {
        super(nombre, vida, posX, posY, posZ, fuerza, vision, salud);
        this.inventario = new Inventario();
        this.vidaEscudo = 0; // Inicia con 0 de escudo
        this.invisible = false;
        this.evasionActiva = false;
        this.probabilidadEsquivaProximoAtaque = 0.0;
        this.movimientosExtra = 0;
        this.ataqueDobleActivo = false;
        this.alianza = null;
    }

    public void agregarCarta(Carta carta) { inventario.agregarCarta(carta); }
    public void eliminarCarta(int indice) { inventario.eliminarCarta(indice); }
    
    public void usarCarta(int indice, Entidad objetivo) {
        inventario.usarCarta(indice, this, objetivo);
    }
    
    public Inventario getInventario() {
        return inventario;
    }

    // --- Getters y Setters para Estados ---

    // Este método ahora AÑADE vida al escudo
    public void agregarVidaEscudo(int cantidad) {
        this.vidaEscudo += cantidad;
    }
    
    // ¡ESTE ES EL MÉTODO QUE FALTABA!
    public int getVidaEscudo() {
        return this.vidaEscudo;
    }
    
    // (Otros buffs)
    public void setInvisible(boolean estado) { this.invisible = estado; }
    public boolean isInvisible() { return this.invisible; }
    public void setEvasionActiva(boolean estado) { this.evasionActiva = estado; }
    public void setProbabilidadEsquiva(double prob) { this.probabilidadEsquivaProximoAtaque = prob; }
    public double getProbabilidadEsquiva() { return this.probabilidadEsquivaProximoAtaque; }
    public void setMovimientosExtra(int cantidad) { this.movimientosExtra = cantidad; }
    public int getMovimientosExtra() { return movimientosExtra; }
    public void setAtaqueDobleActivo(boolean estado) { this.ataqueDobleActivo = estado; }
    public boolean isAtaqueDobleActivo() { return this.ataqueDobleActivo; }

    public Alianza getAlianza() { return this.alianza; }
    public void setAlianza(Alianza a) { this.alianza = a; }

    public boolean estaAliadoCon(Personaje otro) {
        if (this.alianza == null || otro == null) return false;
        return this.alianza.pertenece(otro);
    }

    /**
     * El daño ahora golpea primero al escudo.
     */
    @Override
    public void recibirDanio(int danio) {
        if (invisible) {
            // Invisibilidad evita el daño garantizado y se consume
            invisible = false;
            return;
        }

        // Si hay una probabilidad de esquivar el próximo ataque, evaluar y consumirla
        if (probabilidadEsquivaProximoAtaque > 0.0) {
            double roll = Math.random();
            boolean esquivo = roll < probabilidadEsquivaProximoAtaque;
            // consumir la probabilidad en cualquier caso
            probabilidadEsquivaProximoAtaque = 0.0;
            if (esquivo) {
                return;
            }
            // si falla la esquiva, continuar al cálculo de daño
        }
        if (evasionActiva) {
            evasionActiva = false;
            return;
        }
        
        // --- Nueva Lógica de Escudo ---
        if (this.vidaEscudo > 0) {
            int danioAbsorbido = Math.min(danio, this.vidaEscudo);
            this.vidaEscudo -= danioAbsorbido;
            int danioRestante = danio - danioAbsorbido;

            if (danioRestante <= 0) {
                return;
            }
            
            super.recibirDanio(danioRestante); 
        } else {
            super.recibirDanio(danio); 
        }
    }

    @Override
    public String toString() {
        return "Jugador " + nombre;
    }
}