package app.server.controllers.multiplayer;

import app.data.repositories.UserRepository;
import app.model.User;
import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.ObjectSocketMessage;
import app.server.controllers.Controller;
import app.server.controllers.IBroadcastSender;
import app.server.rooms.RoomsRepository;
import app.services.LoggerService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.services.LoggerService.println;

public class RoomDisconnectController extends Controller implements IBroadcastSender {

    private RoomsRepository roomsRepository;

    public RoomDisconnectController(Connection connection) {
        super(connection);
    }

    public enum Codes {
        ROOM_DISCONNECT_OK,
        ROOM_DISCONNECT_BROADCAST,
        ROOM_DISCONNECT_ERROR
        //todo
    }


    /***
     * @param headers
     * Getting message from client with current headers:
     *  -  headers[0] - room_id
     * Sending message to the client with current headers:
     *  - header[0] - response message status (to ENUM statuses)
     *  - header[1] - room_id (to int)
     *  - header[2] - Current room (to room)
     *
     ***/

    @Override
    public void run(ArrayList<String> headers) {
    }

    @Override
    public void runBroadcast(ArrayList<String> headers , ArrayList<ServerSocketConnection> connections) {
        try {
            roomsRepository = RoomsRepository.getInstance();

            Integer roomId = Integer.parseInt(headers.get(0));
            User user = UserRepository.getInstance().getByUsername(getConnection().getSessionUsername());

            if (roomsRepository.roomIsExists(roomId)
                    && roomsRepository.getRoom(roomId).containsUser(user.getUsername())){
                roomsRepository.deleteUserFromRoom(roomId,user); // fixme
                println(LoggerService.level.INFO.name(),"server","User " + user.getUsername() + " has disconnected from the Room-" + roomId);
                getConnection().setRoomId(-1); // fixme
            }

            ArrayList content = new ArrayList();
            content.add(RoomDisconnectController.Codes.ROOM_DISCONNECT_OK.name());
            content.add(roomId);
            content.add(roomsRepository.getRoom(roomId));

            System.out.println(roomsRepository.getRoom(roomId).roomUsers.toString());
            ObjectSocketMessage response = new ObjectSocketMessage(MessageTypes.STATUS,content);

            getConnection().send(response);

            content.set(0, Codes.ROOM_DISCONNECT_BROADCAST.name());
            for (ServerSocketConnection mcConnection:connections) {
                if(mcConnection.getRoomId() == roomId){ // fixme: тсылается всем, кроме вышедшего юзера
                    mcConnection.send(response);
                }
            }

        } catch (IOException e) {
            println(LoggerService.level.ERROR.name(),"server","Error with sending response " +
                    "message from RoomDisconnectController to the client");
        }
        catch (SQLException e){
            println(LoggerService.level.ERROR.name() , "server" , "Error with getting user from database");
        }
    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
