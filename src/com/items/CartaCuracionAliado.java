package com.items;
import com.entidades.Entidad;
import com.entidades.Personaje;
public class CartaCuracionAliado extends Carta {
    private int cantidad;
    public CartaCuracionAliado(int cantidad) {
        super("Curación de Aliado", "Cura " + cantidad + " puntos de vida a un aliado.");
        this.cantidad = cantidad;
        // --- LÍNEA ELIMINADA ---
    }
    @Override
    public void aplicarEfecto(Personaje usuario, Entidad objetivo) {
        Personaje objetivoPersonaje = null;
        if (objetivo instanceof Personaje) {
            objetivoPersonaje = (Personaje) objetivo;
        }
        if (objetivoPersonaje == null && usuario != null) {
            com.entidades.Alianza alianza = usuario.getAlianza();
            if (alianza != null) {
                int ux = usuario.getPosX();
                int uy = usuario.getPosY();
                int uz = usuario.getPosZ();
                int mejorDist = Integer.MAX_VALUE;
                Personaje candidato = null;
                for (Personaje p : alianza.getMiembros()) {
                    if (p == null || p == usuario) continue;
                    if (p.getVida() <= 0) continue;
                    if (p.getPosZ() != uz) continue;
                    int dist = Math.abs(p.getPosX() - ux) + Math.abs(p.getPosY() - uy);
                    if (dist <= 1) {
                        candidato = p;
                        break;
                    }
                    if (dist < mejorDist) {
                        mejorDist = dist;
                        candidato = p;
                    }
                }
                objetivoPersonaje = candidato;
            }
        }
        if (objetivoPersonaje == null) {
            System.out.println("No hay aliado válido para Curación de Aliado. La carta no tuvo efecto.");
            return;
        }
        int nuevaVida = Math.min(100, objetivoPersonaje.getVida() + cantidad);
        objetivoPersonaje.setVida(nuevaVida);
        System.out.println(usuario.getNombre() + " curó a " + objetivoPersonaje.getNombre() + " en " + cantidad + " puntos.");
    }
}