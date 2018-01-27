package Engine;

import Engine.Players.PokerPlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PokerHandStep implements Serializable  {

    private List<PokerPlayer> players;
    private int activePlayer;
    private int pot;
    private String[] tableCards;
    private int currentBet;   //Current bet amount that must be called/raised
    private String action;
    private int additionActionInformation;

    public PokerHandStep(List<PokerPlayer> playersToSave, int activePlayer, int pot, String[] tableCards, int currentBet, String action, int additionActionInformation) {
        this.players = new ArrayList<>(playersToSave);

        Iterator<PokerPlayer> ite= playersToSave.iterator();

        while(ite.hasNext()) {
            PokerPlayer p = ite.next();
            this.players.add(p.clone());
        }

        for (int i=0; i<players.size(); i++)
            this.players.remove(playersToSave.get(i));

        this.activePlayer = activePlayer;
        this.pot = pot;
        this.tableCards = tableCards;
        this.currentBet = currentBet;
        this.action = action;
        this.additionActionInformation = additionActionInformation;
    }


    private int getplayerIndexById()
    {
        for (int i=0; i<players.size(); i++)
        {
            if (players.get(i).getId()==activePlayer)
                return i;
        }

        return -999;
    }

    public String getStepAsString()
    {
        StringBuilder message;

        if (activePlayer==-999 || activePlayer==-888) {
            message = new StringBuilder("The step is not related to any player\n");
            if (action.equals("PLAYER_CARDS")) {
                message.append("Players cards:\n");
                for (PokerPlayer p: players)
                    message.append("Player <").append(p.getName()).append(", ").append(p.getId()).append("> : ").append(p.getHoleCards()).append("\n");
            }
            else
                message.append("Cards: ").append(tableCards[0]).append(",").append(tableCards[1]).append(",").append(tableCards[2]).append(",").append(tableCards[3]).append(",").append(tableCards[4]).append("\n");
        }
        else {
            int indexInList=getplayerIndexById();
            message = new StringBuilder("Active player ID: " + activePlayer + "\n");
            message.append("Player name: ").append(players.get(indexInList).getName()).append("\n");
            message.append("Player type: ").append(players.get(indexInList).getType()).append("\n");
            message.append("Player cards: ").append(players.get(indexInList).getHoleCards()).append("\n");
            message.append("Player chips: ").append(players.get(indexInList).getChips()).append("\n");
            message.append("Player bet: ").append(players.get(indexInList).getBet()).append("\n");
            message.append("Player equity: ").append(players.get(indexInList).getEquity()).append("\n");

        }

        message.append("Pot: ").append(pot).append("\n");
        message.append("Current Bet: ").append(currentBet).append("\n");
        message.append("Action: ").append(action).append("\n");

        if (Objects.equals(action, "R") || Objects.equals(action, "B"))
            message.append("Additional Action Information: ").append(additionActionInformation).append("\n");


        return message.toString();
    }

    public List<PokerPlayer> getPlayers()
    {
        return players;
    }

    public String[] getTableCards() {
        return tableCards;
    }

    public int getPot() {
        return pot;
    }

    public String getStepToDisplay() {
        String message="";
        if (activePlayer!=-999) {
            message="Player: "+getPlayerNameByID(activePlayer)+
                    ", Action: "+action;
            if (Objects.equals(action, "R") || Objects.equals(action, "B"))
                message=message+" ("+additionActionInformation+")";

        }

            return message;
    }

    public String getPlayerAndID()
    {
        if(activePlayer!=-999)
            return getPlayerNameByID(activePlayer)+ " ("+activePlayer+")";
        return "";
    }

    public String getPlayerNameByID (int id)
    {
        for (PokerPlayer p: players)
        {
            if(p.getId()==id)
                return p.getName();
        }

        return "";
    }

    public String getAction()
    {
        return action;
    }
}
