package app.gui;

import app.client.InterfaceHandler;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;

import java.util.ArrayList;

public class MenuController {
    private GameGUI parent;

    public void init(GameGUI parent) {
        this.parent = parent;
    }

    public void single() {
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(InterfaceHandler.getInstance(this.parent).getSession().getUsername());
        SocketMessage message = new SocketMessage(MessageTypes.USER_CLICKS_GET,userInfo);
        InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(message);
    }

    public void toRoomInput() {
        this.parent.toRoomInput();
    }

    public void toLogin() {
        this.parent.toLogin();
    }
}
