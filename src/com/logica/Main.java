package com.logica; // O el paquete donde tengas tu Main

// Importaciones de la Interfaz Gráfica (Vista)
import com.ui.PanelJuego;
import com.ui.VentanaJuego;

// Importaciones del Tablero (Modelo)
import com.tablero.Tablero;
import com.tablero.TipoCasillero;

/**
 * Clase principal para iniciar y probar el juego Rolgar II.
 * Configura el modelo (Tablero) y la vista (VentanaJuego).
 */
public class Main {

    // --- Constantes para un tablero GRANDE ---
    private static final int ANCHO_TABLERO = 30; // <-- Aumentado
    private static final int ALTO_TABLERO = 22;  // <-- Aumentado
    private static final int NIVELES_TABLERO = 2;
    private static final int NIVEL_INICIAL = 0; // Empezamos en el piso 0

    public static void main(String[] args) {
        
        System.out.println("[Main] Iniciando Rolgar II...");

        // --- 1. CREAR EL MODELO ---
        System.out.println("[Main] Creando modelo del tablero (" + 
                           ANCHO_TABLERO + "x" + ALTO_TABLERO + "x" + NIVELES_TABLERO + ")...");
        Tablero miTablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO, NIVELES_TABLERO);
        
        crearMundoDePrueba(miTablero);

        // --- 2. CREAR LA VISTA (UI) ---
        System.out.println("[Main] Iniciando interfaz grafica...");
        
        PanelJuego miPanel = new PanelJuego(miTablero, NIVEL_INICIAL);
        new VentanaJuego(miPanel); 

        System.out.println("[Main] ¡Juego iniciado! Mostrando nivel " + NIVEL_INICIAL);
    }

    /**
     * Método de ayuda para llenar el tablero con terreno de prueba.
     *
     * @param tablero El tablero a modificar.
     */
    private static void crearMundoDePrueba(Tablero tablero) {
        
        System.out.println("[Mundo] Rellenando pisos con 'VACIO'...");
        for (int z = 0; z < NIVELES_TABLERO; z++) {
            for (int y = 0; y < ALTO_TABLERO; y++) {
                for (int x = 0; x < ANCHO_TABLERO; x++) {
                    tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
                }
            }
        }

        System.out.println("[Mundo] Creando estructuras en Nivel 0...");
        // --- Nivel 0 ---
        
        // Un "lago" de AGUA cerca de la esquina superior izquierda
        for(int y = 2; y <= 5; y++) {
            for(int x = 2; x <= 6; x++) {
                tablero.getCasillero(x, y, 0).setTipo(TipoCasillero.AGUA);
            }
        }

        // Una "pared" de ROCA más central
        for(int y = 10; y <= 20; y++) {
            tablero.getCasillero(15, y, 0).setTipo(TipoCasillero.ROCA);
        }
        for(int x = 10; x <= 15; x++) {
            tablero.getCasillero(x, 10, 0).setTipo(TipoCasillero.ROCA);
        }

        // La "RAMPA" (o ascensor) para subir, en la esquina opuesta
        tablero.getCasillero(ANCHO_TABLERO - 2, ALTO_TABLERO - 2, 0).setTipo(TipoCasillero.RAMPA);

        // --- Nivel 1 ---
        System.out.println("[Mundo] Creando estructuras en Nivel 1...");
        
        // La "RAMPA" que conecta desde arriba
        tablero.getCasillero(ANCHO_TABLERO - 2, ALTO_TABLERO - 2, 1).setTipo(TipoCasillero.RAMPA);
        
        // Un obstáculo de ROCA en el piso de arriba
        tablero.getCasillero(5, 5, 1).setTipo(TipoCasillero.ROCA);
    }
}