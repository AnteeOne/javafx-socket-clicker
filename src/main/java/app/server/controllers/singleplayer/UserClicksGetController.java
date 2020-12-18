package app.server.controllers.singleplayer;

import app.data.repositories.UserRepository;
import app.model.User;
import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import app.server.controllers.Controller;
import app.services.LoggerService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static app.services.LoggerService.println;

public class UserClicksGetController extends Controller
{
    public UserClicksGetController(Connection connection) {
        super(connection);
    }

    public enum Codes{
        DATA_OK,
        DATA_ERROR
    }

    @Override
    public void run(ArrayList<String> headers) {
        try {
            User user = UserRepository.getInstance().getByUsername(headers.get(0));
            ArrayList<String> data = new ArrayList<>();
            data.add(Codes.DATA_OK.name());
            data.add(user.getUsername());
            data.add(String.valueOf(user.getClicksCount()));
            getConnection().send(new SocketMessage(MessageTypes.USER_CLICKS_GET,data));
        } catch (SQLException e) {
            println(LoggerService.level.ERROR.name(),"server","Unable to get clicks from database for " + headers.get(0));
        } catch (IOException e) {
            println(LoggerService.level.ERROR.name(),"server","Unable to send getting clicks status message for " + headers.get(0));
        }
    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
