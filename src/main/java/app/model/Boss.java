package app.model;

import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class Boss implements Serializable {
    public int health;
    public String name;
    public String viewPath;
    public int access;
}
