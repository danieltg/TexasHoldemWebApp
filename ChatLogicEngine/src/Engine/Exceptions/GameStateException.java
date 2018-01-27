package Engine.Exceptions;

public class GameStateException extends Exception {


    public static final String INVALID_VALUE = "Invalid selection ";

    public GameStateException(String message)
    {
        super(message);
    }
}
