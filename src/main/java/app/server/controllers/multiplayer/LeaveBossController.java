package app.server.controllers.multiplayer;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.ObjectSocketMessage;
import app.server.controllers.Controller;
import app.server.controllers.IBroadcastSender;
import app.server.rooms.RoomsRepository;

import java.io.IOException;
import java.util.ArrayList;

public class LeaveBossController extends Controller implements IBroadcastSender {

    private RoomsRepository roomsRepository;

    public LeaveBossController(Connection connection) {
        super(connection);
    }

    enum Codes {
        ROOM_LEAVE_BOSS_OK,
        ROOM_LEAVE_BOSS_ERROR
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
            ArrayList content = new ArrayList();
            content.add(Codes.ROOM_LEAVE_BOSS_OK.name());
            content.add(roomId);
            ObjectSocketMessage response = new ObjectSocketMessage(MessageTypes.STATUS , content);
            for (ServerSocketConnection mcConnection : connections) {
                if (mcConnection.getRoomId() == roomId) {
                    mcConnection.send(response);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
