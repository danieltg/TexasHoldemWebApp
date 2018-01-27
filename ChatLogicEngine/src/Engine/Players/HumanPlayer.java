package Engine.Players;


import java.util.List;

public class HumanPlayer extends PokerPlayer {

    public HumanPlayer(int id)
    {
        super(id);
        setType(PlayerType.Human);

    }

    public HumanPlayer(int id,String name)
    {
        super(id,name);
        setType(PlayerType.Human);
    }

    @Override
    public String play() {
        return null;
    }


    @Override
    public int getRaise(int min, int max) {return 0;
    }


    public List<String> listOfDetailesForHand()
    {
        List<String> list = super.listOfDetailesForHand();
        list.add("Cards: "+this.getHoleCards());

        return list;

    }

    @Override
    public String getSelection(List<String> options)
    {
        return null;
    }
}
