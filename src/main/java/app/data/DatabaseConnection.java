package app.data;

import app.services.LoggerService;
import app.settings.DatabaseSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static app.services.LoggerService.println;


public class DatabaseConnection {

    private static DatabaseConnection instance;

    private Connection connection = null;

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName(DatabaseSettings.DATABASE_DRIVER);
            String url = DatabaseSettings.DATABASE_URL;
            String password = DatabaseSettings.DATABASE_PASSWORD;
            String username = DatabaseSettings.DATABASE_USERNAME;
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            println(LoggerService.level.ERROR.name(),"server","Database connection creation failed");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

}
