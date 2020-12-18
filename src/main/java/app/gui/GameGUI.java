package app.gui;

import app.client.InterfaceHandler;
import app.client.InterfaceRouter;
import app.model.Boss;
import app.model.Info;
import app.model.Room;
import app.network.messages.Message;
import app.network.messages.MessageTypes;
import app.network.messages.SocketMessage;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.stage.WindowEvent;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;

public class GameGUI extends Application implements UI {
    private BossesController bosses;
    private BossController currentBoss;
    public RoomController currentRoom;
    private RoomTakeController roomTake;
    private RegistrationController registrationPage;
    private LoginController loginPage;

    public static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/login.fxml"));
        Parent root = loader.load();
        loginPage = loader.getController();
        loginPage.init(this);

        primaryStage.setTitle("RPG clicker");
        primaryStage.setScene(new Scene(root));

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event){

                ArrayList<String> userInfo = new ArrayList<>();
                userInfo.add(InterfaceHandler.getInstance(GameGUI.this).getSession().getUsername());
                SocketMessage message = new SocketMessage(MessageTypes.USER_CLICKS_GET,userInfo);
                InterfaceHandler.getInstance(GameGUI.this).interfaceService.sendMessage(message);

                try {
                    stop();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });

        primaryStage.show();
    }

    // routes
    public void toSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/sign-up.fxml"));
            Parent root = loader.load();
            registrationPage = loader.getController();
            registrationPage.init(this);
            primaryStage.setTitle("Registration");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void toLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/login.fxml"));
            Parent root = loader.load();
            loginPage = loader.getController();
            loginPage.init(this);
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void toMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/assets/menu.fxml"));
            primaryStage.setTitle("Menu");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void toBoss(Boss boss, int roomId) {
        if (boss.access <= InterfaceHandler.getInstance(this).getSessionUserClicks()
            || boss.access <= InterfaceHandler.getInstance(this).getSession().getRoomClicksCount()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/bosses/boss.fxml"));
                Parent root = loader.load();
                currentBoss = loader.getController();
                currentBoss.init(this, boss.name, boss.health, boss.viewPath, roomId);
                primaryStage.setTitle("Boss");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void toBoses() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/bosses.fxml"));
            Parent root = loader.load();
            bosses = loader.getController();
            bosses.init(this, null);
            primaryStage.setTitle("Bosses");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void toBoses(Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/bosses.fxml"));
            Parent root = loader.load();
            bosses = loader.getController();
            bosses.init(this, room);
            primaryStage.setTitle("Bosses");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void toRoomInput() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/room-take.fxml"));
            Parent root = loader.load();
            roomTake = loader.getController();
            roomTake.init(this);
            primaryStage.setTitle("Room");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void toRoom(int roomId, Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/room.fxml"));
            Parent root = loader.load();
            currentRoom = loader.getController();
            currentRoom.init(this, roomId, room);
            primaryStage.setTitle("Room");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // actions
    public void single() {
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(InterfaceHandler.getInstance(this).getSession().getUsername());
        SocketMessage message = new SocketMessage(MessageTypes.USER_CLICKS_GET,userInfo);
        InterfaceHandler.getInstance(this).interfaceService.sendMessage(message);
    }


    // TODO: remove
    public void toBosesFromBoss(){
        // updateClicks в BossConroller'e
        toBoses();
    }

    @Override
    public void getAnswer(Message message) {
        messageHandler(message);
    }

    public void messageHandler(Message message) {
        // handle here messages from the backend
        Info infoFormServer = InterfaceRouter.getRoute(message, InterfaceHandler.getInstance(this));
        switch(infoFormServer.getRoute()){
            case "menu":{
                toMenu();
                break;
            }
            case "signUp": {
                toSignUp();
                break;
            }
            case "login": {
                toLogin();
                break;
            }
            case "single": {
                toBoses();
                break;
            }
            case "room": {
                // брать из объекта
                toRoom((Integer) infoFormServer.getPayload().get(1), (Room) infoFormServer.getPayload().get(2));
                break;
            }
            // multiplayer
            case "bosses":
            case "leaveBossRoom": {

                toBoses((Room) infoFormServer.getPayload().get(2));
                break;
            }
            case "toBossFromRoom": {
                toBoss((Boss) infoFormServer.getPayload().get(2), (Integer) infoFormServer.getPayload().get(1));
                break;
            }
            case "updateBossData": {
                this.currentBoss.updateBossData();
                break;
            }
            default: {
                toSignUp();
            }
        }
    }
}
