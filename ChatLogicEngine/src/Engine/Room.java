package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.ComputerPlayer;
import Engine.Players.HumanPlayer;
import Engine.Players.PlayerState;
import Engine.Utils.RoomState;

import java.util.HashMap;
import java.util.Map;

public class Room {

    private String roomName;
    private GameManager gameManager;
    private Map<String,String> usersInGame;
    private Map<String,String> viewerInGame;

    public Room (String name, PokerGameDescriptor gameDescriptorForGame)
    {
        roomName=name;
        gameManager=new GameManager();
        gameManager.setGameDescriptor(gameDescriptorForGame);
        usersInGame = new HashMap<>();
        viewerInGame=new HashMap<>();
    }

    public void addViewerToRoom(String userName, String type) {
        viewerInGame.put(userName,type);
    }

    public void removeViewerFromRoom(String userName) {
        viewerInGame.remove(userName);
    }

    public void addUserToRoom(String userName, String type) {
        usersInGame.put(userName,type);
        updateRegisteredPlayersOnAdd();


        if (getRoomState()== RoomState.RUNNING)
        {
            createPlayersList();
            startNewHand();
            gameManager.RunOneHand();
        }
    }

    private void startNewHand() {
        gameManager.startNewHand();
    }

    public void createPlayersList()
    {
        int id=1;

        for (String playerName: usersInGame.keySet())
        {
            String playerType= usersInGame.get(playerName);
            if(playerType.equals("HUMAN"))
            {
                gameManager.getPlayers().add(new HumanPlayer(id,playerName));
            }

            else
            {
                gameManager.getPlayers().add(new ComputerPlayer(id,playerName));
            }

            id++;
        }

        if(gameManager.getPlayers().size()>2)
        {
            gameManager.getPlayers().get(0).setState(PlayerState.DEALER);
            gameManager.getPlayers().get(1).setState(PlayerState.SMALL);
            gameManager.getPlayers().get(2).setState(PlayerState.BIG);
        }
        else
        {
            gameManager.getPlayers().get(0).setState(PlayerState.BIG);
            gameManager.getPlayers().get(1).setState(PlayerState.SMALL);
        }

    }
    public void removeUserFromRoom(String userName) {

        if (usersInGame.containsKey(userName))
        {
            usersInGame.remove(userName);
            updateRegisteredPlayersOnRemove();
            if (getRoomState()==RoomState.RUNNING)
            {
                getGameManager().getPlayerByName(userName).setAction("F");
                getGameManager().getPlayerByName(userName).setAdditionalActionInfo(0);
                getGameManager().getPlayerByName(userName).setLeave(true);

                if (getGameManager().getPlayerByName(userName).isMyTurn())
                {
                    getGameManager().playBettingRounds();
                }
            }
        }
        else
        {
            viewerInGame.remove(userName);
        }

    }

    private void updateRegisteredPlayersOnRemove() {
        gameManager.getGameDescriptor().decRegisteredPlayers();
        if (gameManager.getGameDescriptor().getRegisteredPlayers()==0)
            gameManager.clearGame();
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

    public RoomState getRoomState()
    {
        return gameManager.getGameDescriptor().getStatus();
    }

    public Map<String,String> getViewers() { return viewerInGame; }
}
