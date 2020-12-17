package app.client;

import app.gui.GameGUI;
import app.gui.UI;
import app.model.Session;
import app.network.connections.ClientSocketConnection;
import app.network.connections.Connection;
import app.network.interfaces.MessageListener;
import app.network.messages.Message;
import app.settings.NetworkSettings;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class InterfaceHandler implements MessageListener {

    private ClientSocketConnection connection;
    private static InterfaceHandler instance;
    public static Message currentMessage;
    public InterfaceService interfaceService;
    private UI ui;

    public InterfaceHandler(UI ui) {
        try {
            connection = new ClientSocketConnection(
                    new Socket(InetAddress.getLocalHost(), NetworkSettings.SERVER_PORT),
                    this);
            interfaceService = new InterfaceService(connection);
            this.ui = ui;
        } catch (IOException e) {
            e.printStackTrace();
            //todo: replace with logger
        }
    }

    public static InterfaceHandler getInstance(GameGUI ui){
        if(instance==null) instance = new InterfaceHandler(ui);
        return instance;
    }

    @Override
    public void handleMessage(Message message, Connection connection) {
        currentMessage = message;
        ui.getAnswer(message);
    }

    public void setSession(String username){
        this.connection.setSessionUsername(username);
    }

    public Session getSession(){
        return this.connection.getSession();
    }

    public int getSessionClicks(){
        return this.connection.getSession().getClicksCount();
    }

    public void setSessionClicks(int value){
        this.connection.getSession().setClicksCount(value);
    }
}
