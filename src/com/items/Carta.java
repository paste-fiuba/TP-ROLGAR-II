package com.items;

import com.entidades.Entidad;
import com.entidades.Personaje;

public abstract class Carta {

    protected String nombre;
    protected String descripcion;

    public Carta(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public abstract void aplicarEfecto(Personaje usuario, Entidad objetivo);

    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
