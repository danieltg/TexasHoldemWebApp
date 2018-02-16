package Engine;

import Engine.GameDescriptor.PokerGameDescriptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public class GamesDescriptorManager {

    private final Set<PokerGameDescriptor> gamesSet;

        public GamesDescriptorManager() {
            gamesSet = new HashSet<>();
        }

        public void addGameDescriptor(PokerGameDescriptor game, Lobby lobby) throws Exception {


            gamesSet.add(game);
            lobby.addRoom(game.getGameTitle(),game);

        }

        public void removeGameDescriptor(PokerGameDescriptor game) {
            gamesSet.remove(game);
        }

        public Set<PokerGameDescriptor> getGameDescriptors() {
            return Collections.unmodifiableSet(gamesSet);
        }
    }
