package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Session {

    private String username;
    private int roomId;
    private int clicksCount;
    private int roomClicksCount;

    public Session() {
        roomId = -1;
        username = "";
        clicksCount = -1;
        roomClicksCount = -1;
    }
}
