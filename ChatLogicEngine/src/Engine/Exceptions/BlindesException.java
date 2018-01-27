package Engine.Exceptions;

public class BlindesException extends Exception{

    public static final String CANNOT_CREATE = " -> cannot create Blindes";
    public static final String SMALL_NOT_A_NUMBER = "Small must be a number"+CANNOT_CREATE;
    public static final String BIG_NOT_A_NUMBER = "Big must be a number"+CANNOT_CREATE;
    public static final String ADDITIONS_NOT_A_NUMBER = "Additions must be a number"+CANNOT_CREATE;
    public static final String NEGATIVE_MAX_TOTAL_NOT_A_NUMBER = "Max total rounds must be a number"+CANNOT_CREATE;

    public static final String NEGATIVE_SMALL = "Small cannot be negative"+CANNOT_CREATE;
    public static final String NEGATIVE_BIG = "Big cannot be negative"+CANNOT_CREATE;
    public static final String NEGATIVE_ADDITIONS = "Additions cannot be negative"+CANNOT_CREATE;
    public static final String NEGATIVE_MAX_TOTAL_ROUNDS = "Max total rounds cannot be negative"+CANNOT_CREATE;

    public static final String SMALL_BIGGER_THEN_SMALL="Small cannot be bigger than Big";
    public static final String BUY_SMALLER_THEN_SMALL="Buy cannot be smaller than Big";
    public static final String MAX_BIG_BIGGER_THEN_BUY="Max big blind cannot be bigger than Buy/2";

    public static final String ILLEGAL_BLINDES="Illegal Blindes";

    public BlindesException(String message)
    {
        super(message);
    }

}
