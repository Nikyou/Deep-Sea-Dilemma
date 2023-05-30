package com.deep_sea_dilemma.settings;

import com.deep_sea_dilemma.interfaces.Game;

public class Saver {
    private static Saver instance = null;

    private String saveInformation;
    private Game game = Game.Initialize();

    private Saver()
    {

    }

    private void ReadSave(){

    }

    private void SetShop(){

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
