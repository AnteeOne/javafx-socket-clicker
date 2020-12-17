package app.client;

import app.network.connections.Connection;
import app.network.messages.SocketMessage;

import java.io.IOException;

public class InterfaceService {
    private Connection connection;

    public InterfaceService(Connection conn) {
        this.connection = conn;
    }

    public void sendMessage(SocketMessage message){
        try {
            connection.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
