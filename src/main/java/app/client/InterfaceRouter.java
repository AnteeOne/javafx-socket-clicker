package app.client;

import app.model.Info;
import app.network.messages.Message;

public class InterfaceRouter {

    public static Info getRoute(Message message, InterfaceHandler interfaceController) {
        switch (message.getContent().get(0).toString()){
            case "REGISTER_OK":
            case "LOGIN_INCORRECT_DATA": {
                return new Info("login");
            }
            case "LOGIN_OK": {
                interfaceController.setSession((String) message.getContent().get(1));
                interfaceController.setSessionClicks(Integer.parseInt((String)message.getContent().get(2)));
                return new Info("menu");
            }
            case "REGISTER_USER_ALREADY_EXISTS":
            case "REGISTER_INCORRECT_DATA":
            case "REGISTER_PASSWORDS_DIDNT_MATCH": {
                return new Info("signUp");
            }
            case "ROOM_CONNECT_OK":
            case "ROOM_DISCONNECT_BROADCAST": {
                return new Info("room", message.getContent());
            }
            case "ROOM_DISCONNECT_OK": {
                return new Info("menu", message.getContent());
            }
            case "ROOM_START_OK": {
                return new Info("bosses", message.getContent());
            }
            case "ROOM_CHOOSE_BOSS_OK": {
                return new Info("toBossFromRoom", message.getContent());
            }
            case "ROOM_LEAVE_BOSS_OK": {
                return new Info("leaveBossRoom", message.getContent());
            }
            case "DATA_OK":{
                interfaceController.setSessionClicks(Integer.parseInt(message.getContent().get(2).toString()));
                return new Info("single");
            }
            case "DATA_ERROR":{

            }
            default: {
                return new Info("unknown");
            }
        }
    }
}
