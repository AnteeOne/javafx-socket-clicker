package app.server.controllers;

import app.network.connections.ServerSocketConnection;

import java.util.ArrayList;

public interface IBroadcastSender {

    void runBroadcast(ArrayList<String> headers, ArrayList<ServerSocketConnection> connections);

}
