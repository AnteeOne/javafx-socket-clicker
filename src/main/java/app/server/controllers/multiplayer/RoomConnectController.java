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

import static app.services.LoggerService.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomConnectController extends Controller implements IBroadcastSender {

    private RoomsRepository roomsRepository;

    public RoomConnectController(Connection connection) {
        super(connection);
    }

    public enum Codes {
        ROOM_CONNECT_OK,
        ROOM_CONNECT_ERROR
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
    public void runBroadcast(ArrayList<String> headers, ArrayList<ServerSocketConnection> connections){
        roomsRepository = RoomsRepository.getInstance();
        try {
            Integer roomId = Integer.parseInt(headers.get(0));
            User user = UserRepository.getInstance().getByUsername(getConnection().getSessionUsername());

            if (!roomsRepository.roomIsExists(roomId)) {
                roomsRepository.addUserInRoom(roomsRepository.createRoom(roomId) , user);
                println(level.INFO.name() , "server" , "Room-" + roomId + " has been created");
            } else {
                if (!roomsRepository.getRoom(roomId).containsUser(user.getUsername())) {
                    roomsRepository.addUserInRoom(roomId , user);
                }
            }
            println(level.INFO.name() , "server" , "User " + user.getUsername() + " has connected to the Room-" + roomId);
            getConnection().setRoomId(roomId);
            ArrayList content = new ArrayList();
            content.add(Codes.ROOM_CONNECT_OK.name());
            content.add(roomId);
            content.add(roomsRepository.getRoom(roomId));
            ObjectSocketMessage response = new ObjectSocketMessage(MessageTypes.STATUS , content);

            for (ServerSocketConnection mcConnection:connections) {
                if(mcConnection.getRoomId() == roomId){
                    mcConnection.send(response);
                }
            }
        } catch (IOException e) {
            println(level.ERROR.name() , "server" , "Error with sending response " +
                    "message from RoomConnectController to the client");
        }
        catch (SQLException e){
            println(level.ERROR.name() , "server" , "Error with getting user from database");
        }
    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
