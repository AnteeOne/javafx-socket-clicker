package app.network.messages;

import java.util.ArrayList;

public class SocketMessage implements Message<String> {

    private MessageTypes messageType;
    private ArrayList<String> content;

    public SocketMessage(MessageTypes messageType, ArrayList<String> content) {
        this.messageType = messageType;
        this.content = content;
    }

    @Override
    public MessageTypes getType() {
        return messageType;
    }

    @Override
    public ArrayList<String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "SocketMessage{" +
                "messageType=" + messageType +
                ", content=" + content +
                '}';
    }
}
