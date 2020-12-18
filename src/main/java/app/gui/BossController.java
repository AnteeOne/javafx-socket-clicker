package app.gui;

import app.client.InterfaceHandler;
import app.gui.media.SoundPlayer;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class BossController {
    private GameGUI parent; // replace by interface
    private int health;
    private int score;
    private String soundFileName;

    @FXML
    public Text bossName;

    @FXML
    public Text healhFieled;

    @FXML
    public Text scoreField;

    @FXML
    public ImageView bossView;

    public int roomId;

    public void init(GameGUI parent, String name, int health, String img, int roomId) {
        this.roomId = roomId;
        this.parent = parent;
        this.bossName.setText(name);
        this.health = health;
        this.healhFieled.setText("Health: " + Integer.toString(health));
        this.score = InterfaceHandler.getInstance(this.parent).getSessionUserClicks(); // взяли score юзера
        this.bossView.setImage(new Image(img));
        this.soundFileName = name;
    }

    public void click() {
        this.score++;
        InterfaceHandler.getInstance(this.parent).setSessionUserClicks(score);

        if (this.health > 0) this.health--;
        scoreField.setText("Score: " + this.score);
        healhFieled.setText("Health: " + this.health);
        SoundPlayer.playSound(soundFileName + ".wav");

        // multi-player
        if (roomId != -1) {
            ArrayList<String> data = new ArrayList<>();
            data.add(String.valueOf(roomId));
            InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(
                    new SocketMessage(MessageTypes.ROOM_DATA_UPDATE,data)
            );
        }
    }

    public void updateBossData() {
        this.score++;
        if (this.health > 0) this.health--;
        scoreField.setText("Score: " + this.score);
        healhFieled.setText("Health: " + this.health);
    }

    public void toBosses() {
        this.updateUserClicks();

        if (roomId != -1) {
            ArrayList<String> data = new ArrayList<>();
            data.add(String.valueOf(roomId));
            InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(
                    new SocketMessage(MessageTypes.ROOM_BOSS_LEAVE,data)
            );
            this.parent.toBoses();
        } else {
            this.parent.toBoses();
        }
    }

    public void updateUserClicks(){
        if (roomId != -1) { // room'a

            InterfaceHandler.getInstance(this.parent).setSessionUserClicks(
                    score
            );
        }
        ArrayList<String> data = new ArrayList<>();
        data.add(InterfaceHandler.getInstance(this.parent).getSession().getUsername());
        data.add(String.valueOf(InterfaceHandler.getInstance(this.parent).getSessionUserClicks()));
        InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(
                new SocketMessage(MessageTypes.USER_CLICKS_PUT,data)
        );
    }

}
