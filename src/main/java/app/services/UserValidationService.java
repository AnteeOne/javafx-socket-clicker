package app.services;

import app.data.repositories.UserRepository;
import app.model.User;

import java.sql.SQLException;

public class UserValidationService {


    public enum ErrorCode {
        REGISTER_INCORRECT_DATA,
        REGISTER_PASSWORDS_DIDNT_MATCH,
        REGISTER_USER_ALREADY_EXISTS,
        REGISTER_OK,
        LOGIN_INCORRECT_DATA,
        LOGIN_OK
    }


    public static ErrorCode validateUserForRegistration(User user) throws SQLException {
        if (user.getUsername().equals("")) return ErrorCode.REGISTER_INCORRECT_DATA;
        if (!user.getPassword().equals(user.getPassword2())) return ErrorCode.REGISTER_PASSWORDS_DIDNT_MATCH;
        if(UserRepository.getInstance().isExists(user)) return ErrorCode.REGISTER_USER_ALREADY_EXISTS;
        return ErrorCode.REGISTER_OK;

    }

    public static ErrorCode validateUserForLogin(User user) throws  SQLException {
        if (user.getUsername().equals("")
                || user.getPassword().equals("")
                || !UserRepository.getInstance().isExistsForLogin(user))
            return ErrorCode.LOGIN_INCORRECT_DATA;
        return ErrorCode.LOGIN_OK;
    }
}
