package Engine;

import Engine.GameDescriptor.PokerBlindes;
import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.ComputerPlayer;
import Engine.Players.HumanPlayer;
import Engine.Players.PokerPlayer;
import Engine.Utils.RoomState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private String roomName;
    private GameManager gameManager;
    private Map<String,String> usersInGame;

    public Room (String name, PokerGameDescriptor gameDescriptorForGame)
    {
        roomName=name;
        gameManager=new GameManager();
        gameManager.setGameDescriptor(gameDescriptorForGame);
        usersInGame = new HashMap<>();
    }

    public void addUserToRoom(String userName, String type) {
        usersInGame.put(userName,type);
        updateRegisteredPlayersOnAdd();

        String _name;
        String _type;
        if (gameManager.getGameDescriptor().getStatus()== RoomState.RUNNING)
        {

            int id=0;
            for (Map.Entry<String, String> entry : usersInGame.entrySet())
            {

                _name = entry.getKey();
                _type= entry.getValue();
                if(_type=="Human") {
                    HumanPlayer humanPlayerInRoom=new HumanPlayer(id,_name);

                    gameManager.getPlayers().add(humanPlayerInRoom);
                }
                else
                {
                    ComputerPlayer computerPlayerInRoom=new ComputerPlayer(id,_name);

                    gameManager.getPlayers().add(computerPlayerInRoom);
                }
                id++;

            }

            //we should start a new hand
            //I copied it from EX2
//            handNumber++;
//            PokerBlindes blindes=getGameDescriptor().getStructure().getBlindes();
//
//            currHand= new PokerHand(blindes,getPlayers());
//            currHand.addToPot(getMoneyFromLastHand());
//            currHand.setHandState(HandState.GameInit);
//            setTotalRounds(getHandsCount()/numberOfPlayers);
//            return currHand;
        }
    }
    public void removeUserFromRoom(String userName) {
        usersInGame.remove(userName);
        updateRegisteredPlayersOnRemove();
    }

    private void updateRegisteredPlayersOnRemove() {
        gameManager.getGameDescriptor().decRegisteredPlayers();

    }

    public boolean isUserInGame(String username)
    {
        for(String name:usersInGame.keySet()) {
            if (name.equals(username))
                return true;
        }
        return false;
    }

    public Map<String,String> getUsersInGame()
    {
        return usersInGame;
    }

    public GameManager getGameManager()
    {
        return this.gameManager;
    }


    public void updateRegisteredPlayersOnAdd()
    {
        gameManager.getGameDescriptor().incRegisteredPlayers();
    }
    public int getNumberOfPlayers() {
        return gameManager.getGameDescriptor().getRegisteredPlayers();
    }

}
