package com.items;

// --- Imports de imagen ELIMINADOS ---
import com.entidades.Entidad;
import com.entidades.Personaje;

public abstract class Carta {

    protected String nombre;
    protected String descripcion;

    // --- CAMPO ELIMINADO ---
    // protected BufferedImage imagen;

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

    // --- MÉTODO ELIMINADO ---
    // public BufferedImage getImagen() { ... }

    // --- MÉTODO ELIMINADO ---
    // protected BufferedImage cargarImagen(String ruta) { ... }
}