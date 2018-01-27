package Engine.DeckOfCards;

public enum Rank {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A"),
    NA("?");

    private String rankChar;

    Rank(String rank) {
        this.rankChar = rank;
    }

    public String getRank() {
        return this.rankChar;
    }

}