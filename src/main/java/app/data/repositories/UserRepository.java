package app.data.repositories;

import app.data.DatabaseConnection;
import app.model.User;
import app.services.LoggerService;
import app.settings.GameSettings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private static UserRepository userRepository;
    private DatabaseConnection databaseConnection;

    private PreparedStatement ps;
    private ResultSet rs;
    private Connection con;

    public static UserRepository getInstance(){
        if(userRepository == null) {
            try {
                userRepository = new UserRepository(DatabaseConnection.getInstance());
            } catch (SQLException e) {
                LoggerService.println(
                        LoggerService.level.ERROR.name(),
                        "database",
                        e.getMessage());
            }
        }
        return userRepository;
    }

    UserRepository(DatabaseConnection databaseConnection){
        this.databaseConnection = databaseConnection;
    }

    public void add(User user) throws SQLException {
        con = databaseConnection.getConnection();
        ps = con.prepareStatement("INSERT INTO  users (username,password,clicks) VALUES (?,?,?)");
        ps.setString(1,user.getUsername());
        ps.setString(2,user.getPassword());
        ps.setInt(3, GameSettings.CLICKS_ON_START);
        ps.execute();
    }



    public boolean isExists(User user) throws SQLException{
        String username = user.getUsername();
        con = databaseConnection.getConnection();
        ps = con.prepareStatement("SELECT users.username FROM users");
        rs = ps.executeQuery();
        while (rs.next()){
            String usernameSQL = rs.getString(1);
            if(username.equals(usernameSQL)){
                return true;
            }
        }
        return false;
    }

    public boolean isExistsForLogin(User user) throws SQLException {
        String username = user.getUsername();
        String password = user.getPassword();
        con = databaseConnection.getConnection();
        ps = con.prepareStatement("SELECT users.username,users.password FROM users");
        rs = ps.executeQuery();
        while (rs.next()){
            String usernameSQL = rs.getString(1);
            String passwordSQL = rs.getString(2);
            if(username.equals(usernameSQL) && password.equals(passwordSQL)){
                return true;
            }
        }
        return false;
    }

    public User getByUsername(String username) throws  SQLException {
        con = databaseConnection.getConnection();
        ps = con.prepareStatement("SELECT users.username,clicks FROM users WHERE username = ?");
        ps.setString(1,username);
        rs = ps.executeQuery();
        while (rs.next()){
            String usernameSQL = rs.getString(1);
            int clicksCountSQL = rs.getInt(2);
            return new User(usernameSQL,clicksCountSQL);
        }
        return new User("",0);
    }

    public void updateUserClicks(User user) throws SQLException {
        con = databaseConnection.getConnection();
        ps = con.prepareStatement("UPDATE users SET clicks = ? where username = ?");
        ps.setInt(1,user.getClicksCount());
        ps.setString(2,user.getUsername());
        ps.executeUpdate();

    }
}
