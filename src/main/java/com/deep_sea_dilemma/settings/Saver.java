package com.deep_sea_dilemma.settings;

import com.deep_sea_dilemma.interfaces.Cosmetics;
import com.deep_sea_dilemma.levels.LevelsStore;
import java.io.*;

public class Saver implements LevelsStore {
    private static Saver instance = null;
    public Settings settings = Settings.Initialize();


    private Saver() {
        ReadFile();
    }
    public void SaveFile() {
        ObjectOutputStream writer = null;
        try {
            writer = new ObjectOutputStream(new FileOutputStream(settings.filename));

            //Write gold to save file
            writer.writeObject(settings.gold);

            // Write completed levels to save file
            writer.writeObject(settings.completedLevels);

            // Write selected cosmetic to save file
            writer.writeObject(settings.selectedCosmetic);

            // Write unlocked cosmetics to save file
            writer.writeObject(settings.unlockedTiles);
            writer.writeObject(settings.unlockedShips);
            writer.writeObject(settings.unlockedGoals);
            writer.writeObject(settings.unlockedRocks);
            writer.writeObject(settings.unlockedVortices);

            writer.close();

        } catch (IOException e) {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException ignored) {}
            }
        }
    }

    private void ReadFile(){
        File file = new File(settings.filename);
        if (file.exists()) {
            ObjectInputStream reader = null;
            try {
                //Open save file
                reader = new ObjectInputStream(new FileInputStream(settings.filename));

                //Read gold from save file
                int fileGold = (int) reader.readObject();
                if (fileGold >= 0){
                    settings.gold = fileGold;
                }

                // Read completed levels from save file
                boolean[] completedArray = (boolean[]) reader.readObject();
                if (completedArray.length == settings.completedLevels.length){
                    settings.completedLevels = completedArray;
                }

                // Read selected cosmetic from save file
                int[] cosArray = (int[]) reader.readObject();
                if (cosArray.length == settings.selectedCosmetic.length){
                    settings.selectedCosmetic = cosArray;
                }


                // Write unlocked cosmetics to save file
                boolean[] boolArray = (boolean[]) reader.readObject();
                if (boolArray.length == settings.unlockedTiles.length){
                    settings.unlockedTiles = boolArray;
                }
                boolArray = (boolean[]) reader.readObject();
                if (boolArray.length == settings.unlockedShips.length){
                    settings.unlockedShips = boolArray;
                }
                boolArray = (boolean[]) reader.readObject();
                if (boolArray.length == settings.unlockedGoals.length){
                    settings.unlockedGoals = boolArray;
                }
                boolArray = (boolean[]) reader.readObject();
                if (boolArray.length == settings.unlockedRocks.length){
                    settings.unlockedRocks = boolArray;
                }
                boolArray = (boolean[]) reader.readObject();
                if (boolArray.length == settings.unlockedVortices.length){
                    settings.unlockedVortices = boolArray;
                }

                reader.close();
            } catch (IOException | ClassNotFoundException e) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {}
                }
                SaveFile();
            }
        } else {
            SaveFile();
        }

    }

    public void EndLevelSave(int levelNumber){
        if (!settings.completedLevels[levelNumber]){
            settings.completedLevels[levelNumber] = true;
            Cosmetics.AddGold(LevelsStore.goldPerLevel[levelNumber]);
            SaveFile();
        }
    }

    // Static method to create instance of Initializer class
    public static Saver Initialize()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Saver();

        return instance;
    }
}
