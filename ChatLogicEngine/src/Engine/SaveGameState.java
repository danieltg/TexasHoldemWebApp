package Engine;

import java.io.*;

public class SaveGameState {

    public static void saveGameDataToFile(String path, GameManager gameManager) throws IOException {
        File file = new File(path);
        FileOutputStream fileStream = new FileOutputStream(file);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(gameManager);

        objectStream.close();
        fileStream.close();
    }

    public static GameManager loadGameDataFromFile(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        FileInputStream fileStream = new FileInputStream(file);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);

        return (GameManager) objectStream.readObject();
    }
}
