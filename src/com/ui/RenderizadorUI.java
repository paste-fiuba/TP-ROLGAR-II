package com.ui;

import com.entidades.Enemigo;
import com.entidades.Entidad;
import com.entidades.Personaje;
import com.items.Inventario;
import com.logica.AdministradorDeCombate;
import com.logica.AdministradorDeJuego;
import com.logica.ControladorJuego;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderizador de UI principal. Centraliza fuentes, colores y delega
 * el dibujo de combate a RenderizadorCombate. Usa SpriteManager
 * para resolver sprites por enemigo/jugador en tiempo de ejecución.
 */
public class RenderizadorUI {

    private Font fontMenuTitulo, fontMenuOpcion, fontInfo, fontInstrucciones;
    private Font fontGameOver, fontCombate, fontCombateInfo;
    private List<String> battleLog;

    // Colores
    private Color colorCajaUI = new Color(248, 248, 248);
    private Color colorCajaBorde = new Color(80, 80, 80);
    private Color colorFondoBatallaCielo = new Color(20, 20, 20);
    private Color colorFondoBatallaPasto = new Color(40, 30, 20);
    private Color colorPlataformaBatalla = new Color(110, 80, 40);

    // Módulos delegados
    private RenderizarData renderData;
    private RenderizarMenu renderMenu;
    private RenderizarFinJuego renderFinJuego;
    private RenderizadorCombate renderCombate;
    private RenderizadorHUD renderHUD;

    // Sprite manager
    private SpriteManager spriteManager;
    // Backwards-compatible gerenciador (legacy)
    private GerenciadorDeSprites gerenciadorSprites;

    public RenderizadorUI() {
        this.fontMenuTitulo = new Font("Arial", Font.BOLD, 40);
        this.fontMenuOpcion = new Font("Arial", Font.PLAIN, 24);
        this.fontInfo = new Font("Arial", Font.BOLD, 16);
        this.fontGameOver = new Font("Arial", Font.BOLD, 100);
        this.fontInstrucciones = new Font("Arial", Font.PLAIN, 18);
        this.fontCombate = new Font("Arial", Font.BOLD, 28);
        this.fontCombateInfo = new Font("Arial", Font.BOLD, 22);
        this.battleLog = new ArrayList<>();

        this.renderData = new RenderizarData();
        this.renderMenu = new RenderizarMenu();
        this.renderFinJuego = new RenderizarFinJuego();

        this.spriteManager = new SpriteManager();

        // RenderizadorCombate is instantiated with fallback sprites/colors/fonts.
        this.renderCombate = new RenderizadorCombate(null, spriteManager.getDefaultEnemyBattle(),
                this.fontMenuOpcion, this.fontInfo, this.fontCombate, this.fontCombateInfo,
                this.colorCajaUI, this.colorCajaBorde, this.colorFondoBatallaCielo,
                this.colorFondoBatallaPasto, this.colorPlataformaBatalla);

        this.renderHUD = new RenderizadorHUD(this.fontInfo, this.colorCajaBorde, this.colorPlataformaBatalla);
    }

    /** Backwards-compatible constructor used by PanelJuego */
    public RenderizadorUI(GerenciadorDeSprites gerenciador) {
        this();
        this.gerenciadorSprites = gerenciador;
    }

    // Log utilities
    public void agregarMensajeLog(String mensaje) {
        this.battleLog.add(mensaje);
        if (this.battleLog.size() > 5) this.battleLog.remove(0);
    }

    public void limpiarLog() { this.battleLog.clear(); }

    // Delegated drawing
    public void dibujarHotbar(Graphics2D g, Inventario inventario, int anchoLogico, int altoJuegoLogico) {
        if (this.gerenciadorSprites != null) {
            this.renderData.dibujarHotbar(g, inventario, anchoLogico, altoJuegoLogico, this.gerenciadorSprites);
        } else {
            // If legacy gerenciador not provided, try to adapt SpriteManager via a small wrapper
            this.renderData.dibujarHotbar(g, inventario, anchoLogico, altoJuegoLogico, new GerenciadorDeSprites());
        }
    }

    public void dibujarMenuPausa(Graphics g, int anchoVentana, int altoVentana) {
        this.renderMenu.dibujarMenuPausa(g, anchoVentana, altoVentana, this.fontMenuTitulo, this.fontMenuOpcion);
    }

    public void dibujarPantallaGameOver(Graphics g, int anchoVentana, int altoVentana) {
        this.renderFinJuego.dibujarPantallaGameOver(g, anchoVentana, altoVentana, this.fontGameOver, this.fontMenuOpcion);
    }

    public void dibujarPantallaVictoria(Graphics g, int anchoVentana, int altoVentana) {
        this.renderFinJuego.dibujarPantallaVictoria(g, anchoVentana, altoVentana, this.fontGameOver, this.fontMenuOpcion);
    }

    public void dibujarMenu(Graphics g, ControladorJuego.GameState estado, int anchoVentana, int altoVentana) {
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

    public void dibujarInfoJuego(Graphics g, Personaje p, List<Enemigo> e, List<Personaje> jugadores, AdministradorDeJuego admin, int pendingTransfer) {
        Enemigo enemigoMasCercano = encontrarEnemigoMasCercano(p, e);
        this.renderData.dibujarInfoJuego(g, p, e, jugadores, admin, pendingTransfer, this.fontInfo, this.battleLog, enemigoMasCercano);
        if (admin != null && p != null) {
            int anchoVentana = g.getClipBounds().width;
            int altoVentana = g.getClipBounds().height;
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
        if (adminCombate == null) return;

        Personaje jugador = adminCombate.getJugador();
        Entidad oponente = adminCombate.getOponente();

        // Resolve sprites via SpriteManager
        BufferedImage spriteOponente = null;
        BufferedImage spriteJugador = null;
        if (oponente instanceof Enemigo) spriteOponente = spriteManager.getEnemyBattleSprite((Enemigo) oponente);
        else if (oponente instanceof Personaje) spriteOponente = spriteManager.getPlayerBattleSprite((Personaje) oponente);

        spriteJugador = spriteManager.getPlayerBattleSprite(jugador);

        // Delegate to RenderizadorCombate (pass resolved sprites)
        this.renderCombate.dibujarPantallaCombate(g, adminCombate, anchoVentana, altoVentana, this.battleLog, spriteJugador, spriteOponente);
    }

    private void dibujarCajaInfo(Graphics g, String nombre, int vida, int x, int y, int ancho, int alto) {
        // This method intentionally left as a thin wrapper; RenderizadorCombate uses its own.
    }
}