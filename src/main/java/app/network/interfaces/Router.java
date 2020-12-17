package app.network.interfaces;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.Message;

import java.util.ArrayList;

public interface Router {

    void routeMessage(Message message, Connection connection, ArrayList<ServerSocketConnection> connections);

}
