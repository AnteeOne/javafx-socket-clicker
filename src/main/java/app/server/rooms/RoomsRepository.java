package app.server.rooms;

import app.model.Room;
import app.model.User;

import java.util.HashMap;

public class RoomsRepository {

    private static RoomsRepository instance;

    private HashMap<Integer, Room> rooms;

    private RoomsRepository(){
        rooms = new HashMap<>();
    }

    public static RoomsRepository getInstance() {
        if(instance == null) instance = new RoomsRepository();
        return instance;
    }

    public Room getRoom(Integer id){
        return rooms.get(id);
    }

    public Integer createRoom(Integer id){
        rooms.put(id,new Room(id));
        return id;
    }

    public boolean roomIsExists(Integer id){
        return rooms.containsKey(id);
    }

    public void addUserInRoom(Integer id, User user){
        rooms.get(id).roomUsers.add(user);
    }

    public void deleteUserFromRoom(Integer id , User user){
        for (int i = 0; i < rooms.get(id).roomUsers.size(); i++) {
            if (user.getUsername().equals(rooms.get(id).roomUsers.get(i).getUsername())) {
                rooms.get(id).roomUsers.remove(i);
                break;
            }
        }
    }
}
