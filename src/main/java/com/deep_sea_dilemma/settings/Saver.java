package com.deep_sea_dilemma.settings;

import com.deep_sea_dilemma.interfaces.Game;

public class Saver {
    private static Saver instance = null;

    private String saveInformation;
    private Game game = Game.Game();

    private Saver()
    {

    }

    private void ReadSave(){

    }

    private void SetShop(){

    }

    // Static method to create instance of Initializer class
    public static Saver Saver()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Saver();

        return instance;
    }
}
