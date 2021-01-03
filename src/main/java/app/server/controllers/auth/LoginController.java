package app.server.controllers.auth;

import app.data.repositories.UserRepository;
import app.model.User;
import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import app.server.controllers.Controller;
import app.services.LoggerService;

import static app.services.UserValidationService.*;
import static app.services.LoggerService.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController extends Controller {

    public LoginController(Connection connection) {
        super(connection);
    }

    @Override
    public void run(ArrayList<String> headers) {
        try {
            User user = new User(headers.get(0),headers.get(1));
            ArrayList<String> data = new ArrayList<>();
            switch (validateUserForLogin(user)){
                case LOGIN_OK:{
                  User responseUser = UserRepository.getInstance().getByUsername(user.getUsername());
                  data.add(ErrorCode.LOGIN_OK.name());
                  data.add(responseUser.getUsername());
                  data.add(String.valueOf(responseUser.getClicksCount()));
                  getConnection().setSessionUsername(responseUser.getUsername());
                  getConnection().send(new SocketMessage(MessageTypes.STATUS,data));
                  println(level.INFO.name(), "server", getConnection().getSessionUsername() + " has been signed in");
                  break;
                }
                case LOGIN_INCORRECT_DATA:{
                    data.add(ErrorCode.LOGIN_INCORRECT_DATA.name());
                    getConnection().send(new SocketMessage(MessageTypes.STATUS,data));
                    break;
                }
            }
        } catch (SQLException e) {
            println(LoggerService.level.ERROR.name(),"server","Error with getting "+ headers.get(0) + " from database for sign in");
        }
        catch (IOException e){
            println(LoggerService.level.ERROR.name(),"server","Error with sending "+ headers.get(0) + " message  for sign in in client");
        }

    }

    @Override
    public ServerSocketConnection getConnection() {
        return (ServerSocketConnection) super.getConnection();
    }
}
