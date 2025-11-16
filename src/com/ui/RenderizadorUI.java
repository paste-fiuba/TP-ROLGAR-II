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

<<<<<<< Updated upstream
    // --- Sprites (solo los que necesita esta clase) ---
    private BufferedImage spritePersonajeBatalla; 
    private BufferedImage spriteEnemigoBatalla;   
=======
    private BufferedImage spriteSlot;
    private BufferedImage spritePersonajeBatalla; // Sprite grande de personaje
    private BufferedImage spriteEnemigoBatalla;   // Sprite grande de enemigo (fallback)
    private SpriteManager spriteManager;
>>>>>>> Stashed changes

    private Font fontMenuTitulo, fontMenuOpcion, fontInfo, fontInstrucciones;
    private Font fontGameOver, fontCombate, fontCombateInfo; 
    private List<String> battleLog;
    
    // --- COLORES DE UI ---
    private Color colorCajaUI = new Color(248, 248, 248); 
    private Color colorCajaBorde = new Color(80, 80, 80);
    private Color colorFondoBatallaCielo = new Color(20, 20, 20); 
    private Color colorFondoBatallaPasto = new Color(40, 30, 20); 
    private Color colorPlataformaBatalla = new Color(110, 80, 40); 

    // --- MÓDULOS DELEGADOS ---
    private RenderizarData renderData;
    private RenderizarMenu renderMenu;
    private RenderizarFinJuego renderFinJuego;
    // --- Módulos que creé en pasos anteriores (RenderizadorCombate, RenderizadorHUD)
    // --- los integramos aquí para que todo funcione ---
    private RenderizadorCombate renderCombate; 
    private RenderizadorHUD renderHUD; 
    
    // --- GERENCIADOR DE SPRITES ---
    private GerenciadorDeSprites sprites; 

<<<<<<< Updated upstream
    /**
     * Constructor modificado: Recibe el Gerenciador de Sprites
     */
    public RenderizadorUI(GerenciadorDeSprites gerenciadorSprites) {
        this.sprites = gerenciadorSprites; // Guarda el gerenciador
        
        // Carga sprites específicos de UI desde el gerenciador
        this.spritePersonajeBatalla = sprites.getSpritePersonajeBatalla();
        this.spriteEnemigoBatalla = sprites.getSpriteEnemigoBatalla();
        
        // Carga de fuentes
=======
    public RenderizadorUI() {
        cargarSpritesUI();
        this.spriteManager = new SpriteManager();
>>>>>>> Stashed changes
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
        this.fontInfo = new Font("Arial", Font.BOLD, 16);
        this.fontGameOver = new Font("Arial", Font.BOLD, 100);
        this.fontInstrucciones = new Font("Arial", Font.PLAIN, 18);
        this.fontCombate = new Font("Arial", Font.BOLD, 28); 
        this.fontCombateInfo = new Font("Arial", Font.BOLD, 22); 
        this.battleLog = new ArrayList<>();
        
        // --- Inicializar los módulos DELEGADOS (los que tú creaste) ---
        this.renderData = new RenderizarData();
        this.renderMenu = new RenderizarMenu();
        this.renderFinJuego = new RenderizarFinJuego();
        
        // --- Inicializar los módulos que YO creé ---
        this.renderCombate = new RenderizadorCombate(
            this.spritePersonajeBatalla, this.spriteEnemigoBatalla,
            this.fontMenuOpcion, this.fontInfo, this.fontCombate, this.fontCombateInfo,
            this.colorCajaUI, this.colorCajaBorde, this.colorFondoBatallaCielo,
            this.colorFondoBatallaPasto, this.colorPlataformaBatalla
        );
        
        this.renderHUD = new RenderizadorHUD(
            this.fontInfo, 
            this.colorCajaBorde, 
            this.colorPlataformaBatalla
        );
    }

<<<<<<< Updated upstream
    // --- MÉTODO 'cargarSpritesUI' ELIMINADO ---
    // (Movido al constructor)
=======
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
        
        // El sprite específico de enemigo se resolverá en tiempo de combate usando SpriteManager.
        // Cargamos un fallback genérico si está disponible.
        try {
            this.spriteEnemigoBatalla = ImageIO.read(new File("src/sprites/enemigo_batalla.png"));
        } catch (IOException e) {
            try {
                this.spriteEnemigoBatalla = ImageIO.read(new File("src/sprites/ogro.png"));
            } catch (IOException e2) {
                this.spriteEnemigoBatalla = null;
            }
        }
    }
