package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.PokerPlayer;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<Room> rooms= new ArrayList<>();

    public void createNewRoom(String roomName, PokerGameDescriptor pokerGameDescriptor)
    {
        Room room=new Room(roomName,pokerGameDescriptor);
        rooms.add(room);
    }
}
