package com.entidades;

import com.items.Carta;
import com.items.Inventario;

public class Personaje extends Entidad {

    private Inventario inventario;
    private int vidaEscudo;

    private boolean invisible;
    private boolean evasionActiva;
    private double probabilidadEsquivaProximoAtaque;
    private int movimientosExtra;
    private boolean ataqueDobleActivo;
    private Alianza alianza;

    public Personaje(String nombre, int vida, int posX, int posY, int posZ,
                     int fuerza, int vision, double salud) {

        super(nombre, vida, posX, posY, posZ, fuerza, vision, salud);
        this.inventario = new Inventario();
        this.vidaEscudo = 0;
        this.invisible = false;
        this.evasionActiva = false;
        this.probabilidadEsquivaProximoAtaque = 0.0;
        this.movimientosExtra = 0;
        this.ataqueDobleActivo = false;
        this.alianza = null;
    }

    // ================== INVENTARIO ==================

    public void agregarCarta(Carta carta) {
        inventario.agregarCarta(carta);
    }

    public void eliminarCarta(int indice) {
        inventario.eliminarCarta(indice);
    }

    public void usarCarta(int indice, Entidad objetivo) {
        inventario.usarCarta(indice, this, objetivo);
    }

    public Inventario getInventario() {
        Inventario resultado;
        resultado = inventario;
        return resultado;
    }

    // ================== ATRIBUTOS ESPECIALES ==================

    public void agregarVidaEscudo(int cantidad) {
        vidaEscudo = vidaEscudo + cantidad;
    }

    public int getVidaEscudo() {
        int resultado;
        resultado = vidaEscudo;
        return resultado;
    }

    public void setInvisible(boolean estado) {
        invisible = estado;
    }

    public boolean isInvisible() {
        boolean resultado;
        resultado = invisible;
        return resultado;
    }

    public void setEvasionActiva(boolean estado) {
        evasionActiva = estado;
    }

    public void setProbabilidadEsquiva(double prob) {
        probabilidadEsquivaProximoAtaque = prob;
    }

    public double getProbabilidadEsquiva() {
        double resultado;
        resultado = probabilidadEsquivaProximoAtaque;
        return resultado;
    }

    public void setMovimientosExtra(int cantidad) {
        movimientosExtra = cantidad;
    }

    public int getMovimientosExtra() {
        int resultado;
        resultado = movimientosExtra;
        return resultado;
    }

    public void setAtaqueDobleActivo(boolean estado) {
        ataqueDobleActivo = estado;
    }

    public boolean isAtaqueDobleActivo() {
        boolean resultado;
        resultado = ataqueDobleActivo;
        return resultado;
    }

    public Alianza getAlianza() {
        Alianza resultado;
        resultado = alianza;
        return resultado;
    }

    public void setAlianza(Alianza a) {
        alianza = a;
    }

    public boolean estaAliadoCon(Personaje otro) {
        boolean resultado;
        boolean puedeVerificar;

        resultado = false;
        puedeVerificar = (alianza != null && otro != null);

        if (puedeVerificar) {
            resultado = alianza.pertenece(otro);
        }

        return resultado;
    }

    // ================== RECIBIR DAÑO ==================

    /**
     * pre: danio >= 0.
     * post: aplica efectos especiales de defensa; el daño pasa primero por el escudo.
     */
    @Override
    public void recibirDanio(int danio) {
        boolean bloqueadoPorInvisibilidad;
        boolean esquivoPorProbabilidad;
        boolean esquivoPorEvasionActiva;

        double roll;
        boolean tieneEscudo;
        int danioAbsorbido;
        int danioRestante;


        bloqueadoPorInvisibilidad = false;
        esquivoPorProbabilidad = false;
        esquivoPorEvasionActiva = false;

        tieneEscudo = false;
        danioAbsorbido = 0;
        danioRestante = danio;
        roll = 0.0;


        if (invisible) {
            bloqueadoPorInvisibilidad = true;
            invisible = false;
        }


        if (!bloqueadoPorInvisibilidad && probabilidadEsquivaProximoAtaque > 0.0) {
            roll = Math.random();
            esquivoPorProbabilidad = (roll < probabilidadEsquivaProximoAtaque);
            probabilidadEsquivaProximoAtaque = 0.0;
        }


        if (!bloqueadoPorInvisibilidad && !esquivoPorProbabilidad && evasionActiva) {
            esquivoPorEvasionActiva = true;
            evasionActiva = false;
        }


        if (bloqueadoPorInvisibilidad || esquivoPorProbabilidad || esquivoPorEvasionActiva) {

        } else {

            tieneEscudo = (vidaEscudo > 0);

            if (tieneEscudo) {
                danioAbsorbido = danio;
                if (danioAbsorbido > vidaEscudo) {
                    danioAbsorbido = vidaEscudo;
                }

                vidaEscudo = vidaEscudo - danioAbsorbido;
                danioRestante = danio - danioAbsorbido;
            }


            if (danioRestante > 0) {
                super.recibirDanio(danioRestante);
            }
        }
    }

    // ================== TO STRING ==================

    @Override
    public String toString() {
        String texto;

        texto = "Jugador " + nombre;
        return texto;
    }
}