package Engine.Players;

import java.util.List;
import java.util.Random;

public class ComputerPlayer extends PokerPlayer {

    public ComputerPlayer(int id)
    {
        super(id);
        setType(PlayerType.Computer);
    }

    public ComputerPlayer(int id,String name)
    {
        super(id,name);
        setType(PlayerType.Computer);
    }


    @Override
    public String getSelection(List<String> options)
    {
        int randomNumber= new Random().nextInt(options.size());
        return options.get(randomNumber);
    }

    @Override
    public String play() {

        int randomNumber= new Random().nextInt(50);

        //10/50 chance of Fold
        //15/50 chance of Raise
        //20/50 chance of Call
        //5/50 chance of Check


        if(randomNumber<7)
            return "F";
        else if (randomNumber<20)
            return "R";
        else if (randomNumber<40)
            return "C";
        else
            return "K";

    }

    @Override
    public int getRaise(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

}
