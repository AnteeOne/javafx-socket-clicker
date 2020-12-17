package app.gui;

import app.client.InterfaceHandler;
import app.gui.media.SoundPlayer;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BossController {
    private GameGUI parent; // replace by interface
    private int health;
    private int score;

    @FXML
    public Text bossName;

    @FXML
    public Text healhFieled;

    @FXML
    public Text scoreField;

    @FXML
    public ImageView bossView;

    public void init(GameGUI parent, String name, int health, String img) {
        this.parent = parent;
        this.bossName.setText(name);
        this.health = health;
        this.healhFieled.setText("Health: " + Integer.toString(health));
        this.score = InterfaceHandler.getInstance(this.parent).getSessionClicks();
        this.bossView.setImage(new Image(img));
    }

    public void click() {
        this.score++;
        InterfaceHandler.getInstance(this.parent).setSessionClicks(
                InterfaceHandler.getInstance(this.parent).getSessionClicks() + 1
        );

        if (this.health > 0) this.health--;
        scoreField.setText("Score: " + this.score);
        healhFieled.setText("Health: " + this.health);
        SoundPlayer.playSound("oh.wav");
    }

    public void toBosses() {
        this.updateUserClicks();
        this.parent.toBosesFromBoss();
    }

    public void updateUserClicks(){
        if (InterfaceHandler.getInstance(this.parent).getSession().getRoomClicksCount() != -1) { // room'a

            InterfaceHandler.getInstance(this.parent).getSession().setRoomClicksCount(
                    InterfaceHandler.getInstance(this.parent).getSession().getRoomClicksCount() + score
            );
        }
        ArrayList<String> data = new ArrayList<>();
        data.add(InterfaceHandler.getInstance(this.parent).getSession().getUsername());
        data.add(String.valueOf(score));
        InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(
                new SocketMessage(MessageTypes.USER_CLICKS_PUT,data)
        );
    }

}