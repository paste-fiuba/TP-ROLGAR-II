package com.logica;

/**
 * Enum que representa las acciones/intenciones del jugador
 * durante el combate.
 */
public enum AccionCombate {
    // Acciones principales
    ATACAR,
    ABRIR_MENU_CARTA,
    HUIR,

    // Selección de carta
    USAR_CARTA_1,
    USAR_CARTA_2,
    USAR_CARTA_3,
    USAR_CARTA_4,
    USAR_CARTA_5,
    USAR_CARTA_6,
    USAR_CARTA_7,
    USAR_CARTA_8,
    USAR_CARTA_9,
    USAR_CARTA_0,

    // UI de combate
    CANCELAR_CARTA, // (Corresponde a 'Escape')
    NINGUNA
}