package com.ui;

import com.entidades.Enemigo;
import com.entidades.Personaje;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * TDA que centraliza la carga y consulta de sprites del juego.
 * Permite obtener sprites específicos por nombre o tipo de enemigo.
 */
public class SpriteManager {

    private final Map<String, BufferedImage> spritesByKey;
    private BufferedImage defaultEnemyBattle;

    public SpriteManager() {
        this.spritesByKey = new HashMap<>();
        loadSpritesFromDir("src/sprites");
    }

    private void loadSpritesFromDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) return;

        File[] files = dir.listFiles((d, name) -> name.toLowerCase(Locale.ROOT).endsWith(".png")
                || name.toLowerCase(Locale.ROOT).endsWith(".jpg")
                || name.toLowerCase(Locale.ROOT).endsWith(".jpeg"));
        if (files == null) return;

        for (File f : files) {
            try {
                BufferedImage img = ImageIO.read(f);
                if (img == null) continue;
                String key = normalizeKey(f.getName().replaceAll("\\.png$|\\.jpg$|\\.jpeg$", ""));
                spritesByKey.put(key, img);
            } catch (IOException ex) {
                // Ignorar archivo si no se pudo leer
            }
        }

        // Guardar por separado un fallback si existe
        BufferedImage fb = spritesByKey.get("enemigo_batalla");
        if (fb == null) fb = spritesByKey.get("ogro");
        this.defaultEnemyBattle = fb;
    }

    private String normalizeKey(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD);
        n = n.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        n = n.toLowerCase(Locale.ROOT);
        n = n.replaceAll("\\s+", "_");
        n = n.replaceAll("[^a-z0-9_\\-]", "_");
        return n;
    }

    public BufferedImage getEnemyBattleSprite(Enemigo e) {
        if (e == null) return defaultEnemyBattle;
        // Prefer lookup by nombre, then by tipo
        String nombreKey = normalizeKey(e.getNombre());
        BufferedImage img = spritesByKey.get(nombreKey);
        if (img != null) return img;
        String tipoKey = normalizeKey(e.getTipo());
        img = spritesByKey.get(tipoKey);
        if (img != null) return img;
        return defaultEnemyBattle;
    }

    public BufferedImage getPlayerBattleSprite(Personaje p) {
        if (p == null) return null;
        // Try by player name, fallback to personaje_batalla or personaje
        BufferedImage img = spritesByKey.get(normalizeKey(p.getNombre()));
        if (img != null) return img;
        img = spritesByKey.get("personaje_batalla");
        if (img != null) return img;
        return spritesByKey.get("personaje");
    }

    public BufferedImage getDefaultEnemyBattle() { return defaultEnemyBattle; }
}
