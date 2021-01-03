package app.gui.media;

import app.services.LoggerService;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static app.services.LoggerService.println;

public class SoundPlayer {

    private static final String SOUNDS_DIRECTORY = "src/main/resources/media/";

    public static void playSound(String filename){
        try {
            File f = new File( SOUNDS_DIRECTORY + filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            println(LoggerService.level.ERROR.name(),"client","Error with playing sound"); }
    }

}
