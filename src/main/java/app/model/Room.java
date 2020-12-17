package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class Room implements Serializable {
    public ArrayList<User> roomUsers;
    public int roomId;
    public boolean isRunning;


    public Room(int id){
        this.roomUsers = new ArrayList<>();
        this.roomId = id;
        this.isRunning = false;
    }

    public boolean containsUser(String username) {
        return roomUsers.stream().map(User::getUsername).collect(Collectors.toList()).contains(username);
    }
}
