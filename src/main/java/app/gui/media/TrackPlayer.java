package app.gui.media;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;

public class TrackPlayer {

    private MediaPlayer player;

    private final String SOUNDS_DIRECTORY = "src/main/resources/media/";
    private final double PLAYER_VOLUME = 0.05;

    public TrackPlayer(String fileName){
        this.player = new MediaPlayer(
                new Media(Paths.get(SOUNDS_DIRECTORY + fileName).toUri().toString()));
        this.player.setVolume(PLAYER_VOLUME);
        player.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void play(){
        player.play();
    }

    public void stop(){
        player.stop();
    }



}
