package app.network.interfaces;

import app.network.connections.Connection;
import app.network.messages.Message;

public interface MessageListener {

    public void handleMessage(Message message, Connection connection);

}
