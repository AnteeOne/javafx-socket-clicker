package app.network.messages;

import java.util.ArrayList;

public class ObjectSocketMessage implements Message<Object> {

    private MessageTypes messageType;
    private ArrayList<Object> content;

    public ObjectSocketMessage(MessageTypes messageType, ArrayList<Object> content) {
        this.messageType = messageType;
        this.content = content;
    }

    @Override
    public MessageTypes getType() {
        return messageType;
    }

    @Override
    public ArrayList<Object> getContent() {
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
