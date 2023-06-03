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

    public Image [] imageTiles = {
            // 0
            Entity.GetImageByPath("riptide.jpg"),
            // 1
            Entity.GetImageByPath("riptide1.jpg"),
            // 2
            Entity.GetImageByPath("riptide.jpg"),
    };
    public Image [] imageShips = {
            // 0
            Entity.GetImageByPath("ship.png"),
            // 1
            Entity.GetImageByPath("ship.png"),
            // 2
            Entity.GetImageByPath("ship.png"),
    };
    public Image [] imageGoals = {
            // 0
            Entity.GetImageByPath("goal.png"),
            // 1
            Entity.GetImageByPath("goal.png"),
            // 2
            Entity.GetImageByPath("goal.png"),
    };
    public Image [] imageRocks = {
            // 0
            Entity.GetImageByPath("rock.png"),
            // 1
            Entity.GetImageByPath("rock.png"),
            // 2
            Entity.GetImageByPath("rock.png"),
    };
    public Image [] imageVortices = {
            // 0
            Entity.GetImageByPath("vortex.png"),
            // 1
            Entity.GetImageByPath("vortex.png"),
            // 2
            Entity.GetImageByPath("vortex.png"),
    };

    public int[] cosmeticsPrice = {
            // Defaults
            0,
            // 2 row
            1,
            // 3 row
            3,
    };


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
