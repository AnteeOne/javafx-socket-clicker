package app.network.messages;

import java.io.Serializable;
import java.util.ArrayList;

public interface Message<T> extends Serializable {

    public abstract MessageTypes getType();

    public abstract ArrayList<T> getContent();
}
