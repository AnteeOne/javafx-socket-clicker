package app.server.controllers;

import app.network.connections.Connection;

import java.util.ArrayList;

public abstract class Controller {

    private Connection connection;

    public Controller(Connection connection) {
        this.connection = connection;
    }

    protected abstract void run(ArrayList<String> headers);

    public Connection getConnection() {
        return connection;
    }
}
