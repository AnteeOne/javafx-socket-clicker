package app.server.controllers.multiplayer;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import app.server.controllers.Controller;

import static app.services.LoggerService.*;

import java.io.IOException;
import java.util.ArrayList;

public class MessageController extends Controller {

    public MessageController(Connection connection) {
        super(connection);
    }

    @Override
    public void run(ArrayList headers) {
        println(level.INFO.name(),"server","New message has been accepted: " + headers.get(1));

    }

    public void runBroadcast(ArrayList<String> headers, ArrayList<ServerSocketConnection> connections){
        println(level.INFO.name(),"server","New broadcast message has been accepted: " + headers.get(1));
        for (ServerSocketConnection con:connections) {
            try {
                con.send(new SocketMessage(MessageTypes.MESSAGE,headers));
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }

        }
    }
}
