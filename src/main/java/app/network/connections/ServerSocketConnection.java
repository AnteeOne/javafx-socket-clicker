package app.network.connections;

import app.model.Session;
import app.network.messages.Message;
import app.network.interfaces.MessageListener;
import app.services.LoggerService;

import java.io.*;
import java.net.Socket;

import static app.services.LoggerService.println;

/**
 * Class , for keeping sockets
 *            ||
 *            V
 *  [server][sc] <---> [cc][client]
 *
 */
public class ServerSocketConnection implements Connection {

    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private MessageListener messageListener;
    private Session session;

    public ServerSocketConnection(Socket socket, MessageListener messageListener) {
        this.socket = socket;
        this.session = new Session();
        try {
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
                       messageListener.handleMessage(msg,this);

                   } else {
                       Thread.sleep(200);
                   }
               }
               println(LoggerService.level.INFO.name(),"server","Client has been disconnected");
           } catch (InterruptedException | IOException | ClassNotFoundException e) {
               throw new IllegalStateException(e);
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

    public String getSessionUsername() {
        return session.getUsername();
    }

    public void setSessionUsername(String sessionUsername) {
        session.setUsername(sessionUsername);
    }

    public int getRoomId(){
        return session.getRoomId();
    }

    public void setRoomId(int id){
        session.setRoomId(id);
    }
}
