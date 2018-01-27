package Engine.DeckOfCards;

import java.io.Serializable;

public class Card implements Serializable {

    private Rank rank;
    private Suit suit;

    public  Card()
    {
        this.rank=Rank.NA;
        this.suit=Suit.NA;
    }
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank.getRank() +""+ suit.getSuit();
    }
}
