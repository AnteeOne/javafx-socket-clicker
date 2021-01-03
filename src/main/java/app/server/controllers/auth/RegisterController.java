package app.server.controllers.auth;

import app.data.repositories.UserRepository;
import app.model.User;
import app.network.connections.Connection;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import app.server.controllers.Controller;
import app.services.LoggerService;

import static app.services.UserValidationService.*;
import static app.services.LoggerService.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RegisterController extends Controller {


    public RegisterController(Connection connection) {
        super(connection);
    }

    @Override
    public void run(ArrayList<String> headers) {
        try {
            User user = new User(
                    headers.get(0),
                    headers.get(1),
                    headers.get(2)
            );

            ArrayList<String> data = new ArrayList<>();

            switch (validateUserForRegistration(user)) {
                case REGISTER_OK: {
                    UserRepository.getInstance().add(user);
                    data.add(ErrorCode.REGISTER_OK.name());
                    println(level.INFO.name(), "server", "New user has been registered");
                    getConnection().send(new SocketMessage(MessageTypes.STATUS, data));
                    break;
                }
                case REGISTER_USER_ALREADY_EXISTS: {
                    data.add(ErrorCode.REGISTER_USER_ALREADY_EXISTS.name());
                    getConnection().send(new SocketMessage(MessageTypes.STATUS, data));
                    break;
                }
                case REGISTER_PASSWORDS_DIDNT_MATCH: {
                    data.add(ErrorCode.REGISTER_PASSWORDS_DIDNT_MATCH.name());
                    getConnection().send(new SocketMessage(MessageTypes.STATUS, data));
                    break;
                }
                case REGISTER_INCORRECT_DATA: {
                    data.add(ErrorCode.REGISTER_INCORRECT_DATA.name());
                    getConnection().send(new SocketMessage(MessageTypes.STATUS, data));
                    break;
                }
            }
        } catch (SQLException e) {
            println(LoggerService.level.ERROR.name(),"server","Error with getting "+ headers.get(0) + " from database for sign up");
        } catch (IOException e) {
            println(LoggerService.level.ERROR.name(),"server","Error with getting "+ headers.get(0) + " from database for sign up");

        } catch (IndexOutOfBoundsException e) {
            println(level.ERROR.name(), "server", "Incorrect message has been accepted");
            ArrayList<String> statusData = new ArrayList<String>();
            statusData.add(ErrorCode.REGISTER_INCORRECT_DATA.name());
            try {
                getConnection().send(new SocketMessage(MessageTypes.STATUS, statusData));
            } catch (IOException ex) {
                println(LoggerService.level.ERROR.name(),"server","Error with sending status message from register controller");;
            }
        }


    }
}
