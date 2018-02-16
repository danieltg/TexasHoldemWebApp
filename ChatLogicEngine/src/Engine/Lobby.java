package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lobby {


    private Map<String,Room> rooms;
    private Map<String, String> users;

    public Lobby() {
        rooms = new HashMap<>();
        users=new HashMap<>();
    }

        public void addRoom(String roomName,PokerGameDescriptor pokerGameDescriptor) throws Exception {
        if (rooms.containsKey(roomName))
            throw new Exception("The same game name already exists.");

        rooms.put(roomName,new Room(roomName,pokerGameDescriptor));
    }

    public void addPlayerToRoom(String roomName,String userName,String type)
    {
        rooms.get(roomName).addUserToRoom(userName,type);
        users.put(userName,roomName);
    }
    public void removeUserFromRoom(String roomName,String userName)
    {
        rooms.get(roomName).removeUserFromRoom(userName);
    }
// getUserbyplayername?
    public String getRoomNameByPlayerName(String username) {

        return users.get(username);
    }

    public Room getRoomByName(String gameRoom)
    {
        return rooms.get(gameRoom);
    }

    public Set<PokerGameDescriptor> getAllRooms() {

        Set<PokerGameDescriptor> roomsSet = new HashSet<>();
        
        for (Room room: rooms.values())
        {
            roomsSet.add(room.getGameManager().getGameDescriptor());
        }
        return roomsSet;
    }

    public void removeUser(String username) {
     if(users.containsKey(username))
     {
         String roomName=getRoomNameByPlayerName(username);
         removeUserFromRoom(roomName,username);

     }
    }
}
