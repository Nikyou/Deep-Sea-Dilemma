package com.deep_sea_dilemma.levels;

import com.deep_sea_dilemma.interfaces.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Creator implements LevelsStore {
    private static Creator instance = null;

    private String saveInformation;
    private Game game = Game.Game();

    private Creator()
    {

    }

    public Scene ChoseMode(int levelNumber) {

        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/3, game.buttonHeight/3);
        backButton.setStyle("-fx-font-size: 20pt;");
        backButton.setOnAction(e -> {
            game.window.setScene(game.mainMenu);

        });

        Button CPUButton = new Button("CPU");
        CPUButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        CPUButton.setStyle("-fx-font-size: 30pt;");
        CPUButton.setOnAction(e -> {
            game.window.setScene(createLevelScene(levelNumber, true));

        });

        Button playerButton = new Button("Player");
        playerButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        playerButton.setStyle("-fx-font-size: 30pt;");
        playerButton.setOnAction(e -> {
            game.window.setScene(createLevelScene(levelNumber, false));

        });

        BorderPane root = new BorderPane();
        VBox centerLayout = new VBox();

        root.setTop(backButton);
        root.setCenter(centerLayout);
        centerLayout.setAlignment(Pos.CENTER); // Center alignment for the VBox
        centerLayout.setSpacing(10); // Space between the buttons
        centerLayout.getChildren().addAll(CPUButton, playerButton);

        // Position the button
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Create the level selection scene and return it
        return new Scene(root);
    }

    private Scene createLevelScene(int levelNumber, boolean isAI) {
        // Create the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0));
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setAlignment(Pos.CENTER);

        // Create the border pane
        BorderPane root = new BorderPane();
        root.setCenter(grid);

        //Create Back button
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/3, game.buttonHeight/3);
        backButton.setStyle("-fx-font-size: 20pt;");
        backButton.setOnAction(e -> {
            game.window.setScene(game.levelSelectionScene);
            grid.getChildren().removeAll();

        });
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        //Create label
        Label label = new Label();
        label.setStyle("-fx-font-size: 30pt;");
        BorderPane.setAlignment(label, Pos.TOP_CENTER);

        BorderPane topContainer = new BorderPane();
        root.setTop(topContainer);
        topContainer.setTop(backButton);
        topContainer.setCenter(label);


        //Game variables
        AtomicInteger currentPlayer = new AtomicInteger(1);
        AtomicBoolean isNotEnd = new AtomicBoolean(true);
        int[] changeTurn = new int[] {2, 1};
        //
        label.setText(ChangeTurnString(1, isAI));

        //Set AI seed from storage
        if (isAI){
            game.ship.SetRandom(levelAISeed[levelNumber]);
        }

        //Initialize pathfinding
        game.pathfinder.Initialize(LevelsStore.levelMap[levelNumber], LevelsStore.levelShipSpeed[levelNumber]);

        // Create map
        int tileSize = levelObjectSize[levelNumber];
        double halfTileSize = tileSize / 2;
        // Populate the grid with squares
        for (int y = 0; y < LevelsStore.levelMapSize[levelNumber][0]; y++) {
            for (int x = 0; x < LevelsStore.levelMapSize[levelNumber][1]; x++) {
                // Create the four triangles that form the rectangle
                Polygon triangle1 = new Polygon(0, 0, 0, tileSize, halfTileSize, halfTileSize);
                Polygon triangle2 = new Polygon(tileSize, 0, 0, 0, halfTileSize, halfTileSize);
                Polygon triangle3 = new Polygon(tileSize, tileSize, 0, tileSize, halfTileSize, halfTileSize);
                Polygon triangle4 = new Polygon(tileSize, tileSize, tileSize, 0, halfTileSize, halfTileSize);

                // Set the fill color of each triangle to white
                triangle1.setFill(Color.WHITE);
                triangle2.setFill(Color.RED);
                triangle3.setFill(Color.BLACK);
                triangle4.setFill(Color.BLUE);

                // Add a mouse click event to each triangle
                int finalX = x;
                int finalY = y;
                triangle1.setOnMouseClicked(event -> { // Left
                    if (isNotEnd.get() && game.pathfinder.IsPathOk(finalX, finalY, game.ship.GetPosition(), game.goal.GetPosition(), "Left")){
                        game.ship.Draw(finalX, finalY);
                        switch (IsEnd()){
                            case 1 -> {
                                label.setText(WhoWonString(changeTurn[currentPlayer.get()-1], isAI));
                                isNotEnd.set(false);
                            }
                            case 2 -> {
                                label.setText(WhoWonString(currentPlayer.get(), isAI));
                                isNotEnd.set(false);
                            }
                            default -> {
                                currentPlayer.set(changeTurn[currentPlayer.get()-1]);
                                label.setText(ChangeTurnString(currentPlayer.get(), isAI));
                            }
                        }
                    }
                });
                triangle2.setOnMouseClicked(event -> { // Top
                    if (isNotEnd.get() && game.pathfinder.IsPathOk(finalX, finalY, game.ship.GetPosition(), game.goal.GetPosition(),"Top")){
                        game.ship.Draw(finalX, finalY);
                        switch (IsEnd()){
                            case 1 -> {
                                label.setText(WhoWonString(changeTurn[currentPlayer.get()-1], isAI));
                                isNotEnd.set(false);
                            }
                            case 2 -> {
                                label.setText(WhoWonString(currentPlayer.get(), isAI));
                                isNotEnd.set(false);
                            }
                            default -> {
                                currentPlayer.set(changeTurn[currentPlayer.get()-1]);
                                label.setText(ChangeTurnString(currentPlayer.get(), isAI));
                            }
                        }
                    }
                });
                triangle3.setOnMouseClicked(event -> {  // Bottom
                    if (isNotEnd.get() && game.pathfinder.IsPathOk(finalX, finalY, game.ship.GetPosition(), game.goal.GetPosition(),"Bottom")){
                        game.ship.Draw(finalX, finalY);
                        switch (IsEnd()){
                            case 1 -> {
                                label.setText(WhoWonString(changeTurn[currentPlayer.get()-1], isAI));
                                isNotEnd.set(false);
                            }
                            case 2 -> {
                                label.setText(WhoWonString(currentPlayer.get(), isAI));
                                isNotEnd.set(false);
                            }
                            default -> {
                                currentPlayer.set(changeTurn[currentPlayer.get()-1]);
                                label.setText(ChangeTurnString(currentPlayer.get(), isAI));
                            }
                        }
                    }
                });
                triangle4.setOnMouseClicked(event -> {  // Right
                    if (isNotEnd.get() && game.pathfinder.IsPathOk(finalX, finalY, game.ship.GetPosition(), game.goal.GetPosition(),"Right")){
                        game.ship.Draw(finalX, finalY);
                        switch (IsEnd()){
                            case 1 -> {
                                label.setText(WhoWonString(changeTurn[currentPlayer.get()-1], isAI));
                                isNotEnd.set(false);
                            }
                            case 2 -> {
                                label.setText(WhoWonString(currentPlayer.get(), isAI));
                                isNotEnd.set(false);
                            }
                            default -> {
                                currentPlayer.set(changeTurn[currentPlayer.get()-1]);
                                label.setText(ChangeTurnString(currentPlayer.get(), isAI));
                            }
                        }
                    }
                });

                // Add the four triangles to the grid
                grid.add(triangle1, x, y);
                grid.add(triangle2, x, y);
                grid.add(triangle3, x, y);
                grid.add(triangle4, x, y);

                // Set the position of each triangle to form the rectangle
                triangle1.setTranslateX(triangle1.getTranslateX());
                triangle1.setTranslateY(triangle1.getTranslateY());
                triangle2.setTranslateX(triangle2.getTranslateX());
                triangle2.setTranslateY(triangle2.getTranslateY() - halfTileSize/2);
                triangle3.setTranslateX(triangle3.getTranslateX());
                triangle3.setTranslateY(triangle3.getTranslateY() + halfTileSize/2);
                triangle4.setTranslateX(triangle4.getTranslateX() + halfTileSize);
                triangle4.setTranslateY(triangle4.getTranslateY());

                switch (LevelsStore.levelMap[levelNumber][y][x]){
                    case 'T' -> game.tile.Draw(x, y, tileSize, tileSize, grid);
                    case 'S' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.ship.Draw(x, y, tileSize, tileSize, grid);
                    }
                    case 'G' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.goal.Draw(x, y, tileSize, tileSize, grid);
                    }
                    case 'R' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.rock.Draw(x, y, tileSize, tileSize, grid);
                    }
                    case 'V' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.vortex.Draw(x, y, tileSize, tileSize, grid);
                    }
                }
            }
        }

        // Create the scene
        return new Scene(root);
    }

    public Scene createTutorialLevelScene() {

        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/3, game.buttonHeight/3);
        backButton.setStyle("-fx-font-size: 20pt;");


        // Create the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0));
        grid.setHgap(0);
        grid.setVgap(0);

        // Create the button

        // Create the border pane and add the button to it
        BorderPane root = new BorderPane();
        root.setTop(backButton);
        root.setCenter(grid);
        grid.setAlignment(Pos.CENTER);

        backButton.setOnAction(e -> {
            game.window.setScene(game.levelSelectionScene);
            grid.getChildren().removeAll();

        });

        //Initialize pathfinding
        game.pathfinder.Initialize(LevelsStore.levelMap[0], LevelsStore.levelShipSpeed[0]);

        // Add the grid to the center of the border pane
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        int tileSize = levelObjectSize[0];
        double halfTileSize = tileSize / 2;
        // Populate the grid with squares
        for (int y = 0; y < LevelsStore.levelMapSize[0][0]; y++) {
            for (int x = 0; x < LevelsStore.levelMapSize[0][1]; x++) {
                // Create the four triangles that form the rectangle
                Polygon triangle1 = new Polygon(0, 0, 0, tileSize, halfTileSize, halfTileSize);
                Polygon triangle2 = new Polygon(tileSize, 0, 0, 0, halfTileSize, halfTileSize);
                Polygon triangle3 = new Polygon(tileSize, tileSize, 0, tileSize, halfTileSize, halfTileSize);
                Polygon triangle4 = new Polygon(tileSize, tileSize, tileSize, 0, halfTileSize, halfTileSize);

                // Set the fill color of each triangle to white
                triangle1.setFill(Color.WHITE);
                triangle2.setFill(Color.RED);
                triangle3.setFill(Color.BLACK);
                triangle4.setFill(Color.BLUE);

                // Add a mouse click event to each triangle
                int finalX = x;
                int finalY = y;
                triangle1.setOnMouseClicked(event -> { // Left
                    triangle1.setFill(Color.WHITE);
                    triangle2.setFill(Color.WHITE);
                    triangle3.setFill(Color.WHITE);
                    triangle4.setFill(Color.WHITE);
                    game.ship.Draw(finalX, finalY);
                });
                triangle2.setOnMouseClicked(event -> { // Top
                    triangle1.setFill(Color.RED);
                    triangle2.setFill(Color.RED);
                    triangle3.setFill(Color.RED);
                    triangle4.setFill(Color.RED);
                    game.ship.Draw(finalX, finalY);
                });
                triangle3.setOnMouseClicked(event -> {  // Bottom
                    triangle1.setFill(Color.BLACK);
                    triangle2.setFill(Color.BLACK);
                    triangle3.setFill(Color.BLACK);
                    triangle4.setFill(Color.BLACK);
                    game.ship.Draw(finalX, finalY);
                });
                triangle4.setOnMouseClicked(event -> {  // Right
                    triangle1.setFill(Color.BLUE);
                    triangle2.setFill(Color.BLUE);
                    triangle3.setFill(Color.BLUE);
                    triangle4.setFill(Color.BLUE);
                    game.ship.Draw(finalX, finalY);
                });

                // Add the four triangles to the grid
                grid.add(triangle1, x, y);
                grid.add(triangle2, x, y);
                grid.add(triangle3, x, y);
                grid.add(triangle4, x, y);

                // game.tile.Draw(i,j, rectSize, rectSize, grid);
                // Set the position of each triangle to form the rectangle
                triangle1.setTranslateX(triangle1.getTranslateX());
                triangle1.setTranslateY(triangle1.getTranslateY());
                triangle2.setTranslateX(triangle2.getTranslateX());
                triangle2.setTranslateY(triangle2.getTranslateY() - halfTileSize/2);
                triangle3.setTranslateX(triangle3.getTranslateX());
                triangle3.setTranslateY(triangle3.getTranslateY() + halfTileSize/2);
                triangle4.setTranslateX(triangle4.getTranslateX() + halfTileSize);
                triangle4.setTranslateY(triangle4.getTranslateY());

                switch (LevelsStore.levelMap[0][y][x]){
                    case 'T' -> game.tile.Draw(x, y, tileSize, tileSize, grid);
                    case 'S' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.ship.Draw(x, y, tileSize, tileSize, grid);
                    }
                    case 'G' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.goal.Draw(x, y, tileSize, tileSize, grid);
                    }
                    case 'R' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.rock.Draw(x, y, tileSize, tileSize, grid);
                    }
                    case 'V' -> {
                        game.tile.Draw(x, y, tileSize, tileSize, grid);
                        game.vortex.Draw(x, y, tileSize, tileSize, grid);
                    }
                }
            }
        }

        // Create the scene
        return new Scene(root);
    }

    private void ReadSave(){

    }

    private void SetShop(){

    }

    public void createMainMenuScene() {
        Button levelSelectButton = new Button("Level Select");
        Button shopButton = new Button("Shop");
        Button exitButton = new Button("Exit");

        levelSelectButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        shopButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        exitButton.setPrefSize(game.buttonWidth, game.buttonHeight);

        levelSelectButton.setStyle("-fx-font-size: 30pt;");
        shopButton.setStyle("-fx-font-size: 30pt;");
        exitButton.setStyle("-fx-font-size: 30pt;");

        // Add functionality to the buttons
        levelSelectButton.setOnAction(e -> {
            game.window.setScene(game.levelSelectionScene);
        });

        shopButton.setOnAction(e -> {

        });

        exitButton.setOnAction(e -> {
            game.window.close(); // Close the application
        });

        // Create the main menu layout
        VBox mainMenuLayout = new VBox(20); // 20 is the spacing between elements
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(levelSelectButton, shopButton, exitButton);

        // Create the scene and set it as the primary stage's scene
        game.mainMenu = new Scene(mainMenuLayout);

    }

    public void createLevelSelectionScene() {
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/3, game.buttonHeight/3);
        backButton.setStyle("-fx-font-size: 20pt;");
        backButton.setOnAction(e -> game.window.setScene(game.mainMenu));

        Button centerButton = new Button("Center Button");
        centerButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        centerButton.setStyle("-fx-font-size: 30pt;");
        centerButton.setOnAction(e -> game.window.setScene(ChoseMode(1)));


        BorderPane root = new BorderPane();
        // Create the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(50);
        grid.setVgap(50);

        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                i++;
                // Create the level button
                Button levelButton = new Button("Level " + i);
                levelButton.setPrefSize(game.buttonWidth, game.buttonHeight);
                levelButton.setStyle("-fx-font-size: 30pt;");

                // Add an action to the button
                int finalI = i;
                levelButton.setOnAction(e -> game.window.setScene(ChoseMode(finalI)));

                // Add the four triangles to the grid
                grid.add(levelButton, x, y);
            }
        }

        // Create tutorial button
        Button tutorialButton = new Button("Tutorial");
        tutorialButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        tutorialButton.setStyle("-fx-font-size: 30pt;");
        tutorialButton.setOnAction(e -> game.window.setScene(createTutorialLevelScene()));
        grid.add(tutorialButton, 1, 3);


        root.setTop(backButton);
        root.setCenter(grid);

        // Position the back button
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Create the scene
        game.levelSelectionScene = new Scene(root);

    }

    //In level methods
    private int IsEnd(){
        int[] shipPos = game.ship.GetPosition();
        int[] goalPos = game.goal.GetPosition();
        if (shipPos[0] == goalPos[0] && shipPos[1] == goalPos[1]) {return 1;} //True, current player lost
        if ((Math.abs(goalPos[0] - shipPos[0]) + Math.abs(goalPos[1] - shipPos[1]) == 1)) {return 2;} //True, next player lost
        return 0; //false
    }
    private String WhoWonString(int playerNumber, boolean IsAI){
        switch (playerNumber){
            case 1 -> {
                if (IsAI){
                    return "You win!";
                }else{
                    return "Player 1 won!";
                }
            }
            case 2 -> {
                if (IsAI){
                    return "You lost!";
                }else{
                    return "Player 2 won!";
                }
            }
            default -> {
                return "";
            }
        }
    }
    private String ChangeTurnString(int playerNumber, boolean IsAI){
        switch (playerNumber){
            case 1 -> {
                if (IsAI){
                    return "Your turn";
                }else{
                    return "Player 1 turn";
                }
            }
            case 2 -> {
                if (IsAI){
                    return "CPU turn";
                }else{
                    return "Player 2 turn";
                }
            }
            default -> {
                return "";
            }
        }
    }



    // Static method to create instance of Initializer class
    public static Creator Creator()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Creator();

        return instance;
    }
}