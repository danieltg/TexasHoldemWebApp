package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.PokerPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    private Map<String,Room> rooms= new HashMap<String,Room>();


    //func that creating room - "Create new room" button in gui
    //need to Sync Messages
    public void createNewRoom(String roomName, PokerGameDescriptor pokerGameDescriptor)
    {
        Room room=new Room(roomName,pokerGameDescriptor);
        rooms.put(roomName,room);
    }

}
