package com.ui;

import com.entidades.Enemigo;
import com.entidades.Entidad; 
import com.entidades.Personaje;
import com.items.Carta;
import com.items.Inventario;
import com.logica.ControladorJuego; 
import com.logica.AdministradorDeCombate;
import com.logica.AdministradorDeJuego;
import com.tablero.Casillero; 
import com.tablero.Tablero;   

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

    private Font fontMenuTitulo, fontMenuOpcion, fontInfo, fontInstrucciones;
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
        this.fontInstrucciones = new Font("Arial", Font.PLAIN, 18);
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
                this.spriteEnemigoBatalla = ImageIO.read(new File("src/sprites/ogro.png"));
            } catch (IOException e2) {
                System.err.println("ERROR: No se pudo cargar ni 'enemigo_batalla.png' ni 'ogro.png'.");
            }
        }
    }

    public void agregarMensajeLog(String mensaje) {
        this.battleLog.add(mensaje);
        if (this.battleLog.size() > 5) {
            this.battleLog.remove(0);
        }
    }

    public void limpiarLog() {
        this.battleLog.clear();
    }

    
    public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico) {

        int ALTURA_HOTBAR = PanelJuego.ALTURA_HOTBAR;

        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, altoJuegoLogico, anchoLogico, ALTURA_HOTBAR);

        if (this.spriteSlot == null) return;
        if (inventario == null) return; 

        int numSlots = 10;
        int tamañoSlot = 48;
        int padding = (ALTURA_HOTBAR - tamañoSlot) / 2;
        int anchoTotalSlots = (numSlots * tamañoSlot) + ((numSlots - 1) * 5);
        int startX = (anchoLogico - anchoTotalSlots) / 2;

        for (int i = 0; i < numSlots; i++) {
            int x = startX + (i * (tamañoSlot + 5));
            int y = altoJuegoLogico + padding;

            g.drawImage(this.spriteSlot, x, y, tamañoSlot, tamañoSlot, null);

            if (i < inventario.cantidadDeCartas()) {
                Carta carta = inventario.getCarta(i);
                if (carta != null && carta.getImagen() != null) {
                    int tamañoCarta = 40;
                    int paddingCarta = (tamañoSlot - tamañoCarta) / 2;
                    g.drawImage(
                        carta.getImagen(),
                        x + paddingCarta,
                        y + paddingCarta,
                        tamañoCarta,
                        tamañoCarta,
                        null
                    );
                }
            }
        }
    }

    public void dibujarMenuPausa(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "JUEGO PAUSADO";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 50);

        g.setFont(fontMenuOpcion);
        String opcion1 = "[R] Reanudar";
        String opcion2 = "[Q] Cerrar Juego";

        int anchoOpcion1 = g.getFontMetrics().stringWidth(opcion1);
        int anchoOpcion2 = g.getFontMetrics().stringWidth(opcion2);

        g.drawString(opcion1, (anchoVentana - anchoOpcion1) / 2, altoVentana / 2 + 20);
        g.drawString(opcion2, (anchoVentana - anchoOpcion2) / 2, altoVentana / 2 + 60);
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
                dibujarMenuPrincipal(g, anchoVentana, altoVentana);
                break;
            case MENU_DIFICULTAD:
                dibujarMenuDificultad(g, anchoVentana, altoVentana);
                break;
            case MENU_JUGADORES:
                dibujarMenuJugadores(g, anchoVentana, altoVentana);
                break;
            case MENU_INSTRUCCIONES: 
                dibujarMenuInstrucciones(g, anchoVentana, altoVentana);
                break;
            default:
                break;
        }
    }

    private void dibujarMenuPrincipal(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "ROLGAR II";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String op1 = "[1] Empezar Partida";
        String op2 = "[2] Instrucciones";
        String op3 = "[3] Salir del Juego";

        g.drawString(op1, (anchoVentana - g.getFontMetrics().stringWidth(op1)) / 2, altoVentana / 2);
        g.drawString(op2, (anchoVentana - g.getFontMetrics().stringWidth(op2)) / 2, altoVentana / 2 + 40);
        g.drawString(op3, (anchoVentana - g.getFontMetrics().stringWidth(op3)) / 2, altoVentana / 2 + 80);
    }

    private void dibujarMenuDificultad(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(10, 0, 10, 200)); 
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Seleccionar Dificultad";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String op1 = "[1] Fácil";
        String op2 = "[2] Normal";
        String op3 = "[3] Difícil";
        String opEsc = "[ESC] Volver";

        g.setColor(Color.GREEN);
        g.drawString(op1, (anchoVentana - g.getFontMetrics().stringWidth(op1)) / 2, altoVentana / 2);
        g.setColor(Color.YELLOW);
        g.drawString(op2, (anchoVentana - g.getFontMetrics().stringWidth(op2)) / 2, altoVentana / 2 + 40);
        g.setColor(Color.RED);
        g.drawString(op3, (anchoVentana - g.getFontMetrics().stringWidth(op3)) / 2, altoVentana / 2 + 80);
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(opEsc, (anchoVentana - g.getFontMetrics().stringWidth(opEsc)) / 2, altoVentana / 2 + 150);
    }

    private void dibujarMenuJugadores(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(0, 10, 10, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Cantidad de Jugadores";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 2 - 80);

        g.setFont(fontMenuOpcion);
        String op1 = "[1] Un Jugador";
        String op2 = "[2] Dos Jugadores";
        String op3 = "[3] Tres Jugadores";
        String op4 = "[4] Cuatro Jugadores";
        String opEsc = "[ESC] Volver";

        g.setColor(Color.CYAN);
        g.drawString(op1, (anchoVentana - g.getFontMetrics().stringWidth(op1)) / 2, altoVentana / 2 - 20);
        g.drawString(op2, (anchoVentana - g.getFontMetrics().stringWidth(op2)) / 2, altoVentana / 2 + 20);
        g.drawString(op3, (anchoVentana - g.getFontMetrics().stringWidth(op3)) / 2, altoVentana / 2 + 60);
        g.drawString(op4, (anchoVentana - g.getFontMetrics().stringWidth(op4)) / 2, altoVentana / 2 + 100);
        
        g.setColor(Color.LIGHT_GRAY);
        g.drawString(opEsc, (anchoVentana - g.getFontMetrics().stringWidth(opEsc)) / 2, altoVentana / 2 + 170);
    }
    
    private void dibujarMenuInstrucciones(Graphics g, int anchoVentana, int altoVentana) {
        g.setColor(new Color(10, 10, 0, 200));
        g.fillRect(0, 0, anchoVentana, altoVentana);

        g.setColor(Color.WHITE);
        g.setFont(fontMenuTitulo);
        String titulo = "Instrucciones";
        int anchoTitulo = g.getFontMetrics().stringWidth(titulo);
        g.drawString(titulo, (anchoVentana - anchoTitulo) / 2, altoVentana / 4); 

        g.setFont(fontInstrucciones);
        
        String[] lineas = {
            "Muevete con W, A, S, D (Ortogonal) y Q, E, Z, C (Diagonal).",
            "Cada movimiento gasta un turno.",
            "Entra en combate con Enemigos (E) al caminar sobre ellos.",
            "Recoge Cartas (?) para obtener poderes.",
            "Usa 1-0 para activar cartas (gasta un turno).",
            "",
            "Multijugador:",
            "Presiona [F] para atacar a otro jugador adyacente.",
            "Presiona [L] para proponer alianzas a un jugador adyacente.",
            "Presiona [Y] / [N] para aceptar o rechazar alianzas.",
            "Presiona [T] + (1-0) para transferir una carta a un aliado adyacente.",
            "",
            "¡Sobrevive y derrota al REY MAGO en el último nivel!",
            "",
            "[ESC] Volver al Menú Principal"
        };

        int y = altoVentana / 2 - 100;
        for (String linea : lineas) {
            int anchoLinea = g.getFontMetrics().stringWidth(linea);
            g.drawString(linea, (anchoVentana - anchoLinea) / 2, y);
            y += 25;
        }
    }

    
    // --- ESTE MÉTODO FUE COMPLETAMENTE REDISEÑADO ---
    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer) {

        if (p == null) return; 

        int anchoVentana = g.getClipBounds().width;
        
        // --- Dibuja el HUD del Jugador ---
        int hudX = 20;
        int hudY = 20;
        
        // --- CAMBIO DE TAMAÑO ---
        int hudWidth = 170; // <-- TU NUEVO ANCHO
        int hudHeight = 55;  // Altura base para Nombre + HP
        if (p.getVidaEscudo() > 0) {
            hudHeight += 25; // Altura extra si hay escudo
        }
        
        // Caja de fondo semi-transparente
        g.setColor(new Color(30, 30, 30, 200)); // Fondo oscuro
        g.fillRoundRect(hudX, hudY, hudWidth, hudHeight, 15, 15);
        g.setColor(colorCajaBorde);
        g.drawRoundRect(hudX, hudY, hudWidth, hudHeight, 15, 15);

        // --- Dibuja el contenido del HUD ---
        g.setFont(fontInfo);
        
        // --- REORGANIZADO ---
        // Nombre del Jugador
        g.setColor(Color.WHITE); 
        g.drawString(p.getNombre().toUpperCase(), hudX + 15, hudY + 30);
        
        // HP del Jugador
        g.setColor(Color.RED);
        g.drawString("HP: " + p.getVida(), hudX + 15, hudY + 50); // 20px más abajo
        
        int logY = hudY + 75; // Posición para el escudo

        // Escudo (si tiene)
        if (p.getVidaEscudo() > 0) {
            g.setColor(Color.CYAN);
            g.drawString("ESCUDO: " + p.getVidaEscudo(), hudX + 15, logY);
            logY += 25; // Mover la posición del log de abajo
        }

        // Posición Y para el Log (debajo del HUD)
        int actionY = hudY + hudHeight + 20; // 20px de padding

        // --- Dibuja el Log (flotante, sin caja) ---
        g.setFont(fontInfo);
        int logCount = 0;
        for (int i = battleLog.size() - 1; i >= 0 && logCount < 3; i--) {
            // Dibuja una sombra negra para el texto
            g.setColor(Color.BLACK);
            g.drawString(battleLog.get(i), hudX + 1, actionY + (logCount * 20) + 1);
            // Dibuja el texto verde
            g.setColor(Color.GREEN);
            g.drawString(battleLog.get(i), hudX, actionY + (logCount * 20));
            logCount++;
        }
        actionY += (logCount * 20) + 10; // Espacio después del log


        // --- Dibuja el Mini-Mapa ---
        dibujarMiniMapa(g, admin, p.getPosZ(), anchoVentana, g.getClipBounds().height);


        // --- Dibuja las Acciones Contextuales (Alianzas, Atacar, etc.) ---
        g.setFont(fontInfo);
        
        if (admin != null) {
            Personaje proponente = admin.getPropuestaPara(p);
            if (proponente != null) {
                g.setColor(Color.YELLOW);
                String texto = "Propuesta de alianza de " + proponente.getNombre() + " - [Y] Aceptar  [N] Rechazar";
                g.drawString(texto, hudX, actionY);
                actionY += 20;
            } else {
                if (jugadores != null) {
                    for (Personaje otro : jugadores) {
                        if (otro == null || otro == p || otro.getVida() <= 0) continue;
                        if (!p.estaAliadoCon(otro) && admin.sonAdyacentes(p, otro)) {
                            g.setColor(Color.CYAN);
                            String texto = "[L] Proponer alianza con " + otro.getNombre();
                            g.drawString(texto, hudX, actionY);
                            actionY += 20;
                            break;
                        }
                    }
                }
            }
        }
        if (jugadores != null) {
            for (Personaje otro : jugadores) {
                if (otro == null || otro == p || otro.getVida() <= 0) continue;
                if (p != null && admin != null && admin.sonAdyacentes(p, otro)) {
                    g.setColor(Color.RED);
                    String txt = "[F] Atacar a " + otro.getNombre();
                    g.drawString(txt, hudX, actionY);
                    actionY += 20;
                    break;
                }
            }
        }
        if (pendingTransfer == -2) {
            g.setColor(Color.MAGENTA);
            String txt = "TRANSFERIR: presioná 1..0 para elegir el slot a transferir.";
            g.drawString(txt, hudX, actionY);
        }
        
        // --- Dibuja Jugadores Eliminados (Esquina inferior izquierda) ---
        if (admin != null) {
            java.util.List<String> eliminados = admin.getJugadoresEliminados();
            if (eliminados != null && !eliminados.isEmpty()) {
                g.setColor(Color.RED);
                int elimX = 20;
                int elimY = g.getClipBounds().height - (eliminados.size() * 20) - PanelJuego.ALTURA_HOTBAR - 10;
                g.drawString("Eliminados:", elimX, elimY);
                elimY += 20;
                for (String name : eliminados) {
                    g.drawString("- " + name, elimX, elimY);
                    elimY += 18;
                }
            }
        }
    }

    private Enemigo encontrarEnemigoMasCercano(Personaje jugador, List<Enemigo> enemigos) {
        Enemigo masCercano = null;
        int menorDistancia = Integer.MAX_VALUE;

        if (jugador == null || enemigos == null) return null;

        int pX = jugador.getPosX();
        int pY = jugador.getPosY();
        int pZ = jugador.getPosZ();

        for (Enemigo enemigo : enemigos) {
            if (enemigo.estaVivo() && enemigo.getPosZ() == pZ) {
                int dist = Math.abs(enemigo.getPosX() - pX) + Math.abs(enemigo.getPosY() - pY);
                if (dist < menorDistancia) {
                    menorDistancia = dist;
                    masCercano = enemigo;
                }
            }
        }
        return masCercano;
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
        // Muestra los últimos 2 mensajes del log de batalla
        int logCountBatalla = 0;
        for (int i = battleLog.size() - 1; i >= 0 && logCountBatalla < 2; i--) {
            g.drawString(battleLog.get(i), logX, logMsgY + (logCountBatalla * 25));
            logCountBatalla++;
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
        // Fondo de la caja (estilo Pokémon)
        g.setColor(colorCajaUI);
        g.fillRoundRect(x, y, ancho, alto, 15, 15);
        
        // Borde
        g.setColor(colorCajaBorde);
        g.drawRoundRect(x, y, ancho, alto, 15, 15);
        
        g.setColor(Color.BLACK);
        g.setFont(fontCombateInfo);
        g.drawString(nombre, x + 15, y + 30);
        dibujarBarraHP(g, x + 15, y + 55, vida);
    }

    /**
     * pre: g no es null, x e y son posiciones válidas.
     * post: Dibuja una barra de HP estilo Pokémon.
     */
    private void dibujarBarraHP(Graphics g, int x, int y, int vida) {
        int anchoBarra = 200;
        int altoBarra = 15;
        
        // Etiqueta "HP:"
        g.setFont(fontInfo);
        g.setColor(Color.BLACK);
        g.drawString("HP:", x, y);

        // Fondo de la barra
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        int vidaActual = Math.max(0, vida); 
        int anchoVida = (int) (anchoBarra * (vidaActual / 100.0)); // Asumimos 100 de vida máx por ahora
        
        // TODO: Usar vidaMaxima de la entidad
        // int anchoVida = (int) (anchoBarra * ((double)vidaActual / vidaMaxima));

        // Color de vida
        if (vidaActual > 50) {
            g.setColor(Color.GREEN);
        } else if (vidaActual > 20) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x + 40, y - 12, anchoVida, altoBarra);
        
        // Borde
        g.setColor(Color.BLACK);
        g.drawRect(x + 40, y - 12, anchoBarra, altoBarra);
        
        // Texto de vida (ej. "69/69")
        g.setFont(fontInfo);
        g.drawString(vidaActual + "/100", x + anchoBarra + 50, y);
    } 
    
    // --- NUEVO MÉTODO ---
    /**
     * pre: g no es null, admin no es null, nivelZ es válido.
     * post: Dibuja un mini-mapa en la esquina superior derecha.
     */
    private void dibujarMiniMapa(Graphics g, AdministradorDeJuego admin, int nivelZ, int anchoVentana, int altoVentana) {
        if (admin == null || admin.getTablero() == null) {
            return;
        }
        
        Tablero tablero = admin.getTablero();
        Personaje jugador = admin.getJugadorActual();
        
        int mapWidth = 200; // Ancho de la caja del minimapa
        int mapHeight = 150; // Alto de la caja del minimam
        int mapX = anchoVentana - mapWidth - 20; // Pos X (esquina derecha)
        int mapY = 20;                           // Pos Y (esquina arriba)
        int dotSize = 4; // Tamaño de cada "pixel" del mapa

        // Fondo de la caja del minimapa
        g.setColor(new Color(30, 30, 30, 200)); // Fondo oscuro
        g.fillRoundRect(mapX, mapY, mapWidth, mapHeight, 15, 15);
        g.setColor(colorCajaBorde);
        g.drawRoundRect(mapX, mapY, mapWidth, mapHeight, 15, 15);
        
        // Título
        g.setFont(fontInfo);
        g.setColor(Color.WHITE);
        g.drawString("NIVEL " + (nivelZ + 1), mapX + 10, mapY + 20); // (Mostramos 1 en lugar de 0)

        // Calcular offset para centrar los puntos dentro de la caja
        int mapContentWidth = tablero.getDimensionX() * dotSize;
        int mapContentHeight = tablero.getDimensionY() * dotSize;
        int offsetX = mapX + (mapWidth - mapContentWidth) / 2;
        int offsetY = mapY + 25 + (mapHeight - 25 - mapContentHeight) / 2; // 25px para el título

        for (int x = 0; x < tablero.getDimensionX(); x++) {
            for (int y = 0; y < tablero.getDimensionY(); y++) {
                
                Casillero c = tablero.getCasillero(x, y, nivelZ);
                if (c.isVisitado()) {
                    // Dibuja el casillero visitado
                    switch (c.getTipo()) {
                        case VACIO:
                            g.setColor(colorPlataformaBatalla); // Marrón piso
                            break;
                        case RAMPA:
                            g.setColor(Color.YELLOW);
                            break;
                        case AGUA:
                            g.setColor(Color.BLUE);
                            break;
                        case ROCA:
                            g.setColor(Color.GRAY);
                            break;
                    }
                    g.fillRect(offsetX + x * dotSize, offsetY + y * dotSize, dotSize, dotSize);
                
                } 
                // Si no está visitado, no se dibuja nada (queda el fondo oscuro)
            }
        }
        
        // Dibujar al jugador (punto blanco brillante)
        if (jugador != null && jugador.getPosZ() == nivelZ) {
            g.setColor(Color.WHITE);
            g.fillRect(offsetX + jugador.getPosX() * dotSize, offsetY + jugador.getPosY() * dotSize, dotSize, dotSize);
        }
    }

}