>>>>>>> Stashed changes

    // --- Métodos de Log (permanecen aquí) ---
    public void agregarMensajeLog(String mensaje) {
        this.battleLog.add(mensaje);
        if (this.battleLog.size() > 5) {
            this.battleLog.remove(0);
        }
    }

    public void limpiarLog() {
        this.battleLog.clear();
    }

    
    // --- Métodos Delegados ---

    /**
     * MÉTODO MODIFICADO: Ahora pasa el gerenciador de sprites a renderData.
     */
    public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico) {
        // Llama a la clase que tú creaste
        this.renderData.dibujarHotbar(g, inventario, anchoLogico, altoJuegoLogico, this.sprites);
    }

    public void dibujarMenuPausa(Graphics g, int anchoVentana, int altoVentana) {
        // Llama a la clase que tú creaste
        this.renderMenu.dibujarMenuPausa(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
    }

    public void dibujarPantallaGameOver(Graphics g, int anchoVentana, int altoVentana) {
        // Llama a la clase que tú creaste
    	this.renderFinJuego.dibujarPantallaGameOver(g, anchoVentana, altoVentana, this.fontGameOver, this.fontMenuOpcion);
    }

    public void dibujarPantallaVictoria(Graphics g, int anchoVentana, int altoVentana) {
        // Llama a la clase que tú creaste
        this.renderFinJuego.dibujarPantallaVictoria(g, anchoVentana, altoVentana, this.fontGameOver, this.fontMenuOpcion);
    }

    public void dibujarMenu(Graphics g, ControladorJuego.GameState estado, int anchoVentana, int altoVentana) {
        // Llama a la clase que tú creaste
        switch (estado) {
            case MENU_PRINCIPAL:
                this.renderMenu.dibujarMenuPrincipal(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            case MENU_DIFICULTAD:
                this.renderMenu.dibujarMenuDificultad(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            case MENU_JUGADORES:
                this.renderMenu.dibujarMenuJugadores(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
                break;
            case MENU_INSTRUCCIONES: 
                this.renderMenu.dibujarMenuInstrucciones(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontInstrucciones);
                break;
            default:
                break;
        }
    }

    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, java.util.List<Personaje> jugadores, com.logica.AdministradorDeJuego admin, int pendingTransfer) {
        // 1. Delegar el HUD de texto/data
        Enemigo enemigoMasCercano = encontrarEnemigoMasCercano(p, e);
        // Llama a la clase que tú creaste
        this.renderData.dibujarInfoJuego(g, p, e, jugadores, admin, pendingTransfer, this.fontInfo, this.battleLog, enemigoMasCercano);
        
        // 2. Delegar el Minimapa
        if (admin != null && p != null) {
            int anchoVentana = g.getClipBounds().width;
            int altoVentana = g.getClipBounds().height;
            // Llama a la clase que yo creé
            this.renderHUD.dibujarMiniMapa(g, admin, p.getPosZ(), anchoVentana, altoVentana);
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

    
    public void dibujarPantallaCombate(Graphics g, AdministradorDeCombate adminCombate, int anchoVentana, int altoVentana) {
<<<<<<< Updated upstream
        // Llama a la clase que yo creé
        this.renderCombate.dibujarPantallaCombate(g, adminCombate, anchoVentana, altoVentana, this.battleLog);
=======
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
        BufferedImage spriteOponente = null;
        if (oponente instanceof Enemigo) {
            spriteOponente = spriteManager.getEnemyBattleSprite((Enemigo) oponente);
        } else if (oponente instanceof Personaje) {
            spriteOponente = spriteManager.getPlayerBattleSprite((Personaje) oponente);
        }
        if (spriteOponente == null) spriteOponente = spriteEnemigoBatalla;
        if (spriteOponente != null) {
             g.drawImage(spriteOponente, 
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
>>>>>>> Stashed changes
    }
}