package com.deep_sea_dilemma;

import com.deep_sea_dilemma.interfaces.Game;
import com.deep_sea_dilemma.levels.Creator;
import com.deep_sea_dilemma.settings.Initializer;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class Main extends Application {

    private Game game = Game.Initialize();
    private Initializer initializer = Initializer.Initialize();
    private Creator creator = Creator.Initialize();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        game.window = primaryStage;

        creator.createMainMenuScene();
        creator.createLevelSelectionScene();

        // Set the minimum and maximum sizes of the primary stage to allow resizing
        game.window.setMinWidth(1000);
        game.window.setMinHeight(1000);
        game.window.setMaximized(true);

        game.window.setScene(game.mainMenu);

        // Show the primary stage
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png")));
        game.window.getIcons().add(icon);
        game.window.setTitle("Deep Sea Dilemma");
        game.window.show();
        game.window.setWidth(game.window.getWidth());
        game.window.setHeight(game.window.getHeight());
    }







}

