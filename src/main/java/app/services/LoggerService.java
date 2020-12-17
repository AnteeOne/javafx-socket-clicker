package app.services;

public class LoggerService {

    public static enum level {
        INFO,
        WARNING,
        DEBUG,
        ERROR
    }

    public static void println(String level, String tag, String message) {
        System.out.println("[" + level + "](" + tag + "): " + message);
    }
}
