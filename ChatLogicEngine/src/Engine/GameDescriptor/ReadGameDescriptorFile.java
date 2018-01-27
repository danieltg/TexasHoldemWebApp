package Engine.GameDescriptor;

import Engine.Exceptions.BlindesException;
import Engine.Exceptions.StructureException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import Engine.Players.PokerPlayer;
import Engine.Utils.EngineUtils;
import Jaxb.GameDescriptor;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

public class ReadGameDescriptorFile extends Task<Boolean>{

    private  PokerGameDescriptor pokerGameDescriptor;
    private  String filePath=null;
    private boolean isValid=true;

    public void readFile(String filePath) throws FileNotFoundException, JAXBException, StructureException, BlindesException {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateMessage("Loading");
        this.updateProgress(20, 100);
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        if (!EngineUtils.getFileExtension(file).equals("xml"))
            throw new FileNotFoundException("Invalid file extension");

        JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        GameDescriptor gameDescriptor = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);
        this.updateProgress(50, 100);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pokerGameDescriptor = new PokerGameDescriptor(gameDescriptor);
        validatePokerGameDescriptor(pokerGameDescriptor);
        this.updateProgress(80, 100);
        updateMessage("Check file");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.updateProgress(100, 100);

    }


    private void validatePokerGameDescriptor(PokerGameDescriptor game) throws StructureException, BlindesException {

        PokerStructure s=game.getStructure();
        PokerBlindes b=s.getBlindes();
        int numberOfPlayers=game.getNumberOfPlayers();

        if (s.getHandsCount() < numberOfPlayers)
                    throw new StructureException(StructureException.INVALID_HANDSCOUNT_BIGGER_THEN_NUMOFPLAYERS +" ("+numberOfPlayers+")");

        if ((s.getHandsCount() % numberOfPlayers) != 0)
            throw new StructureException(StructureException.INVALID_HANDSCOUNT+" ("+numberOfPlayers+")");


        try{
            game.getPlayers().stream().collect(
                    Collectors.toMap(PokerPlayer::getId, PokerPlayer::getName));
        }
        catch (IllegalStateException e)
        {
            throw new StructureException(StructureException.INVALID_PLAYERS);
        }

        if (s.getHandsCount()<0 )
            throw new StructureException(StructureException.NEGATIVE_HANDSCOUNT);

        if (s.getBuy()<0)
            throw new StructureException(StructureException.NEGATIVE_BUY);

        if (b.getSmall()<0 ) {
            throw new BlindesException(BlindesException.NEGATIVE_SMALL);
        }
        if (b.getBig()<0) {

            throw new BlindesException(BlindesException.NEGATIVE_BIG);
        }
        if(b.getAdditions()<0) {

            throw new BlindesException(BlindesException.NEGATIVE_ADDITIONS);
        }
        if(b.getMaxTotalRounds()<0) {

            throw new BlindesException(BlindesException.NEGATIVE_MAX_TOTAL_ROUNDS);
        }

        if(b.getSmall()>=b.getBig()) {

            throw new BlindesException(BlindesException.SMALL_BIGGER_THEN_SMALL);
        }

        if (s.getBuy()<b.getBig()){
            throw new BlindesException(BlindesException.BUY_SMALLER_THEN_SMALL);
        }

        if (s.getBlindes().isFixed()==false)
        {
            int totalRounds=s.getHandsCount()/numberOfPlayers;
            int totalRAdd=totalRounds*b.getAdditions();
            int maxTotalRA=b.getMaxTotalRounds()*b.getAdditions();
            int maxBig=getMin(totalRAdd,maxTotalRA)+b.getBig();

            if (maxBig>(s.getBuy()/2))
                throw new BlindesException(BlindesException.MAX_BIG_BIGGER_THEN_BUY);

        }

    }

    private int getMin(int a, int b)
    {
        return (a<=b? a:b);
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            readFile(filePath);
            updateMessage("Configuration file was loaded successfully");
            isValid=true;
            return true;
        }
        catch (Exception e)
        {
            updateMessage(e.getMessage());
            isValid=false;
            return false;
        }
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PokerGameDescriptor getGameDescriptor() {
        return pokerGameDescriptor;
    }

    public boolean isValidFile()
    {
        return isValid;
    }

    public String getPath() {
        return filePath;
    }
}
