package app.network.connections;

import app.model.Session;
import app.network.messages.Message;
import app.network.interfaces.MessageListener;
import app.services.LoggerService;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

import static app.services.LoggerService.*;

/**
 * Class , for keeping sockets
 *            ||
 *            V
 *  [server][sc] <---> [cc][client]
 *
 */
public class ClientSocketConnection implements Connection {

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private MessageListener messageListener;
//    private String sessionUsername = "";
//    public int sessionClicksCount;
    private Session session;

    public ClientSocketConnection(Socket socket,MessageListener messageListener) {
        this.socket = socket;
        try {
            this.session = new Session();
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
            this.messageListener = messageListener;
            startObserver();
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    private void startObserver(){
        Thread thread = new Thread(() -> {
            try{
                while(isConnected()){
                    int amount = in.available();
                    if (amount != 0) {
                        ObjectInputStream objIn = new ObjectInputStream(in);
                        Message msg = (Message) objIn.readObject();
                        Platform.runLater(()->{
                            messageListener.handleMessage(msg,this);
                        });

                    } else {
                        Thread.sleep(200);
                    }
                }
            } catch (InterruptedException | IOException | ClassNotFoundException e) {
                throw new IllegalStateException(e.getMessage());
                //todo: replace catch clause
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    //send message to the client
    @Override
    public void send(Message message) throws IOException {
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(message);
            objOut.flush();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    //get message from client
    @Override
    public Message get() throws IOException {
        try {
            int amount = in.available();
            System.out.println(amount);
            if (amount != 0) {
                ObjectInputStream objIn = new ObjectInputStream(in);
                return (Message) objIn.readObject();
            }
            return null;
        }
        catch (ClassNotFoundException e){
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    public Session getSession(){
        return this.session;
    }

    // fixme
    public String getSessionUsername() {
        return session.getUsername();
    }

    // fixme
    public void setSessionUsername(String sessionUsername) {
        this.session.setUsername(sessionUsername);
        println(level.INFO.name(),"client","Has been set session username: " + sessionUsername);
    }

    public void setSessionUserClicks(int clicks) {
        this.session.setClicksCount(clicks);
    }

    public void setSessionRoomClicks(int clicks) {
        this.session.setClicksCount(clicks);
    }

    public int getSessionUserClicks() {
        return this.session.getClicksCount();
    }

    public int getSessionRoomClicks() {
        return this.session.getRoomClicksCount();
    }
}
