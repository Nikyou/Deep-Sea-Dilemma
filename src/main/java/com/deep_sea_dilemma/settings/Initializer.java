package com.deep_sea_dilemma.settings;

import com.deep_sea_dilemma.interfaces.Game;

public class Initializer {
    private static Initializer instance = null;

    private String saveInformation;
    private Game game = Game.Game();

    private Initializer()
    {
        game.tile.SetImage("riptide.jpg");
        game.ship.SetImage("ship.png");
        game.goal.SetImage("goal.png");
        game.vortex.SetImage("vortex.png");
        game.rock.SetImage("rock.png");
    }

    private void ReadSave(){

    }

    private void SetShop(){

    }

    // Static method to create instance of Initializer class
    public static Initializer Initializer()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Initializer();

        return instance;
    }
}
