package app.gui;

import app.client.InterfaceHandler;
import app.model.Boss;
import app.model.Room;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class BossesController {
    private GameGUI parent; // replace by interface

    @FXML
    public Button backButton;

    @FXML
    public GridPane gridPane;

    public ArrayList<Boss> bosses;

    public Room currentRoom = null;

    public void init(GameGUI parent, Room room) {
        this.parent = parent;
        this.bosses = new ArrayList<>();
        Text score = new Text();
        this.currentRoom = room;

        int usersScore = 0;
        // если мультипллеер - считаем
        // проверка на нахождение в комнате
        if (InterfaceHandler.getInstance(this.parent).getSession().getRoomId() != -1 && room != null) {

                for (int i = 0; i < room.roomUsers.size(); i++) {
                    usersScore += room.roomUsers.get(i).getClicksCount();
                }

                InterfaceHandler.getInstance(this.parent).getSession().setRoomClicksCount(usersScore);

                score.setText("Your score: " + usersScore);
                backButton.setText("leave");
                backButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        parent.currentRoom.leave(String.valueOf(room.getRoomId()));
                    }
                });

        } else {
            // если сингл плеер
            score.setText("Your score: " + InterfaceHandler.getInstance(this.parent).getSession().getClicksCount());
            backButton.setText("menu");
            backButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    toMenu();
                }
            });
        }

        gridPane.add(score, 0, 0);

        Boss dino = new Boss(100, "Dino", "@../../images/dino-boss.jpg", 0);
        Boss fireMan = new Boss(500, "Fire", "@../../images/fire.jpg", 100);
        Boss devilMan = new Boss(1000, "Devil", "@../../images/red.jpg", 200);
        Boss sandMan = new Boss(5000, "Sandy", "@../../images/govno.jpg", 300);

        bosses.add(dino);
        bosses.add(devilMan);
        bosses.add(fireMan);
        bosses.add(sandMan);

        int i = 0;
        int j = 1;

        for (Boss boss : bosses) {
            GridPane bossPane = new GridPane();
            bossPane.setAlignment(Pos.CENTER);
            Text text = new Text();
            text.setTextAlignment(TextAlignment.CENTER);
            text.setText(boss.getName() + " (" + boss.getAccess() + ")");
            ImageView imageView = new ImageView(new Image(boss.getViewPath()));

            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    toBoss(boss);
                }
            });

            imageView.addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(160);
                }
            });

            imageView.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);
                }
            });

            imageView.setFitHeight(150);
            imageView.setFitWidth(200);
            bossPane.add(text, 0, 1);
            bossPane.add(imageView, 0, 0);
            gridPane.add(bossPane, i, j);
            i++;
            if (i % 2 == 0) {
                j+=2;
                i = 0;
            }
        }
    }

    public void toMenu() {
        this.parent.toMenu();
    }

    public void toBoss(Boss boss) {

        // multi
        if (currentRoom != null) {
            ArrayList<String> bossInfo = new ArrayList<>();

            bossInfo.add(String.valueOf(currentRoom.getRoomId()));
            bossInfo.add(String.valueOf(boss.health));
            bossInfo.add(boss.name);
            bossInfo.add(boss.viewPath);
            bossInfo.add(String.valueOf(boss.access));

            SocketMessage message = new SocketMessage(MessageTypes.ROOM_BOSS_CHOOSE, bossInfo);
            InterfaceHandler.getInstance(parent).interfaceService.sendMessage(message);
            this.parent.toBoss(boss, currentRoom.roomId);
        } else {
            // single
            this.parent.toBoss(boss, -1);
        }
    }
}
