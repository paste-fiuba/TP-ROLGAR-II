package com.ui;

import javax.sound.sampled.*;
import java.io.File;

public class RenderizarMusica {
    private Clip clip;

    public void reproducir(String ruta) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY); 
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detener() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}