package app.server.controllers.multiplayer;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.ObjectSocketMessage;
import app.server.controllers.Controller;
import app.server.controllers.IBroadcastSender;
import app.server.rooms.RoomsRepository;

import static app.services.LoggerService.*;

import java.io.IOException;
import java.util.ArrayList;

public class RoomStartController extends Controller implements IBroadcastSender {

    public RoomStartController(Connection connection) {
        super(connection);
    }

    private RoomsRepository roomsRepository;

    enum Codes{

        ROOM_START_OK,
        ROOM_START_ERROR
    }

    /***
     * @param headers
     * Getting message from client with current headers:
     *  -  headers[0] - room_id
     * Sending message to the client with current headers:
     *  - header[0] - response message status (to ENUM statuses)
     *  - header[1] - room_id (to int)
     *  - header[2] - room (to room)
     ***/

    @Override
    protected void run(ArrayList<String> headers) {

    }

    @Override
    public void runBroadcast(ArrayList<String> headers , ArrayList<ServerSocketConnection> connections) {

        roomsRepository = RoomsRepository.getInstance();
        Integer roomId = Integer.parseInt(headers.get(0));

        try {
            roomsRepository.getRoom(roomId).setRunning(true);   // change running state of the room

            ArrayList content = new ArrayList();
            content.add(RoomStartController.Codes.ROOM_START_OK.name());
            content.add(roomId);
            content.add(roomsRepository.getRoom(roomId));
            ObjectSocketMessage response = new ObjectSocketMessage(MessageTypes.STATUS , content);

            for (ServerSocketConnection mcConnection:connections) {
                if(mcConnection.getRoomId() == roomId){
                    mcConnection.send(response);
                }
            }
        }
        catch (IOException e){
            println(level.ERROR.name(),"server","Unable to start Room-" + roomId);
        }
    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
