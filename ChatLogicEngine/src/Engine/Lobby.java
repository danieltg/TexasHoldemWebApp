package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    private List<Room> rooms;

    public Lobby() {
        rooms =  new ArrayList<>();
    }

    public void addRoom(String roomName,PokerGameDescriptor pokerGameDescriptor) {
        rooms.add(new Room(roomName,pokerGameDescriptor));
    }


    public List<Room> getLobby() {
        return  rooms;
    }

    public void createNewRoom(String roomName, PokerGameDescriptor pokerGameDescriptor)
    {
        Room room=new Room(roomName,pokerGameDescriptor);
        rooms.add(room);
    }

}
