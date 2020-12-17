package app.network.connections;

import app.network.messages.Message;

import java.io.IOException;

public interface Connection {

    void send(Message message) throws IOException;

    Message get() throws IOException;

    boolean isConnected();

}
