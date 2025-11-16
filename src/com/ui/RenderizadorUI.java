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

    // --- Sprites (solo los que necesita esta clase) ---
    private BufferedImage spritePersonajeBatalla; 
    private BufferedImage spriteEnemigoBatalla;   

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

    /**
     * Constructor modificado: Recibe el Gerenciador de Sprites
     */
    public RenderizadorUI(GerenciadorDeSprites gerenciadorSprites) {
        this.sprites = gerenciadorSprites; // Guarda el gerenciador
        
        // Carga sprites específicos de UI desde el gerenciador
        this.spritePersonajeBatalla = sprites.getSpritePersonajeBatalla();
        this.spriteEnemigoBatalla = sprites.getSpriteEnemigoBatalla();
        
        // Carga de fuentes
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

    // --- MÉTODO 'cargarSpritesUI' ELIMINADO ---
    // (Movido al constructor)

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
        // Llama a la clase que yo creé
        this.renderCombate.dibujarPantallaCombate(g, adminCombate, anchoVentana, altoVentana, this.battleLog);
    }
}