package com.deep_sea_dilemma.interfaces;

import com.deep_sea_dilemma.objects.*;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game {
    private static Game instance = null;


    public Tile tile = new Tile();
    public Ship ship = new Ship();
    public Goal goal = new Goal();
    public Rock rock = new Rock();
    public Vortex vortex = new Vortex();
    public Pathfinder pathfinder = new Pathfinder();

    public int buttonWidth = 300;
    public int buttonHeight = 100;

    public Stage window = null;
    public Scene mainMenu = null;
    public Scene levelSelectionScene = null;

    private Game()
    {

    }

    // Static method to create instance of Initializer class
    public static Game Game()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Game();

        return instance;
    }
}
