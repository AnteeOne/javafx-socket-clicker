package app.data;

import app.services.LoggerService;
import app.settings.DatabaseSettings;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static app.services.LoggerService.println;


public class DatabaseConnection {

    private static DatabaseConnection instance;

    private Connection connection = null;

    private DatabaseConnection() throws SQLException {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("db.properties"));
            Class.forName(properties.getProperty("db.jdbc.driver-class-name"));
            String url = properties.getProperty("db.jdbc.url");
            String password = properties.getProperty("db.jdbc.password");
            String username = properties.getProperty("db.jdbc.username");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            println(LoggerService.level.ERROR.name(),"server","Database connection creation failed");
        } catch (IOException e) {
            println(LoggerService.level.ERROR.name(),"server","Error with reading database properties");
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
