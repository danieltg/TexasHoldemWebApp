package Engine.DeckOfCards;

public enum Suit {
    CLUB("C"),
    DIAMOND("D"),
    HEART("H"),
    SPADE("S"),
    NA("?");

    private String suitChar;

    Suit(String suit) {
        this.suitChar = suit;
    }

    public String getSuit() {
        return this.suitChar;
    }

}