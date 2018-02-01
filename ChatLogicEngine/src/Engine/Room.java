package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.PokerPlayer;
import Jaxb.GameDescriptor;
import com.sun.javafx.collections.MappingChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {

    private String roomName;
    private GameManager gameManager;
    private Map<String,String> usersInGame= new HashMap<String, String>();
    int numberPlayers=0;

    public Room (String name, PokerGameDescriptor gameDescriptorForGame)
    {
        roomName=name;
        gameManager=new GameManager();
        gameManager.setGameDescriptor(gameDescriptorForGame);

    }


    public void addUserToRoom(String userName, String type) {
        usersInGame.put(userName,type);
        numberPlayers=usersInGame.size();
    }
    public void removeUserFromRoom(String userName) {
        usersInGame.remove(userName);
        numberPlayers=usersInGame.size();
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

}
