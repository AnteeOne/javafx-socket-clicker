package app.server.routers;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.interfaces.Router;
import app.network.messages.Message;
import app.server.controllers.auth.LoginController;
import app.server.controllers.auth.RegisterController;
import app.server.controllers.multiplayer.*;
import app.server.controllers.singleplayer.UserClicksGetController;
import app.server.controllers.singleplayer.UserClicksPutController;

import java.util.ArrayList;

public class MessageRouter implements Router {

    @Override
    public void routeMessage(Message message, Connection connection,ArrayList<ServerSocketConnection> connections) {
        ArrayList headers = message.getContent();
        switch (message.getType()){
            case REGISTER:{
                new RegisterController(connection).run(headers);
                break;
            }
            case LOGIN:{
                new LoginController(connection).run(headers);
                break;
            }
            case MESSAGE:{
                new MessageController(connection).runBroadcast(headers,connections);
                break;
            }
            case USER_CLICKS_GET:{
                new UserClicksGetController(connection).run(headers);
                break;
            }
            case USER_CLICKS_PUT:{
                new UserClicksPutController(connection).run(headers);
                break;
            }
            case ROOM_CONNECT:{
                new RoomConnectController(connection).runBroadcast(headers,connections);
                break;
            }
            case ROOM_DISCONNECT:{
               new RoomDisconnectController(connection).runBroadcast(headers,connections);
               break;
            }
            case ROOM_START:{
                new RoomStartController(connection).runBroadcast(headers,connections);
                break;
            }
            case ROOM_BOSS_LEAVE:{
                new LeaveBossController(connection).runBroadcast(headers,connections);
                break;
            }
            case ROOM_BOSS_CHOOSE:{
                new ChooseBossController(connection).runBroadcast(headers,connections);
                break;
            }
            case ROOM_DATA_UPDATE:{
                new RoomUpdateController(connection).runBroadcast(headers,connections);
                break;
            }
            case DISCONNECT:{
                new DisconnectController(connection).runBroadcast(headers,connections);
                break;
            }

            default:;
        }
    }
}
