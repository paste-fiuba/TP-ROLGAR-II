package com.logica;

import java.awt.event.KeyEvent;

/**
 * TDA para modularizar la entrada de teclado.
 * Traduce los KeyCodes de Swing a AccionesJuego semánticas.
 */
public class LectorTeclado {

    /**
     * Traduce un código de tecla de Swing a una AccionJuego.
     * @param keyCode El KeyEvent.VK_...
     * @return La AccionJuego correspondiente.
     */
    public AccionJuego traducirInput(int keyCode) {
        switch (keyCode) {
            // Movimiento
            case KeyEvent.VK_W: return AccionJuego.MOV_ARRIBA;
            case KeyEvent.VK_S: return AccionJuego.MOV_ABAJO;
            case KeyEvent.VK_A: return AccionJuego.MOV_IZQUIERDA;
            case KeyEvent.VK_D: return AccionJuego.MOV_DERECHA;
            case KeyEvent.VK_Q: return AccionJuego.MOV_DIAG_ARRIBA_IZQ;
            case KeyEvent.VK_E: return AccionJuego.MOV_DIAG_ARRIBA_DER;
            case KeyEvent.VK_Z: return AccionJuego.MOV_DIAG_ABAJO_IZQ;
            case KeyEvent.VK_C: return AccionJuego.MOV_DIAG_ABAJO_DER;

            // Acciones
            case KeyEvent.VK_F: return AccionJuego.ACCION_ATACAR_JUGADOR;
            case KeyEvent.VK_T: return AccionJuego.ACCION_INICIAR_TRANSFERENCIA;
            case KeyEvent.VK_L: return AccionJuego.ACCION_PROPONER_ALIANZA;
            case KeyEvent.VK_Y: return AccionJuego.ACCION_ACEPTAR_ALIANZA;
            case KeyEvent.VK_N: return AccionJuego.ACCION_RECHAZAR_ALIANZA;

            // Cartas
            case KeyEvent.VK_1: return AccionJuego.USAR_CARTA_1;
            case KeyEvent.VK_2: return AccionJuego.USAR_CARTA_2;
            case KeyEvent.VK_3: return AccionJuego.USAR_CARTA_3;
            case KeyEvent.VK_4: return AccionJuego.USAR_CARTA_4;
            case KeyEvent.VK_5: return AccionJuego.USAR_CARTA_5;
            case KeyEvent.VK_6: return AccionJuego.USAR_CARTA_6;
            case KeyEvent.VK_7: return AccionJuego.USAR_CARTA_7;
            case KeyEvent.VK_8: return AccionJuego.USAR_CARTA_8;
            case KeyEvent.VK_9: return AccionJuego.USAR_CARTA_9;
            case KeyEvent.VK_0: return AccionJuego.USAR_CARTA_0;

            // Sistema
            case KeyEvent.VK_ESCAPE: return AccionJuego.PAUSA;

            default:
                return AccionJuego.NINGUNA;
        }
    }
}