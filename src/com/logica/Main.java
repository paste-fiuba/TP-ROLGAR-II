package com.logica;

import com.entidades.Personaje;
import com.items.*;
// MAIN DE PRUEBA----------------------- asi testeaba las cartas por las dudas
public class Main {

    public static void main(String[] args) {
        // Crear dos personajes para la prueba
        Personaje jugador = new Personaje("Link", 100, 1, 1, 1, 15, 3, 10);
        Personaje enemigo = new Personaje("Orco", 80, 2, 1, 1, 12, 2, 5);

        // Agregar cartas a ambos
        jugador.agregarCarta(new CartaCuracionTotal());
        jugador.agregarCarta(new CartaEscudo(50)); // ✅ escudo de 50%

        jugador.agregarCarta(new CartaEsquivarDanio(0.7));

        enemigo.agregarCarta(new CartaAumentoVida(20));
        enemigo.agregarCarta(new CartaRoboDeCarta());
        enemigo.agregarCarta(new CartaTeletransportacion(3, 2, 0));

        // Mostrar inventarios iniciales
        System.out.println("=== Inventarios iniciales ===");
        System.out.println("Jugador:\n" + jugador.getInventario());
        System.out.println("Enemigo:\n" + enemigo.getInventario());

        // Probar uso de cartas
        System.out.println("\n=== Pruebas de efectos ===");

        jugador.usarCarta(0, jugador); // Curación total
        jugador.usarCarta(1, jugador); // Escudo
        jugador.usarCarta(2, enemigo); // Evasión
        enemigo.usarCarta(1, jugador); // Robo de carta

        // Mostrar inventarios después de usar cartas
        System.out.println("\n=== Inventarios finales ===");
        System.out.println("Jugador:\n" + jugador.getInventario());
        System.out.println("Enemigo:\n" + enemigo.getInventario());
    }
}
