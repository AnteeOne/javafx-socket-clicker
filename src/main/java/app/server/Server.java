package app.server;

import app.network.connections.Connection;
import app.network.connections.ServerSocketConnection;
import app.network.messages.Message;
import app.network.interfaces.MessageListener;
import app.server.rooms.RoomsRepository;
import app.server.routers.MessageRouter;
import app.settings.NetworkSettings;

import static app.services.LoggerService.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements MessageListener {

    private  ArrayList<ServerSocketConnection> connections;
    private RoomsRepository rooms;
    private ServerSocket serverSocket;
    private MessageRouter messageRouter;

    public Server(){
        init();
    }

    private void init(){
        connections = new ArrayList<>();
        rooms = RoomsRepository.getInstance();
        messageRouter = new MessageRouter();
        try {
            serverSocket = new ServerSocket(NetworkSettings.SERVER_PORT);
        } catch (IOException e) {
            println(level.ERROR.name(),"server","Error with server initialisation ");
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }

    public void start(){
        println(level.INFO.name(),"server","Server is running!");
        while (true){
            try {
                Socket socket = serverSocket.accept();
                connections.add(new ServerSocketConnection(socket,this));
                println(level.INFO.name(),"server","New connection has been accepted");
            }
            catch (IOException e){
                println(level.ERROR.name(),"server","Error with trying to add new connection");
            }
        }

    }

    @Override
    public void handleMessage(Message message, Connection connection) {
        messageRouter.routeMessage(message,connection,connections);
    }

}
