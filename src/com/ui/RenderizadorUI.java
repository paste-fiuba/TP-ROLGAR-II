package com.ui;

import com.entidades.Enemigo;
import com.entidades.Entidad; 
import com.entidades.Personaje;
import com.items.Carta;
import com.items.Inventario;
import com.logica.AdministradorDeCombate;
import com.logica.AdministradorDeJuego;
import com.logica.ControladorJuego;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class RenderizadorUI {

    private BufferedImage spriteSlot;
    private BufferedImage spritePersonajeBatalla; // Sprite grande de personaje
    private BufferedImage spriteEnemigoBatalla;   // Sprite grande de enemigo
    
    private RenderizarMenu renderizadorMenu = new RenderizarMenu();
    private RenderizarData renderizadorData = new RenderizarData();

    private Font fontMenuTitulo, fontMenuOpcion, fontInfo;
    private Font fontGameOver, fontCombate, fontCombateInfo; // Fuentes para combate
    private List<String> battleLog;
    
    // --- COLORES DE UI ---
    private Color colorCajaUI = new Color(248, 248, 248); // Casi blanco
    private Color colorCajaBorde = new Color(80, 80, 80);
    private Color colorFondoBatallaCielo = new Color(20, 20, 20); // El negro/gris oscuro del fondo
    private Color colorFondoBatallaPasto = new Color(40, 30, 20); // Un marrón oscuro para el "piso"
    private Color colorPlataformaBatalla = new Color(110, 80, 40); // El marrón de la plataforma (como vacio.png)


    public RenderizadorUI() {
        cargarSpritesUI();
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
        this.fontInfo = new Font("Arial", Font.BOLD, 16);
        this.fontGameOver = new Font("Arial", Font.BOLD, 100);
        new Font("Arial", Font.PLAIN, 18);
        this.fontCombate = new Font("Arial", Font.BOLD, 28); 
        this.fontCombateInfo = new Font("Arial", Font.BOLD, 22); // Fuente para HP
        this.battleLog = new ArrayList<>();
    }

    private void cargarSpritesUI() {
        try {
            this.spriteSlot = ImageIO.read(new File("src/sprites/slot.png"));
        } catch (IOException e) {
            System.err.println("WARN: No se pudo cargar 'slot.png'");
        }
        
        // Intentar cargar el sprite grande del personaje
        try {
            this.spritePersonajeBatalla = ImageIO.read(new File("src/sprites/personaje_batalla.png"));
        } catch (IOException e) {
            System.err.println("WARN: 'personaje_batalla.png' no encontrado. Usando 'personaje.png' (chico) como fallback.");
            try {
                // Si falla, cargar el sprite chico
                this.spritePersonajeBatalla = ImageIO.read(new File("src/sprites/personaje.png"));
            } catch (IOException e2) {
                System.err.println("ERROR: No se pudo cargar ni 'personaje_batalla.png' ni 'personaje.png'.");
            }
        }
        
        // Intentar cargar el sprite grande del enemigo
        try {
            this.spriteEnemigoBatalla = ImageIO.read(new File("src/sprites/enemigo_batalla.png"));
        } catch (IOException e) {
            System.err.println("WARN: 'enemigo_batalla.png' no encontrado. Usando 'ogro.png' (chico) como fallback.");
            try {
                // Si falla, cargar el sprite chico
                this.spriteEnemigoBatalla = ImageIO.read(new File("src/sprites/ogro.png"));
            } catch (IOException e2) {
                System.err.println("ERROR: No se pudo cargar ni 'enemigo_batalla.png' ni 'ogro.png'.");
            }
        }
    }

    public void agregarMensajeLog(String mensaje) {
        this.battleLog.add(mensaje);
        // Mantenemos solo los últimos 2 mensajes para la caja de log
        if (this.battleLog.size() > 2) {
            this.battleLog.remove(0);
        }
    }

    public void limpiarLog() {
        this.battleLog.clear();
    }

    
    public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico) {
    	renderizadorData.dibujarHotbar(g, inventario, anchoLogico, altoJuegoLogico, this.spriteSlot);
    }

    public void dibujarMenuPausa(Graphics g, int anchoVentana, int altoVentana) {
    	renderizadorMenu.dibujarMenuPausa(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
    }

    public void dibujarPantallaGameOver(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(100, 0, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.RED);
        g.setFont(fontGameOver);
        String titulo = "PERDISTE";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuOpcion);
        String opcion = "[Q] Cerrar Juego";
        int anchoOpcion = g.getFontMetrics().stringWidth(opcion);
        g.drawString(opcion, (anchoVentana - anchoOpcion) / 2, altoVentana / 2 + 60);
    }

    public void dibujarPantallaVictoria(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 80, 20, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.YELLOW);
        g.setFont(fontGameOver);
        String titulo = "¡GANASTE!";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuOpcion);
        String opcion = "[Q] Cerrar Juego";
        int anchoOpcion = g.getFontMetrics().stringWidth(opcion);
        g.drawString(opcion, (anchoVentana - anchoOpcion) / 2, altoVentana / 2 + 60);
    }

    /**
     * Dibuja la pantalla correspondiente al estado de menú actual.
     */
    public void dibujarMenu(Graphics g, ControladorJuego.GameState estado, int anchoVentana, int altoVentana) {
        switch (estado) {
            case MENU_PRINCIPAL:
                renderizadorMenu.dibujarMenuPrincipal(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            case MENU_DIFICULTAD:
                renderizadorMenu.dibujarMenuDificultad(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            case MENU_JUGADORES:
                renderizadorMenu.dibujarMenuJugadores(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            case MENU_INSTRUCCIONES: 
                renderizadorMenu.dibujarMenuInstrucciones(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            default:
                break;
        }
    }

    
    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer) {
    	Enemigo enemigoMasCercano = AdministradorDeJuego.encontrarEnemigoMasCercano(p, e);
    	renderizadorData.dibujarInfoJuego(g, p, e, jugadores, admin, pendingTransfer, this.fontInfo, this.battleLog, enemigoMasCercano);
    }

    
    /**
     * pre: adminCombate no es null.
     * post: Dibuja la pantalla de combate estilo Pokémon.
     */
    public void dibujarPantallaCombate(Graphics g, AdministradorDeCombate adminCombate, int anchoVentana, int altoVentana) {
        if (adminCombate == null) return;
        
        Personaje jugador = adminCombate.getJugador();
        Entidad oponente = adminCombate.getOponente();
        AdministradorDeCombate.EstadoCombate estado = adminCombate.getEstado();

        // 1. Fondo (Estilo Cueva)
        g.setColor(colorFondoBatallaCielo); 
        g.fillRect(0, 0, anchoVentana, (int)(altoVentana * 0.65)); 
        g.setColor(colorFondoBatallaPasto); 
        g.fillRect(0, (int)(altoVentana * 0.65), anchoVentana, (int)(altoVentana * 0.35));


        // 2. Plataformas (Color cueva)
        int plataformaAncho = 240;
        int plataformaAlto = 40;
        int plataformaOponenteX = (int)(anchoVentana * 0.65) - (plataformaAncho / 2);
        int plataformaOponenteY = (int)(altoVentana * 0.2) + 120;
        int plataformaJugadorX = (int)(anchoVentana * 0.15) + 10;
        int plataformaJugadorY = (int)(altoVentana * 0.4) + 120;
        
        g.setColor(colorPlataformaBatalla); 
        g.fillOval(plataformaOponenteX, plataformaOponenteY, plataformaAncho, plataformaAlto);
        g.fillOval(plataformaJugadorX, plataformaJugadorY, plataformaAncho, plataformaAlto);


        // 3. Dibujar Sprites (escalados 96x96)
        int spriteSize = 96; 
        
        // Oponente (Arriba-Derecha)
        if (spriteEnemigoBatalla != null) {
             g.drawImage(spriteEnemigoBatalla, 
                (int)(anchoVentana * 0.65), 
                (int)(altoVentana * 0.2), 
                spriteSize, spriteSize, null);
        }
        
        // Jugador (Abajo-Izquierda)
        if (spritePersonajeBatalla != null) {
            g.drawImage(spritePersonajeBatalla, 
                (int)(anchoVentana * 0.15) + (spriteSize / 2), 
                (int)(altoVentana * 0.4), 
                spriteSize, spriteSize, null);
        }
        
        
        // 4. Caja de Acciones (Abajo)
        int altoCajaAccion = (int)(altoVentana * 0.35); 
        int yCajaAccion = altoVentana - altoCajaAccion;
        
        g.setColor(colorCajaUI);
        g.fillRoundRect(0, yCajaAccion, anchoVentana-1, altoCajaAccion, 15, 15);
        
        g.setColor(colorCajaBorde);
        g.drawRoundRect(0, yCajaAccion, anchoVentana-1, altoCajaAccion, 15, 15);

        int xDivision = anchoVentana / 2;
        g.fillRect(xDivision, yCajaAccion, 4, altoCajaAccion); 

        // Lado Izquierdo: Log de Batalla
        g.setFont(fontMenuOpcion);
        int logX = 40; 
        int logY = yCajaAccion + 60;
        g.setColor(Color.BLACK);
        
        g.drawString(adminCombate.getMensajeAccion(), logX, logY);
        
        int logMsgY = logY + 40;
        for (String mensaje : battleLog) {
            g.drawString(mensaje, logX, logMsgY);
            logMsgY += 25;
        }

        // Lado Derecho: Opciones (Depende del estado)
        if (estado == AdministradorDeCombate.EstadoCombate.ELIGE_ACCION) {
            g.setFont(fontCombate);
            g.drawString("[1] LUCHAR", xDivision + 50, yCajaAccion + 80);
            g.drawString("[2] CARTA", xDivision + 270, yCajaAccion + 80);
            g.drawString("[3] HUIR", xDivision + 270, yCajaAccion + 140);
        
        } else if (estado == AdministradorDeCombate.EstadoCombate.ELIGE_CARTA) {
            // Dibujar el inventario para seleccionar
            g.setFont(fontInfo); // Fuente más chica
            Inventario inv = jugador.getInventario();
            for (int i = 0; i < 10; i++) {
                int slot = (i + 1) % 10; // 1, 2, ... 9, 0
                String textoCarta = "[" + slot + "] ";
                Carta c = inv.getCarta(i);
                if (c != null) {
                    textoCarta += c.getNombre();
                } else {
                    textoCarta += "- Vacío -";
                }
                
                int col = i / 5; // Columna 0 o 1
                int fila = i % 5; // Fila 0 a 4
                
                int x = xDivision + 40 + (col * 220);
                int y = yCajaAccion + 60 + (fila * 20);
                g.drawString(textoCarta, x, y);
            }
            g.setFont(fontMenuOpcion);
            g.setColor(Color.BLUE);
            g.drawString("[ESC] Cancelar", xDivision + 50, yCajaAccion + 170);
        }
        

        // 5. Cajas de Info (HP)
        g.setFont(fontCombateInfo);
        
        // Caja Oponente (Arriba-Izquierda)
        int cajaAncho = 350;
        int cajaAlto = 80;
        int cajaOponenteX = 50;
        int cajaOponenteY = 50;
        dibujarCajaInfo(g, oponente.getNombre(), oponente.getVida(), cajaOponenteX, cajaOponenteY, cajaAncho, cajaAlto);


        // Caja Jugador (Abajo-Derecha)
        int cajaJugadorX = anchoVentana - cajaAncho - 150;
        int cajaJugadorY = (int)(altoVentana * 0.65) - cajaAlto - 30; // Justo arriba de la caja de acciones
        dibujarCajaInfo(g, jugador.getNombre(), jugador.getVida(), cajaJugadorX, cajaJugadorY, cajaAncho, cajaAlto);
    }
    
    /**
     * pre: g no es null, x e y son posiciones válidas.
     * post: Dibuja la caja de info de un combatiente (nombre y HP).
     */
    private void dibujarCajaInfo(Graphics g, String nombre, int vida, int x, int y, int ancho, int alto) {
        renderizadorData.dibujarCajaInfo(g, nombre, vida, x, y, ancho, alto, this.colorCajaUI, this.colorCajaBorde, this.fontCombateInfo, this.fontInfo);
    }

}