package app.server.controllers.singleplayer;

import app.data.repositories.UserRepository;
import app.model.User;
import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.server.controllers.Controller;

import static app.services.LoggerService.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserClicksPutController extends Controller {

    public UserClicksPutController(Connection connection) {
        super(connection);
    }

    @Override
    public void run(ArrayList<String> headers) {
        User user = new User(headers.get(0),Integer.parseInt(headers.get(1)));
        try {
            UserRepository.getInstance().updateUserClicks(user);
            println(level.INFO.name(),"server","Have been updated clicks for user " + user.getUsername());
        } catch (SQLException e) {
            println(level.ERROR.name(),"server","Error with updating clicks for user " + user.getUsername());
        }
    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
