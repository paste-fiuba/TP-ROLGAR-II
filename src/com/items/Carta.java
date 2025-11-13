package com.items;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.entidades.Entidad;
import com.entidades.Personaje;

public abstract class Carta {

    protected String nombre;
    protected String descripcion;

    
    protected BufferedImage imagen;

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

    
    public BufferedImage getImagen() {
        return imagen;
    }

    
    protected BufferedImage cargarImagen(String ruta) {
        try {
            return ImageIO.read(new File(ruta));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
