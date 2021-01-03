package app.client;

import app.network.connections.Connection;
import app.network.messages.SocketMessage;
import app.services.LoggerService;

import java.io.IOException;

import static app.services.LoggerService.println;

public class InterfaceService {
    private Connection connection;

    public InterfaceService(Connection conn) {
        this.connection = conn;
    }

    public void sendMessage(SocketMessage message){
        try {
            connection.send(message);
        } catch (IOException e) {
            println(LoggerService.level.ERROR.name(),"client","Error with sending message to the server");
        }
    }
}
