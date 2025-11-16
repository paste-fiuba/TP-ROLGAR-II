package com.logica;

/**
 * Enum que representa las intenciones/acciones del jugador,
 * independientemente de la tecla presionada.
 */
public enum AccionJuego {
    // Movimiento
    MOV_ARRIBA,
    MOV_ABAJO,
    MOV_IZQUIERDA,
    MOV_DERECHA,
    MOV_DIAG_ARRIBA_IZQ,
    MOV_DIAG_ARRIBA_DER,
    MOV_DIAG_ABAJO_IZQ,
    MOV_DIAG_ABAJO_DER,

    // Acciones
    ACCION_ATACAR_JUGADOR,
    ACCION_INICIAR_TRANSFERENCIA,
    ACCION_PROPONER_ALIANZA,
    ACCION_ACEPTAR_ALIANZA,
    ACCION_RECHAZAR_ALIANZA,

    // Uso de Cartas (Slots)
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
    
    // UI / Estado
    PAUSA,
    NINGUNA // Para teclas no mapeadas
}