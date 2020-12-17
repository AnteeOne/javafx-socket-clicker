package app.services;

import app.network.messages.Message;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;

import java.net.Socket;

public class MessageValidationService {

    public static boolean messageTypeIsValid(String receivedMessageType){
        for(MessageTypes messageType:MessageTypes.values()){
            if(messageType.name().equals(receivedMessageType)) return true;
        }
        return false;
    }

}
