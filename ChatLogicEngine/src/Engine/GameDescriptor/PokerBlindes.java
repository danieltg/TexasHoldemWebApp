package Engine.GameDescriptor;

import Jaxb.Blindes;
import java.io.Serializable;

public class PokerBlindes implements Serializable {

    private boolean fixed;
    private int big;
    private int small;
    private int additions;
    private int maxTotalRounds;


    public PokerBlindes (Blindes b)
    {
        this.big=b.getBig().intValue();
        this.small= b.getSmall().intValue();
        this.fixed=b.isFixed();
        if (b.getAdditions()!=null)
            this.additions=b.getAdditions().intValue();
        else
            this.additions=0;

        if (b.getMaxTotalRounds()!=null)
            this.maxTotalRounds=b.getMaxTotalRounds().intValue();
        else
            this.maxTotalRounds=0;
    }

    //Getter
    public boolean isFixed() { return fixed; }

    public int getBig() { return big; }

    public int getSmall() { return small; }

    public int getAdditions() { return additions; }

    public int getMaxTotalRounds() { return maxTotalRounds; }

    //Setter
    public void setFixed(boolean fixed) { this.fixed = fixed; }

    public void setBig(int big) { this.big = big; }

    public void setSmall(int small) { this.small = small; }

    public void setAdditions(int additions) { this.additions = additions; }

    public void setMaxTotalRounds(int maxTotalRounds) { this.maxTotalRounds = maxTotalRounds; }

    @Override
    public String toString() {
        return "Blindes{" +
                "fixed=" + fixed +
                ", big=" + big +
                ", small=" + small +
                ", additions=" + additions +
                ", maxTotalRounds=" + maxTotalRounds +
                '}';
    }

}
