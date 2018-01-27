package Engine;

import Engine.DeckOfCards.Card;
import Engine.DeckOfCards.Deck;
import Engine.DeckOfCards.Rank;
import Engine.DeckOfCards.Suit;
import Engine.GameDescriptor.PokerBlindes;
import Engine.Players.PokerPlayer;
import Engine.Players.PlayerState;
import Engine.Players.PlayerType;
import com.rundef.poker.EquityCalculator;
import com.rundef.poker.Hand;

import java.util.ArrayList;
import java.util.List;

import static Engine.HandState.*;

public class PokerHand {

    private int pot;    //the amount of money in the pot
    private Card[] tableCards;
    private int maxBet;
    private int numberOfPlayers;
    private List<PokerPlayer> players;
    private Deck deck;
    private PokerBlindes blinde;
    private int currentBet;   //Current bet amount that must be called/raised
    private int dealer;      //the index of the dealer
    private int b;
    private int s;
    private int round;
    private PokerPlayer lastRaise = null;
    private PokerPlayer nextToPlay = null;
    private String lastAction;
    private String lastActionBeforeFold;
    private int numberOfWinners;

    private int lastActionInfo;
    private int lastPlayerToPlay;
    private HandState state;
    private int big;
    private int small;

    public PokerHand(PokerBlindes gameBlinde, List<PokerPlayer> playersInHand) {
        pot = 0;
        currentBet = 0;
        round = 0;
        tableCards = new Card[5];
        deck = new Deck();

        for (int i = 0; i < 5; i++)
            tableCards[i] = new Card();

        lastAction = "N";
        lastActionBeforeFold="N";
        lastActionInfo = 0;
        lastPlayerToPlay = -999;
        numberOfWinners=0;

        numberOfPlayers = playersInHand.size();
        players = playersInHand;
        blinde = gameBlinde;
        big=gameBlinde.getBig();
        small=gameBlinde.getSmall();

        updateStateIndex();
        updateMaxBet();
        setNextToPlayForTheFirstTime();
    }

    public void setNextToPlayForTheFirstTime() {
        int i=0;

        for (PokerPlayer p : players)
        {
            i++;
            if (p.getState() == PlayerState.BIG) {
                nextToPlay = players.get(i % this.getNumberOfPlayers());
            }
        }

        nextToPlay.clearSelection();
    }


    public int getNumberOfWinners(){return numberOfWinners;}

    public void setHandState(HandState newState) {
        state = newState;
    }

    public int getPot() {
        return pot;
    }

    public void updateMaxBet() {

        int maxBetByPlayers = players.get(whoIsInTheGame()).getChips();

        //Here is the min of the players chips
        for (PokerPlayer p : players) {
            if (!p.isFolded() && maxBetByPlayers > p.getChips())
                maxBetByPlayers = p.getChips();
        }

        if (maxBetByPlayers <= 0)
            maxBet = 0;
        else if (pot <= maxBetByPlayers)
            maxBet = pot;
        else
            maxBet = maxBetByPlayers;

    }

    public String[] getCardsAsStringArray() {
        String[] cards = new String[5];

        cards[0] = tableCards[0].toString();
        cards[1] = tableCards[1].toString();
        cards[2] = tableCards[2].toString();
        cards[3] = tableCards[3].toString();
        cards[4] = tableCards[4].toString();


        return cards;
    }

    public String getCardsAsString() {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < 4; i++)
            s.append(tableCards[i].toString()).append(" | ");

        s.append(tableCards[4].toString());

