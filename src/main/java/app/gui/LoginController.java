package app.gui;

import app.client.InterfaceHandler;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class LoginController {
    private GameGUI parent;

    @FXML
    public TextField userLogin;

    @FXML
    public TextField userPass;

    public void init(GameGUI parent) {
        this.parent = parent;
    }

    public void login() {
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(userLogin.getText());
        userInfo.add(userPass.getText());
        SocketMessage message = new SocketMessage(MessageTypes.LOGIN, userInfo);
        InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(message);
    }

    public void toSignUp() {
        this.parent.toSignUp();
    }
}
