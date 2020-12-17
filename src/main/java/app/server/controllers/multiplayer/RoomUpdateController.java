package app.server.controllers.multiplayer;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.ObjectSocketMessage;
import app.server.controllers.Controller;
import app.server.controllers.IBroadcastSender;
import app.server.rooms.RoomsRepository;
import app.services.LoggerService;

import java.io.IOException;
import java.util.ArrayList;

import static app.services.LoggerService.println;

public class RoomUpdateController extends Controller implements IBroadcastSender {

    private RoomsRepository roomsRepository;

    enum Codes{
        ROOM_UPDATE_OK,
        ROOM_UPDATE_ERROR
    }


    public RoomUpdateController(Connection connection) {
        super(connection);
    }

    /***
     * @param headers
     * Getting message from client with current headers:
     *  -  headers[0] - room_id
     * Sending message to the client with current headers:
     *  - header[0] - response message status (to ENUM statuses)
     *  - header[1] - room_id (to int)
     ***/

    @Override
    protected void run(ArrayList<String> headers) {

    }

    @Override
    public void runBroadcast(ArrayList<String> headers , ArrayList<ServerSocketConnection> connections) {

        roomsRepository = RoomsRepository.getInstance();
        Integer roomId = Integer.parseInt(headers.get(0));

        try {
            ArrayList content = new ArrayList();
            content.add(Codes.ROOM_UPDATE_OK.name());
            content.add(roomId);
            ObjectSocketMessage response = new ObjectSocketMessage(MessageTypes.STATUS , content);

            for (ServerSocketConnection mcConnection:connections) {
                if(mcConnection.getRoomId() == roomId && mcConnection!=getConnection()){
                    mcConnection.send(response);
                }
            }
        }
        catch (IOException e){
            println(LoggerService.level.ERROR.name(),"server","Unable to update clicks in  Room-" + roomId);
        }

    }
}
