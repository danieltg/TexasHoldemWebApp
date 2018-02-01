package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;
import Engine.Players.PokerPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {


    private Map<String,Room> rooms;

    public Lobby() {
        rooms = new HashMap<>();
    }

    public void addRoom(String roomName,PokerGameDescriptor pokerGameDescriptor) {
        rooms.put(roomName,new Room(roomName,pokerGameDescriptor));
    }

    public void addPlayerToRoom(String roomName,String userName,String type)
    {
        rooms.get(roomName).addUserToRoom(userName,type);
    }
    public void removeUserFromRoom(String roomName,String userName,String type)
    {
        rooms.get(roomName).removeUserFromRoom(userName);
    }

    public String getRoomNameByPlayerName(String usernameFromSession) {
        for (String s: rooms.keySet()) {
            if (rooms.get(s).isUserInGame(usernameFromSession))
                return s;
        }

        return "error";
    }

    public Room getRoomByName(String gameRoom)
    {
        return rooms.get(gameRoom);
    }
}
