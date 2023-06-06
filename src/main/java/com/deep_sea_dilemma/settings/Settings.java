package com.deep_sea_dilemma.settings;

import com.deep_sea_dilemma.objects.Entity;
import javafx.scene.image.Image;

public class Settings {
    private static Settings instance = null;

    String filename = "Save.dsd"; // Name of the save file

    //In-game settings
    public int gold = 0;
    boolean [] completedLevels = {
            // 0 level
            false,
            // 1 level
            false,
            // 2 level
            false,
            // 3 level
            false,
            // 4 level
            false,
            // 5 level
            false,
            // 6 level
            false,
            // 7 level
            false,
            // 8 level
            false,
            // 9 level
            false,
    };
    public int [] selectedCosmetic = {
            // 0: ship
            0,
            // 1: tile
            0,
            // 2: goal
            0,
            // 3: rock
            0,
            // 4: vortex
            0,

    };
    public boolean [] unlockedTiles = {
            // 0
            true,
            // 1
            false,
            // 2
            false,
    };
    public boolean [] unlockedShips = {
            // 0
            true,
            // 1
            false,
            // 2
            false,
    };
    public boolean [] unlockedGoals = {
            // 0
            true,
            // 1
            false,
            // 2
            false,
    };
    public boolean [] unlockedRocks = {
            // 0
            true,
            // 1
            false,
            // 2
            false,
    };
    public boolean [] unlockedVortices = {
            // 0
            true,
            // 1
            false,
            // 2
            false,
    };

    static public String [] pathImageTiles = {
            // 0
            "tile1.jpg",
            // 1
            "tile2.jpg",
            // 2
            "tile3.jpg",
    };
    static public String [] pathImageShips = {
            // 0
            "ship1.png",
            // 1
            "ship2.png",
            // 2
            "ship3.png",
    };
    static public String [] pathImageGoals = {
            // 0
            "goal1.png",
            // 1
            "goal2.png",
            // 2
            "goal3.png",
    };
    static public String [] pathImageRocks = {
            // 0
            "rock1.png",
            // 1
            "rock2.png",
            // 2
            "rock3.png",
    };
    static public String [] pathImageVortices = {
            // 0
            "vortex1.png",
            // 1
            "vortex2.png",
            // 2
            "vortex3.png",
    };
    static public Image [] imageTiles = {
            // 0
            Entity.GetImageByPath(pathImageTiles[0]),
            // 1
            Entity.GetImageByPath(pathImageTiles[1]),
            // 2
            Entity.GetImageByPath(pathImageTiles[2]),
    };
    static public Image [] imageShips = {
            // 0
            Entity.GetImageByPath(pathImageShips[0]),
            // 1
            Entity.GetImageByPath(pathImageShips[1]),
            // 2
            Entity.GetImageByPath(pathImageShips[2]),
    };
    static public Image [] imageGoals = {
            // 0
            Entity.GetImageByPath(pathImageGoals[0]),
            // 1
            Entity.GetImageByPath(pathImageGoals[1]),
            // 2
            Entity.GetImageByPath(pathImageGoals[2]),
    };
    static public Image [] imageRocks = {
            // 0
            Entity.GetImageByPath(pathImageRocks[0]),
            // 1
            Entity.GetImageByPath(pathImageRocks[1]),
            // 2
            Entity.GetImageByPath(pathImageRocks[2]),
    };
    static public Image [] imageVortices = {
            // 0
            Entity.GetImageByPath(pathImageVortices[0]),
            // 1
            Entity.GetImageByPath(pathImageVortices[1]),
            // 2
            Entity.GetImageByPath(pathImageVortices[2]),
    };

    public int[] cosmeticsPrice = {
            // Defaults
            0,
            // 2 row
            1,
            // 3 row
            3,
    };


    public boolean [] GetCompletedLevels(){
        return completedLevels;
    }

    private Settings()
    {
    }

    // Static method to create instance of Initializer class
    public static Settings Initialize()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Settings();

        return instance;
    }
}
