package Engine.Exceptions;

public class GameTypeException extends Exception{

    public static final String INVALID_VALUE = "Invalid value for GameType";

    public GameTypeException(String message)
    {
        super(message);
    }
}
