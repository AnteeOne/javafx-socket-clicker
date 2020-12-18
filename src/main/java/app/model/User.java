package app.model;

import lombok.Setter;

import java.io.Serializable;

@Setter
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String password2;
    private int clicksCount;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String password2) {
        this.username = username;
        this.password = password;
        this.password2 = password2;
    }

    public User(String username, int clicksCount) {
        this.username = username;
        this.clicksCount = clicksCount;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }

    public int getClicksCount() {
        return clicksCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                '}';
    }
}
