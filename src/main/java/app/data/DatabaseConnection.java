package app.data;

import app.settings.DatabaseSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


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
            System.err.println("Database Connection Creation Failed : " + ex.getMessage());
            ex.printStackTrace();
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
