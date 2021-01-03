package app.gui;

import app.client.InterfaceHandler;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class RegistrationController {
    private GameGUI parent;

    @FXML
    public TextField login;

    @FXML
    public TextField password;

    @FXML
    public TextField passwordRepeat;

    public void init(GameGUI parent) {
        this.parent = parent;
    }

    public void signUp() {
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(login.getText());
        userInfo.add(password.getText());
        userInfo.add(passwordRepeat.getText());
        SocketMessage message = new SocketMessage(MessageTypes.REGISTER, userInfo);
        InterfaceHandler.getInstance(this.parent).interfaceService.sendMessage(message);
    }

    public void toLogin() {
        this.parent.toLogin();
    }
}
