package app.server.controllers.multiplayer;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.server.controllers.Controller;
import app.server.controllers.IBroadcastSender;
import app.server.controllers.singleplayer.UserClicksPutController;
import app.server.rooms.RoomsRepository;

import java.util.ArrayList;

public class DisconnectController extends Controller implements IBroadcastSender {

    private RoomsRepository roomsRepository;

    enum Codes {
        DISCONNECT_OK,
        DISCONNECT_ERROR
    }

    public DisconnectController(Connection connection) {
        super(connection);
    }

    /***
     * @param headers
     * Getting message from client with current headers:
     *  -  headers[0] - username
     *  -  headers[1] - room_id
     * Sending messages from controllers:
     *  - UserClicksPutController
     ***/

    @Override
    protected void run(ArrayList<String> headers) {

    }

    @Override
    public void runBroadcast(ArrayList<String> headers , ArrayList<ServerSocketConnection> connections) {
        
        Integer roomId = Integer.parseInt(headers.get(1));

        //update user's data before disconnecting
        new UserClicksPutController(getConnection()).run(headers);

        //deleting disconnected user from room
        if (roomId!=-1) {
            ArrayList<String> dcHeaders = new ArrayList<>();
            dcHeaders.add(String.valueOf(roomId));
            new RoomDisconnectController(getConnection()).runBroadcast(dcHeaders,connections);
        }

        //deleting disconnected user connection from list of all connections
        for (ServerSocketConnection connection:connections) {
            if(getConnection().getSessionUsername().equals(connection.getSessionUsername())){
                connections.remove(connection);
            }
        }

    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
