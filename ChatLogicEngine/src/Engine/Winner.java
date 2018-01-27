package Engine;

import Engine.Players.PokerPlayer;

import java.io.Serializable;

public class Winner implements Serializable {

    private PokerPlayer player;
    private String handRank;

    public Winner(PokerPlayer player, String handRank) {
        this.player = player;
        this.handRank = handRank;
    }
    public String getHandRank() {
        return handRank;
    }
    public PokerPlayer getPlayer() {
        return player;
    }

}
