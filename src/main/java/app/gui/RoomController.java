package app.gui;

import app.client.InterfaceHandler;
import app.model.Boss;
import app.model.Room;
import app.model.User;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class RoomController {
    private GameGUI parent; // replace by interface

    @FXML
    public GridPane gridPane;

    @FXML
    public Text roomIdField;
    public int roomId;
    public Room currentRoom;

    public ArrayList<User> users;

    public void init(GameGUI parent, int roomID, Room room) {
        this.parent = parent;
        this.users = room.roomUsers;
        this.roomId = roomID;
        this.currentRoom = room;
        this.roomIdField.setText("Current room id: " + roomID);

        int i = 0;
        int j = 2;

        for (User user : users) {
            GridPane usersPane = new GridPane();
            usersPane.setAlignment(Pos.CENTER);
            Text text = new Text();
            text.setTextAlignment(TextAlignment.CENTER);
            text.setText(user.getUsername() + " (" + user.getClicksCount() + ")");

            ImageView imageView = new ImageView(new Image("@../../images/user.png"));

            imageView.setFitHeight(150);
            imageView.setFitWidth(130);
            usersPane.add(text, 0, 1);
            usersPane.add(imageView, 0, 0);
            gridPane.add(usersPane, i, j);
            i++;
            if (i % 2 == 0) {
                j+=2;
                i = 0;
            }
        }
    }

    public void roomLeave() {
        leave(String.valueOf(roomId));
    }

    public void leave(String roomId) {
        ArrayList<String> roomInfo = new ArrayList<>();
        roomInfo.add(roomId);

        InterfaceHandler.getInstance(this.parent).getSession().setRoomClicksCount(-1);

        SocketMessage message = new SocketMessage(MessageTypes.ROOM_DISCONNECT, roomInfo);
        InterfaceHandler.getInstance(parent).interfaceService.sendMessage(message);
    }

    // fixme: отправлять сообщение нужного типа


    public void roomStart() {
        start(String.valueOf(roomId));
    }

    public void start(String roomId) {
        ArrayList<String> roomInfo = new ArrayList<>();
        roomInfo.add(roomId);
        SocketMessage message = new SocketMessage(MessageTypes.ROOM_START, roomInfo);
        InterfaceHandler.getInstance(parent).interfaceService.sendMessage(message);
        this.parent.toBoses();
    }
}
