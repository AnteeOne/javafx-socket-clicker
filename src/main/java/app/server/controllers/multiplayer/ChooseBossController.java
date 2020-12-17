package app.server.controllers.multiplayer;

import app.model.Boss;
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

public class ChooseBossController extends Controller implements IBroadcastSender {

    private RoomsRepository roomsRepository;


    public ChooseBossController(Connection connection) {
        super(connection);
    }

    enum Codes {
        ROOM_CHOOSE_BOSS_OK,
        ROOM_CHOOSE_BOSS_ERROR
    }

    /***
     * @param headers
     * Getting message from client(ROOM_BOSS_CHOOSE) with current headers:
     *  -  headers[0] - room_id
     *  -  headers[1] - boss health
     *  -  headers[2] - boss name
     *  -  headers[3] - boss view path
     *  -  headers[4] - boss access
     * Sending message to the client with current headers:
     *  - header[0] - response message status (to ENUM statuses)
     *  - header[1] - room_id (to int)
     *  - header[2] - boss object (to Boss)
     ***/

    @Override
    protected void run(ArrayList<String> headers) {
    }

    @Override
    public void runBroadcast(ArrayList<String> headers , ArrayList<ServerSocketConnection> connections) {

        roomsRepository = RoomsRepository.getInstance();
        Integer roomId = Integer.parseInt(headers.get(0));

        try {

            Boss boss = new Boss(
                    Integer.parseInt(headers.get(1)) ,
                    headers.get(2) ,
                    headers.get(3) ,
                    Integer.parseInt(headers.get(4))

            );

            ArrayList content = new ArrayList();
            content.add(Codes.ROOM_CHOOSE_BOSS_OK.name());
            content.add(roomId);
            content.add(boss);
            ObjectSocketMessage response = new ObjectSocketMessage(MessageTypes.STATUS , content);
            for (ServerSocketConnection mcConnection : connections) {
                if (mcConnection.getRoomId() == roomId) {
                    mcConnection.send(response);
                }
            }

        } catch (IOException e) {
            println(LoggerService.level.ERROR.name() , "server" , "Unable to choose boss in Room-" + roomId);
        }

    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
