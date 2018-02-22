package Engine.GameDescriptor;

import Engine.Players.ComputerPlayer;
import Engine.Players.HumanPlayer;
import Engine.Players.PlayerType;
import Engine.Players.PokerPlayer;
import Engine.Utils.RoomState;
import Jaxb.GameDescriptor;
import Jaxb.Player;
import Jaxb.Players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PokerGameDescriptor implements Serializable {

    private GameType type;
    private PokerStructure structure;
    private List<PokerPlayer> players;
    private int numberOfPlayers=4;
    private  int numberOfHumanPlayers;
    private  int numberOfComputerPlayers;
    private int registeredPlayers;

    private String gameTitle="game";
    private String uploadedBy="";

    private RoomState status;

    public final static int BASIC_NUM_OF_HUMAN_PLAYERS= 1;
    public final static int BASIC_NUM_OF_COMPUTER_PLAYERS= 3;

    public PokerGameDescriptor (GameDescriptor g,String username)
    {
        numberOfHumanPlayers=0;
        numberOfComputerPlayers=0;
        registeredPlayers=0;
        players= new ArrayList<>();
        uploadedBy=username;
        type= GameType.valueOf(g.getGameType());
        if (type==GameType.DynamicMultiPlayer) {
            numberOfPlayers=g.getDynamicPlayers().getTotalPlayers();
            gameTitle=g.getDynamicPlayers().getGameTitle();
        }

        structure=new PokerStructure((g.getStructure()));
        status=RoomState.WAITING;
    }

    public PokerGameDescriptor (GameDescriptor g)
    {
        numberOfHumanPlayers=0;
        numberOfComputerPlayers=0;
        registeredPlayers=0;
        players= new ArrayList<>();

        type= GameType.valueOf(g.getGameType());
        if (type==GameType.DynamicMultiPlayer) {
            numberOfPlayers=g.getDynamicPlayers().getTotalPlayers();
            gameTitle=g.getDynamicPlayers().getGameTitle();
        }

        structure=new PokerStructure((g.getStructure()));
    }

    public void roomEnded()
    {
        status=RoomState.Ended;
        registeredPlayers=0;

    }
    public String getGameTitle() {
        return gameTitle;
    }
    private void setPlayers(Players players) {
        if (type == GameType.Basic)
        {

            for (int i=0; i<BASIC_NUM_OF_HUMAN_PLAYERS; i++) {
                this.players.add(new HumanPlayer(i));
                numberOfHumanPlayers++;
            }

            for (int i=BASIC_NUM_OF_HUMAN_PLAYERS; i<numberOfPlayers; i++) {
                this.players.add(new ComputerPlayer(i));
                numberOfComputerPlayers++;
            }
        }
        else
        {
            numberOfPlayers=players.getPlayer().size();
            for (int i=0; i<numberOfPlayers; i++)
            {
                Player p= players.getPlayer().get(i);
                int id=p.getId().intValue();
                String name=p.getName();

                if (PlayerType.valueOf(p.getType())==PlayerType.Human) {
                    this.players.add(new HumanPlayer(id,name));
                    numberOfHumanPlayers++;
                }
                else
                {
                    this.players.add(new ComputerPlayer(id,name));
                    numberOfComputerPlayers++;

                }
            }

        }
    }

    public GameType getType() {
        return type;
    }

    public PokerStructure getStructure() {
        return structure;
    }

    @Override
    public String toString() {
        return "GameDescriptor:\n" +
                "\tType=" + type +
                "\n\tStructure:\n"+
                "\t\tHandsCount="+structure.getHandsCount()+
                "\n\t\tBuy="+structure.getBuy()+
                "\n\t\tBlindes:\n"+
                "\t\t\tFixed="+structure.getBlindes().isFixed()+
                "\n\t\t\tBig="+structure.getBlindes().getBig()+
                "\n\t\t\tSmall="+structure.getBlindes().getSmall()+
                "\n\t\t\tAdditions="+structure.getBlindes().getAdditions()+
                "\n\t\t\tMax total rounds="+structure.getBlindes().getMaxTotalRounds();

    }

    public int getNumberOfPlayers() {return  numberOfPlayers;}

    public int getNumberOfHumanPlayersPlayers() {return  numberOfHumanPlayers;}

    public int getNumberOfComputerPlayers() {return  numberOfComputerPlayers;}

    public List<PokerPlayer> getPlayers() {
        return players;
    }

    public int getRegisteredPlayers() {
        return registeredPlayers;
    }

    public void incRegisteredPlayers() {
        registeredPlayers++;
        if (registeredPlayers== numberOfPlayers)
        {
            status=RoomState.RUNNING;
        }
    }

    public void decRegisteredPlayers() {
        if (registeredPlayers>0) {
            registeredPlayers--;
        }
        status=RoomState.WAITING;

    }

    public RoomState getStatus() {
        return status;
    }
}
