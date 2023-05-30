package com.deep_sea_dilemma.interfaces;

import com.deep_sea_dilemma.objects.*;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Game {
    private static Game instance = null;


    public Tile tile = new Tile();
    public Ship ship = new Ship();
    public Goal goal = new Goal();
    public Rock rock = new Rock();
    public Vortex vortex = new Vortex();
    public Pathfinder pathfinder = Pathfinder.Initialize();

    public int buttonWidth = 300;
    public int buttonHeight = 100;

    public Stage window = null;
    public Scene mainMenu = null;
    public Scene levelSelectionScene = null;

    private Game()
    {

    }

    public void DrawTile (int x, int y, int width, int height, GridPane grid){
        tile.Draw(x, y, width, height, grid);
    }
    public void DrawShip (int x, int y, int width, int height, GridPane grid){
        ship.Draw(x, y, width, height, grid);
    }
    public void DrawGoal (int x, int y, int width, int height, GridPane grid){
        goal.Draw(x, y, width, height, grid);
    }
    public void DrawRock (int x, int y, int width, int height, GridPane grid){
        rock.Draw(x, y, width, height, grid);
    }
    public void DrawVortex (int x, int y, int width, int height, GridPane grid){
        vortex.Draw(x, y, width, height, grid);
    }

    public void DrawShip (int x, int y){
        ship.Draw(x, y);
    }
    public void AIMakeTurn (int difficulty){
        ship.AIMakeTurn(difficulty);
    }

    // Static method to create instance of Initializer class
    public static Game Initialize()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Game();

        return instance;
    }
}