        return s.toString();

    }

    public String printHand() {

        return getCardsAsString() +
                "  POT: " + pot;
    }

    public List<Winner> getWinner() {

        List<Winner> winnersList = new ArrayList<>();

        if (playersLeft() == 1) {
            int index = whoIsInTheGame();
            String cards = players.get(index).getHoleCards();
            Winner winner = new Winner(players.get(index), cards);
            winnersList.add(winner);
        }

        //Human player left the game
        else {
            for (PokerPlayer p : players) {
                if (!p.isFolded()) {
                    String cards = p.getHoleCards();
                    Winner winner = new Winner(p, cards);
                    winnersList.add(winner);
                }
            }
        }

        return winnersList;
    }


    private int whoIsInTheGame() {
        for (int i = 0; i < numberOfPlayers; i++)
            if (!players.get(i).isFolded())
                return i;

        return 0;
    }

    public void dealingRiverCard() {
        tableCards[4] = deck.drawCard();
        lastAction = "CARD";
        lastActionInfo = 0;
        lastPlayerToPlay = -999;
    }

    public void dealingTurnCard() {
        tableCards[3] = deck.drawCard();
        lastAction = "CARD";
        lastActionInfo = 0;
        lastPlayerToPlay = -999;
    }


    public List<Winner> evaluateRound() throws Exception {

        EquityCalculator calculator = new EquityCalculator();
        calculator.reset();
        StringBuilder tableCardsStr = new StringBuilder();

        for (Card c : tableCards) {
            if (c.getSuit() != Suit.NA && c.getRank() != Rank.NA)
                tableCardsStr.append(c.toString());
        }

        calculator.setBoardFromString(tableCardsStr.toString());

        for (PokerPlayer p : players) {
            if (!p.isFolded()) {
                String playerCards = (p.getHoleCards()).replaceAll("\\s+", "");
                Hand hand = Hand.fromString(playerCards);
                calculator.addHand(hand);
            }
        }
        calculator.calculate();
        return getWinners(calculator);
    }

    private List<Winner> getWinners(EquityCalculator calculator) {
        List<Winner> winnersList = new ArrayList<>();
        List<Integer> winningHands = calculator.getWinningHands();
        for (int index : winningHands) {
            PokerPlayer p = getActivePlayerInIndex(index);
            String handRanking = calculator.getHandRanking(index).toString();
            Winner tmp = new Winner(p, handRanking);
            winnersList.add(tmp);

        }
        return winnersList;
    }

    private PokerPlayer getActivePlayerInIndex(int index) {

        int i = 0;

        for (PokerPlayer p : players) {
            if (!p.isFolded()) {
                if (i == index)
                    return p;
                else
                    i++;
            }
        }

        return null;
    }

    public void resetPlayersBets() {
        for (PokerPlayer p : players) {
            p.setCheckOccurred(false);
            p.itIsNotMyTurn();
            p.setBet(0);
        }
    }

    public void dealingFlopCards() {

        for (int i = 0; i < 3; i++)
            tableCards[i] = deck.drawCard();

        lastAction = "CARD";
        lastActionInfo = 0;
        lastPlayerToPlay = -999;
    }


    // Deal cards to all players
    public void dealingHoleCards() {
        for (PokerPlayer p : players) {
            p.setFolded(false);
            for (int i = 0; i < 2; i++)
                p.setCard(deck.drawCard(), i);
        }
        lastAction = "PLAYER_CARDS";
        lastActionInfo = 0;
        lastPlayerToPlay = -999;
    }


    public void betSmall() {

        players.get(s).setBet(getSmall());
        pot = pot + getSmall();
        players.get(s).collectBet();

        lastAction = "B";
        lastActionInfo = getSmall();
        lastPlayerToPlay = players.get(s).getId();
    }

    public void betBig() {
        players.get(b).setBet(getBig());
        pot = pot + getBig();
        players.get(b).collectBet();

        lastAction = "R";
        lastActionInfo = getBig();
        lastPlayerToPlay = players.get(b).getId();
        setCurrentBet(getBig());
        setLastRaise(players.get(b));

    }

    public void updateStateIndex() {
        for (int i = 0; i < numberOfPlayers; i++) {
            if (players.get(i).getState() == PlayerState.DEALER)
                dealer = i;
            else if (players.get(i).getState() == PlayerState.SMALL)
                s = i;
            else if (players.get(i).getState() == PlayerState.BIG)
                b = i;
        }
    }

    public int playersLeft() {
        int count = 0;

        for (PokerPlayer p : players) {
            if (!p.isFolded())
                count++;
        }

        return count;

    }


    public List<PokerPlayer> getPlayers() {
        return players;
    }

    public int getRound() {
        return round;
    }

    public void setLastRaise(PokerPlayer lastRaisePlayer) {
        lastRaise = lastRaisePlayer;
    }

    public int getDealer() {
        return dealer;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setCurrentBet(int currbet) {
        currentBet = currbet;
    }

    public PokerBlindes getBlinde() {
        return blinde;
    }

    public PokerPlayer getLastRaise() {
        return lastRaise;
    }

    public void addToPot(int bet) {
        pot += bet;
    }

    public void upRound() {
        round++;
    }

    public void subFromPot(int bet) {
        pot = pot - bet;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public int getMaxBet() {
        return maxBet;
    }

    public boolean humanIsLeft() {
        for (PokerPlayer p : players) {
            if (p.isFolded() && p.getType() == PlayerType.Human)
                return true;
        }
        return false;
    }

    public boolean isAllCheckOccurred() {
        for (PokerPlayer p : players) {
            if (!p.getCheckOccurred())
                return false;
        }

        return true;
    }

    public void setMaxBet(int newMaxbet) {
        maxBet = newMaxbet;
    }

    public Card[] getTableCards() {
        return tableCards;
    }

    public void setLastAction(String s) {
        lastAction = s;
    }

    public void setLastActionInfo(int i) {
        lastActionInfo = i;
    }

    public void setLastPlayerToPlay(int id) {
        lastPlayerToPlay = id;
    }

    public String getLastAction() {
        return lastAction;
    }

    public int getLastActionInfo() {
        return lastActionInfo;
    }

    public int getLastPlayerToPlay() {
        return lastPlayerToPlay;
    }


    public PokerPlayer getNextToPlay() {
        return nextToPlay;
    }

    public void updateNextToPlay() {

        lastPlayerToPlay=-999;
        lastRaise=null;

        for (int i=0; i<numberOfPlayers; i++)
        {
            if (players.get(i).getState()==PlayerState.DEALER)
            {
                for (int k=1; k<=numberOfPlayers; k++)
                {
                    if (!players.get((i + k) % numberOfPlayers).isFolded())
                    {
                        nextToPlay = players.get((i + k)% this.numberOfPlayers);
                        nextToPlay.clearSelection();
                        return;
                    }
                }
            }
        }
    }



    public boolean doWeHaveMoreThanTwoActivePlayersInTheGame()
    {
        int count=0;

        for (PokerPlayer p: players)
        {
            if (!p.isFolded())
                count++;
        }

        return (count>1);
    }

    /**
     * To be called at the betting rounds, after the player has performed an
     * action.
     */

    public void afterPlayerAction() {

        switch (state) {
            case GameInit:
            {
                if (!doWeHaveMoreThanTwoActivePlayersInTheGame())
                {
                    System.out.println("Game over- we have only one player in the game");
                    state=END;
                }

                else if (nextToPlay==lastRaise || getMaxBet()==0)
                {
                    if (getMaxBet()==0)
                        System.out.println("Max bet is 0, we are going to open the Flop");
                    else
                        System.out.println("We finished the first betting round");
                    state=TheFlop;
                }
                break;

            }
            case bettingAfterFlop:
            {

                if (!doWeHaveMoreThanTwoActivePlayersInTheGame())
                {
                    System.out.println("Game over- we have only one player in the game");
                    state=END;
                }

                else if (nextToPlay==lastRaise || getMaxBet()==0)
                {
                    if (getMaxBet()==0)
                        System.out.println("Max bet is 0, we are going to open the Turn");
                    else
                        System.out.println("We finished the second betting round");

                    state=TheTurn;
                 }

                break;
            }
            case bettingAfterTurn:
            {
                if (!doWeHaveMoreThanTwoActivePlayersInTheGame())
                {
                    System.out.println("Game over- we have only one player in the game");
                    state=END;
                }

                else if (nextToPlay==lastRaise || getMaxBet()==0)
                {
                    if (getMaxBet()==0)
                        System.out.println("Max bet is 0, we are going to open the River");
                    else
                        System.out.println("We finished the third betting round");

                    state=TheRiver;
                }
                break;
            }

            case bettingAfterRiver:
            {
                if (!doWeHaveMoreThanTwoActivePlayersInTheGame())
                {
                    System.out.println("Game over - we have only one player in the game");
                    state=END;
                }
                else if (nextToPlay==lastRaise || getMaxBet()==0)
                {
                    if (getMaxBet()==0)
                        System.out.println("Max bet is 0, we are going to finish the game");
                    else
                        System.out.println("We finished the game.");

                    state=END;
                }
                break;
            }

            case TheFlop: {
                state=bettingAfterFlop;
                break;
            }
            case TheRiver: {
                state=bettingAfterRiver;
                break;
            }
            case TheTurn:
                state=bettingAfterTurn;
                break;
        }
    }

    public HandState getHandState() {
        return state;
    }


    public void bettingRoundForAPlayer(boolean equity) {
        PokerPlayer currPlayer = getNextToPlay();
        String action = currPlayer.getPlayerSelection();
        int additionalActionInfo = currPlayer.getAdditionalActionInfo();

        printInfoToConsole(currPlayer,action,additionalActionInfo);
        handlePlayerAction(action, additionalActionInfo);
        addToPot(currPlayer.getBet());
        currPlayer.collectBet();
        updateMaxBet();

        if (equity && action.equals("F") )
            updatePlayersWithEquity();

        incCurrPlayer();
        afterPlayerAction();
    }

    private void printInfoToConsole(PokerPlayer currPlayer, String action, int additionalActionInfo) {
        String message= currPlayer.getType() +" player ("+currPlayer.getName()+
                ") is now playing and he wants to: "+action;

        if (action.equals("R") || action.equals("B"))
        {
            message = message + " (" + additionalActionInfo + ")";
        }

        System.out.println(message);
    }


    private void clearPlayersEquity()
    {
        for (PokerPlayer p: players)
        {
            p.setEquity(0);
        }
    }
    public void updatePlayersWithEquity() {

        try {
            clearPlayersEquity();

            EquityCalculator calculator = new EquityCalculator();
            calculator.reset();

            for (PokerPlayer p: players)
            {
                if (!p.isFolded())
                {
                    String playerCards = (p.getHoleCards()).replaceAll("\\s+", "");
                    calculator.addHand(Hand.fromString(playerCards));
                }
            }

            StringBuilder tableCardsStr = new StringBuilder("");

            for (Card c : tableCards) {
                if (c.getSuit() != Suit.NA && c.getRank() != Rank.NA)
                {
                    tableCardsStr.append(c.toString());
                }
            }

            if (!tableCardsStr.toString().equals(""))
            {
                calculator.setBoardFromString(String.valueOf(tableCardsStr));
            }

            calculator.calculate();

            int i=0;

            for (PokerPlayer p: players)
            {
                if (!p.isFolded())
                {
                    p.setEquity(calculator.getHandEquity(i).getEquity());
                    i++;
                }
            }

        }
        catch (Exception e)
        {
            System.out.println("Failed to get players equity...");
            e.printStackTrace();
        }
    }


    private void handlePlayerAction(String action, int additionalActionInfo) {
        if (action.equals("F")) {
            fold(nextToPlay);
        }

        if (action.equals("B")) {
            bet(nextToPlay, additionalActionInfo);
        }

        if (action.equals("C")) {
            call(nextToPlay);
        }

        if (action.equals("R")) {
            raise(nextToPlay, additionalActionInfo);
        }

        if (action.equals("K")) {
            check(nextToPlay);
        }

    }

    private void fold(PokerPlayer player) {
        player.setFolded(true);
        player.setBet(0);

        if (!lastAction.equals("F"))
            lastActionBeforeFold=lastAction;

        setLastAction("F");
        setLastActionInfo(0);

        if (getLastRaise() == player)
            setLastRaise(null);
    }

    private void bet(PokerPlayer player, int betTo) {
        setCurrentBet(betTo);
        setLastRaise(player);
        player.setBet(getCurrentBet());

        setLastAction("B");
        setLastActionInfo(betTo);
    }

    private void call(PokerPlayer player) {
        subFromPot(player.getBet());
        player.addChips(player.getBet());
        updateMaxBet();
        player.setBet(getCurrentBet());

        setLastAction("C");
        setLastActionInfo(getCurrentBet());
    }

    private void raise(PokerPlayer player, int raiseTo) {
        subFromPot(player.getBet());
        player.addChips(player.getBet());
        updateMaxBet();

        setCurrentBet(getCurrentBet() + raiseTo);
        setLastRaise(player);

        setLastAction("R");
        setLastActionInfo(raiseTo);

        for (PokerPlayer playerInLoop : getPlayers()) {
            if (playerInLoop != player)
                playerInLoop.setCheckOccurred(false);
        }

        player.setBet(getCurrentBet());
    }

    private void check(PokerPlayer player) {
        if (getLastRaise() == null)
            setLastRaise(player);

        player.setBet(0);
        setLastAction("K");
        setLastActionInfo(0);
    }


    public void incCurrPlayer()
    {
        nextToPlay.clearSelection();
        lastPlayerToPlay=nextToPlay.getId();

        for (int i=0; i<numberOfPlayers; i++)
        {
            if (players.get(i)==nextToPlay)
            {
                for (int j=1; j<=numberOfPlayers; j++)
                {
                    if (!players.get((i + j) % numberOfPlayers).isFolded())
                    {
                        nextToPlay = players.get((i + j)% this.numberOfPlayers);
                        nextToPlay.clearSelection();
                        return;
                    }
                }
            }
        }

        nextToPlay.clearSelection();
    }

    public String getWinnersToDisplay() {

        StringBuilder message= new StringBuilder();
        List<Winner> winners= null;

        if (playersLeft() == 1) {
            int index = whoIsInTheGame();
            String cards = players.get(index).getHoleCards();
            players.get(index).isAWinner();
            players.get(index).addChips(getPot());
            players.get(index).setWinnigPrice(getPot());
            message.append(players.get(index).getName()).append(" (").append(players.get(index).getId()).append(")").append(" won with this hand: ").append(cards).append(".\n").append("Prize: ").append(getPot()).append("$\n\n");
            numberOfWinners=1;
        }

        else
        {
            try {
                winners = evaluateRound();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Winner w: winners)
            {
                w.getPlayer().isAWinner();
                int chipsToAdd=getPot()/winners.size();
                w.getPlayer().addChips(chipsToAdd);
                w.getPlayer().setWinnigPrice(getPot());
                message.append(w.getPlayer().getName()).append(" (").append(w.getPlayer().getId()).append(")").append(" won with this hand: ").append(w.getHandRank()).append(".\n").append("Prize: ").append(chipsToAdd).append("$\n\n");
             }

            numberOfWinners=winners.size();
        }

        return message.toString();
    }


    public List<String> getPossibleOptions()
    {
        List<String> options=new ArrayList<>();

        if (lastAction.equals("F")) {
            System.out.println("Last player action was Fold... I'm going to change it to: "+lastActionBeforeFold);
            lastAction = lastActionBeforeFold;
        }

        //LastPlayerToPlay -999 is only at the
        //beginning of a betting round

        if (lastPlayerToPlay==-999 || lastAction.equals("N")) {
            if (state==HandState.GameInit)
            {
                options.add("F");
                options.add("C");
                options.add("R");
            }

            if (state==HandState.bettingAfterFlop || state==HandState.bettingAfterRiver || state==HandState.bettingAfterTurn)
            {
                options.add("F");
                options.add("B");
                options.add("K");
            }
        }


        //We are NOT at the beginning of a betting round
        else
        {
            if (lastAction.toUpperCase().equals("R"))
            {
                options.add("F");
                options.add("C");
                options.add("R");
            }

            else if (lastAction.toUpperCase().equals("B"))
            {
                options.add("F");
                options.add("C");
                options.add("R");
            }

            else if (lastAction.toUpperCase().equals("C"))
            {
                options.add("F");
                options.add("C");
                options.add("R");
            }
            else if (lastAction.toUpperCase().equals("K"))
            {
                options.add("F");
                options.add("K");
                if (currentBet==0)
                {
                    options.add("B");
                }

            }
        }

        return options;

    }

    public void resetPlayersFold() {
        for (PokerPlayer p: players)
        {
            p.setFolded(false);
        }
    }

    public void resetPlayersStates() {
        for(PokerPlayer p:players)
        {
            p.setState(PlayerState.NONE);
            p.setStyle("-fx-background-color: #CCAA99");
        }
    }

    public int getWinnerID() {

        if (playersLeft() == 1)
            return players.get(whoIsInTheGame()).getId();

        try {

            List<Winner> winners = evaluateRound();
            for (PokerPlayer p:players)
            {
                if (winners.get(0).getPlayer()==p)
                    return p.getId();
            }
        } catch (Exception e) {
                e.printStackTrace();
        }

        return 0;
    }

    public int getBig(){return big;}
    public int getSmall(){return small;}

    public void setBig(int newBig) {
        big=newBig;
    }

    public void setSmall(int newSmall) {
        small=newSmall;
    }

}
