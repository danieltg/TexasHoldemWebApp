package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.PokerPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    private Map<String,Room> rooms= new HashMap<String,Room>();

    //need to Sync Messages
    public void createNewRoom(String roomName, PokerGameDescriptor pokerGameDescriptor)
    {
        Room room=new Room(roomName,pokerGameDescriptor);
        rooms.put(roomName,room);
    }
    public void addPlayerToRoom(String roomName,String userName,String type)
    {
        rooms.get(roomName).addUserToRoom(userName,type);
    }
    public void removeUserFromRoom(String roomName,String userName,String type)
    {
        rooms.get(roomName).removeUserFromRoom(userName);
    }
}
