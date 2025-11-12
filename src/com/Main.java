package com; // O el paquete donde tengas tu Main

// Importaciones de la Interfaz Gráfica (Vista)
import com.ui.PanelJuego;
import com.ui.VentanaJuego;

// Importaciones del Tablero (Modelo)
import com.tablero.Tablero;
import com.tablero.TipoCasillero;
import com.entidades.Personaje; 

// Importamos el Controlador
import com.logica.ControladorJuego; // O la ruta a tu ControladorJuego

/**
 * Clase principal para iniciar y probar el juego Rolgar II.
 * Configura el Modelo, la Vista y el Controlador (MVC).
 */
public class Main {

    // --- Constantes para el tablero ---
    private static final int ANCHO_TABLERO = 25;
    private static final int ALTO_TABLERO = 20;
    private static final int NIVELES_TABLERO = 2;
    private static final int NIVEL_INICIAL = 0; // Empezamos en el piso 0

    public static void main(String[] args) {
        
        System.out.println("[Main] Iniciando Rolgar II...");

        // --- 1. CREAR EL MODELO (Tablero y Personaje) ---
        System.out.println("[Main] Creando modelo...");
        Tablero miTablero = new Tablero(ANCHO_TABLERO, ALTO_TABLERO, NIVELES_TABLERO);
        crearMundoCoherente(miTablero);
        
        Personaje miPersonaje = crearPersonaje();

        // --- 2. CREAR LA VISTA ---
        // Creamos el panel (la vista) y le pasamos los datos del modelo que necesita.
        System.out.println("[Main] Creando la vista...");
        PanelJuego miPanel = new PanelJuego(miTablero, miPersonaje, NIVEL_INICIAL);
        
        // --- 3. CREAR EL CONTROLADOR ---
        // Creamos el controlador y le pasamos el modelo Y la vista.
        System.out.println("[Main] Creando el controlador...");
        ControladorJuego miControlador = new ControladorJuego(miTablero, miPersonaje, miPanel);

        // --- 4. "CONECTAR" TODO (La parte clave) ---
        System.out.println("[Main] Conectando MVC...");
        
        // "Inyectamos" el controlador en la vista. 
        // Ahora el panel sabe a quién avisarle cuando se presiona una tecla.
        miPanel.setControlador(miControlador);
        
        // Creamos la ventana y le pasamos el panel ya listo.
        new VentanaJuego(miPanel); 
        
        // Le decimos al panel que empiece a escuchar (IMPORTANTE: hacer al final).
        miPanel.iniciarOyenteTeclado();

        System.out.println("[Main] ¡Juego iniciado!");
    }

    /**
     * Método de ayuda para crear al personaje (mantiene 'main' limpio)
     */
    private static Personaje crearPersonaje() {
        int centroX = ANCHO_TABLERO / 2;
        int centroY = ALTO_TABLERO / 2;
        
        // Llama al constructor de 8 argumentos de tu clase Personaje
        return new Personaje("Héroe Rolgar", 100, 
                             centroX, centroY, NIVEL_INICIAL, 
                             10, 1, 5.0);
    }
    
    /**
     * Método de ayuda para "tallar" un mundo coherente en el tablero.
     * (Sin cambios)
     */
    private static void crearMundoCoherente(Tablero tablero) {
        
        System.out.println("[Mundo] Rellenando todo el mundo de ROCA sólida...");
        for (int z = 0; z < NIVELES_TABLERO; z++) {
            for (int y = 0; y < ALTO_TABLERO; y++) {
                for (int x = 0; x < ANCHO_TABLERO; x++) {
                    tablero.getCasillero(x, y, z).setTipo(TipoCasillero.ROCA);
                }
            }
        }

        System.out.println("[Mundo] Tallando salas y pasillos en Nivel 0...");
        
        // Sala 1: La sala inicial (central)
        tallarHabitacion(tablero, 10, 8, 15, 12, 0); 
        
        // Sala 2: Sala superior (con un lago)
        tallarHabitacion(tablero, 10, 2, 15, 5, 0);
        
        // Sala 3: Sala izquierda (con la rampa)
        tallarHabitacion(tablero, 2, 8, 7, 12, 0);

        // Conectar Sala 1 y Sala 2 (Pasillo vertical)
        tallarPasilloVertical(tablero, 12, 5, 8, 0);
        
        // Conectar Sala 1 y Sala 3 (Pasillo horizontal)
        tallarPasilloHorizontal(tablero, 7, 10, 10, 0);

        System.out.println("[Mundo] Añadiendo lago y rampa...");
        
        // Añadir un lago de AGUA en la Sala 2
        tablero.getCasillero(12, 3, 0).setTipo(TipoCasillero.AGUA);
        tablero.getCasillero(13, 3, 0).setTipo(TipoCasillero.AGUA);
        
        // Añadir la RAMPA en la Sala 3
        tablero.getCasillero(4, 10, 0).setTipo(TipoCasillero.RAMPA);

        System.out.println("[Mundo] Tallando sala de llegada en Nivel 1...");
        // Tallar una pequeña sala de llegada en el Nivel 1
        tallarHabitacion(tablero, 3, 9, 5, 11, 1);
        
        // Poner la rampa de conexión en el Nivel 1
        tablero.getCasillero(4, 10, 1).setTipo(TipoCasillero.RAMPA);
    }

    // --- Métodos de ayuda para tallar el mundo ---

    private static void tallarHabitacion(Tablero tablero, int x1, int y1, int x2, int y2, int z) {
        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                if (tablero.esCoordenadaValida(x, y, z)) {
                    tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
                }
            }
        }
    }

    private static void tallarPasilloHorizontal(Tablero tablero, int x1, int x2, int y, int z) {
        for (int x = x1; x <= x2; x++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
    
    private static void tallarPasilloVertical(Tablero tablero, int x, int y1, int y2, int z) {
        for (int y = y1; y <= y2; y++) {
            if (tablero.esCoordenadaValida(x, y, z)) {
                tablero.getCasillero(x, y, z).setTipo(TipoCasillero.VACIO);
            }
        }
    }
}