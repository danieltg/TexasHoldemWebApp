package Engine.GameDescriptor;

import Jaxb.Structure;
import java.io.Serializable;

public class PokerStructure implements Serializable {

    private int handsCount;
    private int buy;
    private PokerBlindes blindes;


    public PokerStructure (Structure s)
    {
        this.handsCount=s.getHandsCount().intValue();
        this.buy=s.getBuy().intValue();
        this.blindes= new PokerBlindes(s.getBlindes());
    }


    //Getter
    public int getHandsCount() {
        return handsCount;
    }

    public int getBuy() {
        return buy;
    }

    public PokerBlindes getBlindes() {
        return blindes;
    }

    //Setter
    public void setHandsCount(int handsCount) {
        this.handsCount = handsCount;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public void setBlindes(PokerBlindes blindes) {
        this.blindes = blindes;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "handsCount=" + handsCount +
                ", buy=" + buy +
                ", blindes=" + blindes +
                '}';
    }

}
