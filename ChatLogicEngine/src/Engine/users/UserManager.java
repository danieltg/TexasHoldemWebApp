package Engine.users;

import java.util.*;

public class UserManager {

    private final Map<String,String> usersSet;

    public UserManager() {
        usersSet = new HashMap<>();
    }

    public void addUser(String username,String type) {
        usersSet.put(username,type);
    }

    public void removeUser(String username) {
        usersSet.remove(username);
    }

    public Map<String,String> getUsers() {
        return  usersSet;
    }

    public boolean isUserExists(String username) {
        return usersSet.containsKey(username);
    }
}
