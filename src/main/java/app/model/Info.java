package app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Setter
@Getter
public class Info<T> {
    private String route;
    private ArrayList<Object> payload;

    public Info(String route) {
        this.route = route;
    }
}
