package Engine.Players;

import Engine.DeckOfCards.Card;
import Engine.GameManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class PokerPlayer implements Serializable,Cloneable {

    private PlayerType type;
    private PlayerState state;
    private int id;
    private String name;
    private boolean folded;  //whether or not a player has folded
    private int bet;        //The amount that a player is betting in one session of betting

    private boolean isMyTurn;

    private int chips;      //The amount of money that a player has
    private int numbersOfBuy;

    private Card[] holeCards ;
    private int handsWon;
    private boolean checkOccurred = false;
    private int initialAmount;
    private String style="-fx-background-color: #CCAA99;";
    private String action;
    private int actionInfo;
    private int equity;
    private int winnigPrice;

    PokerPlayer(int playerID){

        chips=0;
        bet =0;
        initialAmount=0;
        folded=false;
        isMyTurn=false;
        numbersOfBuy=0;
        handsWon=0;
        winnigPrice=0;
        state=PlayerState.NONE;
        id=playerID;

        holeCards= new Card[2];
        for (int i=0; i<2; i++)
            holeCards[i]=new Card();
    }

    PokerPlayer(int playerID, String playerName){

        chips=0;
        bet =0;
        initialAmount=0;
        folded=false;
        isMyTurn=false;
        numbersOfBuy=0;
        handsWon=0;
        state=PlayerState.NONE;
        id=playerID;
        name=playerName;

        holeCards= new Card[2];
        for (int i=0; i<2; i++)
            holeCards[i]=new Card();
    }

    public int getWinnigPrice()
    {
        return winnigPrice;
    }

    public void setWinnigPrice(int price)
    {
        winnigPrice=winnigPrice+price;
    }

    public void setInitialAmount(int initialAmount) {
        this.initialAmount = initialAmount;
    }

    public void itIsMyTurn() {
        isMyTurn = true;
    }

    public void itIsNotMyTurn() {
        isMyTurn = false;
    }

    public int getId() {
        return id;
    }

    public String getName(){return name;}

    public int getChips() {
        return chips;
    }

    abstract public String play();
    abstract public int getRaise(int min, int max);

    public void setState (PlayerState state) {this.state=state;}
    void setType(PlayerType type)
    {
        this.type=type;
    }
    public void buy(int amount) {
        addChips(amount);
        numbersOfBuy++;
    }

    public void addChips (int amount)
    {
        chips=chips+amount;
    }

    public PlayerState getState() {
        return state;
    }

    public List<String> listOfDetails() {
        List<String> list = new ArrayList<>();
        list.add("Type: "+this.type.getType());
        list.add("ID: "+this.getId());
        if (initialAmount==0)
            list.add("State: "+this.state.getState());
        else
            list.add("State: "+this.state.getState()+" ("+initialAmount+")");

        if (chips<1)
            list.add("Chips: 0");
        else
            list.add("Chips: "+this.chips);

        list.add("Buys: "+this.numbersOfBuy);
        list.add("Hands won: "+this.handsWon+"/"+ GameManager.handNumber);
        return list;
    }

    public List<String> listOfDetailesForHand()
    {
        List<String> list = new ArrayList<>();
        if (isMyTurn)
            list.add("Type: "+this.type.getType() + " ***");
        else
            list.add("Type: "+this.type.getType());

        list.add("ID: "+this.getId());
        if (initialAmount==0)
            list.add("State: "+this.state.getState());
        else
            list.add("State: "+this.state.getState()+" ("+initialAmount+")");

        if (this.bet==0)
            list.add("Bet: ??");
        else
            list.add("Bet: "+this.bet);

        if (chips<1)
            list.add("Chips: 0");
        else
            list.add("Chips: "+this.chips);

        if (isFolded())
            list.add("***Folded***");


        return list;
    }

    public void setCard(Card c,int index)
    {
        if (index< 2 && index>=0)
            holeCards[index]=c;

    }

    public void collectBet()
    {
        if ((chips-bet)<0) {
            //System.out.println("We have bug!!! Bet is bigger than chips");
            chips=0;
        }
        else
            chips=chips-bet;
    }

    public int getBet() {
        return bet;
    }

    public String getHoleCards()
    {
        return holeCards[0] + " " +holeCards[1];
    }


    public String[] getCardsAsStringArray()
    {
        String[] cards = new String[2];
        cards[0]=holeCards[0].toString();
        cards[1]=holeCards[1].toString();

        return cards;

    }
    public void setBet(int newBet)
    {
        // Checks to make sure the bet is valid
        if (newBet < 0) {
            System.out.println("Invalid bet detected");
        }
        else {
            bet = newBet;
        }

    }


    public void setFolded(boolean status)
    {
        folded=status;
    }

    public boolean isFolded() {
        return folded;
    }

    public PlayerType getType() {
        return type;
    }

    public void isAWinner()
    {
        handsWon++;
    }

    public boolean getCheckOccurred() {
        return checkOccurred;
    }

    public void setCheckOccurred(boolean checkOccurred) {
        this.checkOccurred = checkOccurred;
    }

    public String toString()
    {
        return String.valueOf(this.id);
    }

    public int getNumbersOfBuy() {
        return numbersOfBuy;
    }

    public int getHandsWon() {
        return handsWon;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String newStyle)
    {
        style=newStyle;
    }

    @Override
    public final PokerPlayer clone() {
        try {
            return (PokerPlayer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public int getAdditionalActionInfo() {
        return actionInfo;
    }

    public String getPlayerSelection() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAdditionalActionInfo(int info) {
        actionInfo=info;
    }

    public void clearSelection() {
        this.action= "NOT SELECTED";
        this.actionInfo=0;
    }

    abstract public String getSelection(List<String> options);

    public int getEquity()
    {
        return equity;
    }

    public void setEquity(int newEquity)
    {
        this.equity=newEquity;
    }
}
