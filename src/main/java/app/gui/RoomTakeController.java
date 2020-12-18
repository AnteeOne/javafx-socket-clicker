package app.gui;

import app.client.InterfaceHandler;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class RoomTakeController {
    public GameGUI parent;

    @FXML
    public TextField roomInput;

    public int score;

    public void init(GameGUI parent) {
        this.parent = parent;
        this.score = InterfaceHandler.getInstance(this.parent).getSessionUserClicks();
    }

    public void toMenu() {
        parent.toMenu();
    }

    public void roomConnect() {
        if (roomInput.getText().matches("[-+]?\\d+")) {
            connect(roomInput.getText());
        }
    }

    // отправляем сообщение о коннекте к комнате
    public void connect(String roomId) {
        ArrayList<String> roomInfo = new ArrayList<>();
        roomInfo.add(roomId);
        SocketMessage message = new SocketMessage(MessageTypes.ROOM_CONNECT, roomInfo);
        InterfaceHandler.getInstance(parent).getSession().setRoomId(Integer.parseInt(roomId)); // в сессии теперь roomId
        InterfaceHandler.getInstance(parent).interfaceService.sendMessage(message);
    }
}
