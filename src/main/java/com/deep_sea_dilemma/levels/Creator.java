package com.deep_sea_dilemma.levels;

import com.deep_sea_dilemma.interfaces.Cosmetics;
import com.deep_sea_dilemma.interfaces.Game;
import com.deep_sea_dilemma.objects.Arrow;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Creator implements LevelsStore {
    private static Creator instance = null;
    private final Game game = Game.Initialize();

    private Creator()
    {

    }

    public Scene ChoseMode(int levelNumber) {

        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/2, game.buttonHeight/2);
        backButton.setOnAction(e -> game.window.setScene(game.levelSelectionScene));

        Button CPUButton = new Button("CPU");
        CPUButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        CPUButton.setOnAction(e -> game.window.setScene(createLevelScene(levelNumber, true)));

        Button playerButton = new Button("Player");
        playerButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        playerButton.setOnAction(e -> game.window.setScene(createLevelScene(levelNumber, false)));

        BorderPane root = new BorderPane();
        VBox centerLayout = new VBox();

        root.setTop(backButton);
        root.setCenter(centerLayout);
        centerLayout.setAlignment(Pos.CENTER); // Center alignment for the VBox
        centerLayout.setSpacing(30); // Space between the buttons
        centerLayout.getChildren().addAll(CPUButton, playerButton);

        // Position the button
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Set styles for buttons
        backButton.getStyleClass().add("back");

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

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

        //Game variable
        AtomicBoolean canMove = new AtomicBoolean(true);

        //Create Back button
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/2, game.buttonHeight/2);
        backButton.setOnAction(e -> {
            if (canMove.get()) {
                game.window.setScene(game.levelSelectionScene);
                grid.getChildren().removeAll();
            }
        });
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        //Create label
        Label turnOrder = new Label();
        BorderPane.setAlignment(turnOrder, Pos.TOP_CENTER);

        BorderPane topContainer = new BorderPane();
        root.setTop(topContainer);
        topContainer.setTop(backButton);
        topContainer.setCenter(turnOrder);

        //Game variables
        AtomicInteger currentPlayer = new AtomicInteger(1);
        AtomicBoolean isNotEnd = new AtomicBoolean(true);
        int[] changeTurn = new int[] {2, 1};

        turnOrder.setText(ChangeTurnString(1, isAI));

        // Create map
        int tileSize = levelObjectSize[levelNumber];
        double halfTileSize = tileSize / 2;
        // Create arrow
        game.arrow = new Arrow(grid,tileSize);

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
                    if (canMove.get() &&
                            game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(), "Left", currentPlayer.get(), isAI)){
                        MakeTurnPlayer(finalX, finalY, "Left", turnOrder, changeTurn, currentPlayer,
                                isNotEnd, canMove, isAI, LevelsStore.levelAIDifficulty[levelNumber], levelNumber);
                        if(game.arrow.IsShown()){
                            game.arrow.Clear();
                        }
                    }
                });
                triangle1.setOnMouseEntered(event -> {
                    if (canMove.get()){
                        if(game.arrow.IsShown()){
                            game.arrow.Clear();
                        }
                        if(!game.arrow.IsShown() && game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(),"Left", currentPlayer.get(), isAI)){
                            game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], finalX, finalY, "Left");
                        }
                    }
                });

                triangle2.setOnMouseClicked(event -> { // Top
                    if (canMove.get() &&
                            game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(),"Top", currentPlayer.get(), isAI)){
                        MakeTurnPlayer(finalX, finalY, "Top", turnOrder, changeTurn, currentPlayer,
                                isNotEnd, canMove, isAI, LevelsStore.levelAIDifficulty[levelNumber], levelNumber);
                        if(game.arrow.IsShown()){
                            game.arrow.Clear();
                        }
                    }
                });
                triangle2.setOnMouseEntered(event -> {
                    if (canMove.get()) {
                        if (game.arrow.IsShown()) {
                            game.arrow.Clear();
                        }
                        if (!game.arrow.IsShown() && game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(), "Top", currentPlayer.get(), isAI)) {
                            game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], finalX, finalY, "Top");
                        }
                    }
                });

                triangle3.setOnMouseClicked(event -> {  // Bottom
                    if (canMove.get() &&
                            game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(),"Bottom", currentPlayer.get(), isAI)){
                        MakeTurnPlayer(finalX, finalY, "Bottom", turnOrder, changeTurn, currentPlayer,
                                isNotEnd, canMove, isAI, LevelsStore.levelAIDifficulty[levelNumber], levelNumber);
                        if(game.arrow.IsShown()){
                            game.arrow.Clear();
                        }
                    }
                });
                triangle3.setOnMouseEntered(event -> {
                    if (canMove.get()) {
                        if (game.arrow.IsShown()) {
                            game.arrow.Clear();
                        }
                        if (!game.arrow.IsShown() && game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(), "Bottom", currentPlayer.get(), isAI)) {
                            game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], finalX, finalY, "Bottom");
                        }
                    }
                });

                triangle4.setOnMouseClicked(event -> {  // Right
                    if (canMove.get() &&
                            game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(),"Right", currentPlayer.get(), isAI)){
                        MakeTurnPlayer(finalX, finalY, "Right", turnOrder, changeTurn, currentPlayer,
                                isNotEnd, canMove, isAI, LevelsStore.levelAIDifficulty[levelNumber], levelNumber);
                        if(game.arrow.IsShown()){
                            game.arrow.Clear();
                        }
                    }
                });
                triangle4.setOnMouseEntered(event -> {
                    if (canMove.get()) {
                        if (game.arrow.IsShown()) {
                            game.arrow.Clear();
                        }
                        if (!game.arrow.IsShown() && game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(), "Right", currentPlayer.get(), isAI)) {
                            game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], finalX, finalY, "Right");
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


                game.DrawTile(x, y, tileSize, tileSize, grid);

                switch (LevelsStore.levelMap[levelNumber][y][x]){
                    case 'S' -> game.DrawShip(x, y, tileSize, tileSize, grid);
                    case 'G' -> game.DrawGoal(x, y, tileSize, tileSize, grid);
                    case 'R' -> game.DrawRock(x, y, tileSize, tileSize, grid);
                    case 'V' -> game.DrawVortex(x, y, tileSize, tileSize, grid);
                }
            }
        }

        //Initialize pathfinding
        game.pathfinder.Initialize(LevelsStore.levelMap[levelNumber], LevelsStore.levelShipSpeed[levelNumber], LevelsStore.levelMapSize[levelNumber], game.goal.GetPosition());

        //Set AI settings
        if (isAI){
            game.ship.SetRandom(levelAISeed[levelNumber]);
            game.pathfinder.CalculateWinPositions();
        }

        //DEBUGGING
        /*for (int i = 0; i < game.pathfinder.winPositions.size(); i++){
            int [] cord = game.pathfinder.winPositions.get(i);
            Rectangle rectangle = new Rectangle(halfTileSize, halfTileSize);
            rectangle.setFill(Color.GREEN);
            grid.add(rectangle, cord[0], cord[1]);
        }*/


        // Set styles for button
        backButton.getStyleClass().add("back");

        // Set styles for label
        turnOrder.getStyleClass().add("turn-order");

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene
        return new Scene(root);
    }

    public Scene createTutorialLevelScene() {

        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/2, game.buttonHeight/2);


        // Create the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0));
        grid.setHgap(0);
        grid.setVgap(0);


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
        game.pathfinder.Initialize(LevelsStore.levelMap[0], LevelsStore.levelShipSpeed[0], LevelsStore.levelMapSize[0], game.goal.GetPosition());

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

        // Set styles for buttons
        backButton.getStyleClass().add("back");

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene
        return new Scene(root);
    }

    public void createMainMenuScene() {
        Button levelSelectButton = new Button("Select Level");
        Button shopButton = new Button("Shop");
        Button exitButton = new Button("Exit");

        levelSelectButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        shopButton.setPrefSize(game.buttonWidth, game.buttonHeight);
        exitButton.setPrefSize(game.buttonWidth, game.buttonHeight);


        // Add functionality to the buttons
        levelSelectButton.setOnAction(e -> game.window.setScene(game.levelSelectionScene));

        shopButton.setOnAction(e ->  game.window.setScene(createShop()));

        exitButton.setOnAction(e -> {
            game.Save();
            game.window.close(); // Close the application
        });

        //Create label
        Label mainMenuLabel = new Label("Deep Sea Dilemma");
        mainMenuLabel.getStyleClass().add("menu-title");
        BorderPane.setAlignment(mainMenuLabel, Pos.CENTER);

        // Create the main menu layout
        VBox mainMenuLayout = new VBox(40); // 40 is the spacing between elements
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(levelSelectButton, shopButton, exitButton);

        BorderPane root = new BorderPane();
        BorderPane topBar = new BorderPane();
        root.setTop(topBar);
        topBar.setBottom(mainMenuLabel);
        root.setCenter(mainMenuLayout);

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene and set it as the primary stage's scene
        game.mainMenu = new Scene(root);

    }

    public void createLevelSelectionScene() {
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/2, game.buttonHeight/2);
        backButton.setOnAction(e -> game.window.setScene(game.mainMenu));

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
        tutorialButton.setOnAction(e -> game.window.setScene(createTutorialLevelScene()));
        grid.add(tutorialButton, 1, 3);


        root.setTop(backButton);
        root.setCenter(grid);

        // Position the back button
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Set styles for buttons
        backButton.getStyleClass().add("back");

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene
        game.levelSelectionScene = new Scene(root);

    }

    public Scene createShop(){
        // Create the grid
        GridPane grid = new GridPane();
        grid.setMinSize(6,7);
        grid.setPadding(new Insets(0));
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Create the border pane
        BorderPane root = new BorderPane();
        root.setCenter(grid);

        //Create Back button
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.buttonWidth/2, game.buttonHeight/2);
        backButton.setOnAction(e -> {
            game.window.setScene(game.mainMenu);
            grid.getChildren().removeAll();

        });
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        //Create labels
        Label titleLabel = new Label("Shop");
        BorderPane.setAlignment(titleLabel, Pos.TOP_CENTER);

        Label goldLabel = new Label(game.settings.gold + " Gold");
        BorderPane.setAlignment(goldLabel, Pos.TOP_CENTER);

        BorderPane topContainer = new BorderPane();
        root.setTop(topContainer);
        topContainer.setTop(backButton);
        topContainer.setCenter(titleLabel);
        topContainer.setRight(goldLabel);


        //Create labels for shop
        List<Label> itemNames = new ArrayList<>();
        Label labelShip = new Label("Ship");
        labelShip.setAlignment(Pos.CENTER);
        itemNames.add(labelShip);

        Label labelTile = new Label("Tile");
        labelTile.setAlignment(Pos.CENTER);
        itemNames.add(labelTile);

        Label labelGoal = new Label("Goal");
        labelGoal.setAlignment(Pos.CENTER);
        itemNames.add(labelGoal);

        Label labelVortex = new Label("Vortex");
        labelVortex.setAlignment(Pos.CENTER);
        itemNames.add(labelVortex);

        Label labelRock = new Label("Rock");
        labelRock.setAlignment(Pos.CENTER);
        itemNames.add(labelRock);

        for (int x = 1; x <=5; x++){
            grid.add(itemNames.get(x-1), x, 0);
        }

        //Prices
        Label price1Row = new Label(Integer.toString(game.settings.cosmeticsPrice[1]));
        grid.add(price1Row, 0, 4);
        Label price2Row = new Label(Integer.toString(game.settings.cosmeticsPrice[2]));
        grid.add(price2Row, 0, 6);

        int tileSize = 150;
        //Ship images
        AddShopImages(grid, game.settings.imageShips, tileSize, 1);
        //Tile images
        AddShopImages(grid, game.settings.imageTiles, tileSize, 2);
        //Goal images
        AddShopImages(grid, game.settings.imageGoals, tileSize, 3);
        //Vortex images
        AddShopImages(grid, game.settings.imageVortices, tileSize, 4);
        //Rock images
        AddShopImages(grid, game.settings.imageRocks, tileSize, 5);

        // Create buy buttons
        for (int x = 1; x <=5; x++){
            int y = 2;
            for (int i = 0; i <3; i++){
                Button button = new Button("Buy");
                button.setPrefSize(game.buttonWidth/2, game.buttonHeight/2);
                button.getStyleClass().add("shop");
                grid.add(button, x, y);
                y = y+2;

                int finalX = x-1;
                int finalI = i;
                switch (finalX){
                    case 0 -> {
                        if (game.settings.unlockedShips[i]){
                            button.setText("Select");
                        }
                        if (i == game.settings.selectedCosmetic[finalX]){
                            button.setText("Selected");
                        }
                    }
                    case 1 -> {
                        if (game.settings.unlockedTiles[i]){
                            button.setText("Select");
                        }
                        if (i == game.settings.selectedCosmetic[finalX]){
                            button.setText("Selected");
                        }
                    }
                    case 2 -> {
                        if (game.settings.unlockedGoals[i]){
                            button.setText("Select");
                        }
                        if (i == game.settings.selectedCosmetic[finalX]){
                            button.setText("Selected");
                        }
                    }
                    case 3 -> {
                        if (game.settings.unlockedVortices[i]){
                            button.setText("Select");
                        }
                        if (i == game.settings.selectedCosmetic[finalX]){
                            button.setText("Selected");
                        }
                    }
                    case 4 -> {
                        if (game.settings.unlockedRocks[i]){
                            button.setText("Select");
                        }
                        if (i == game.settings.selectedCosmetic[finalX]){
                            button.setText("Selected");
                        }
                    }
                }
                button.setOnAction(e -> {
                    boolean change = false;
                    if (button.getText().equals("Select")){
                        Cosmetics.SelectCosmetic(finalX, finalI);
                        change = true;
                    }
                    if (button.getText().equals("Buy") && Cosmetics.IsEnoughGold(finalI)){
                        Cosmetics.UnlockCosmetic(finalX, finalI);
                        change = true;
                    }
                    if (change) {
                        game.window.setScene(createShop());
                    }
                });
            }
        }


        // Set styles for buttons
        backButton.getStyleClass().add("back");

        // Set styles for labels
        price1Row.getStyleClass().add("shop-price");
        price2Row.getStyleClass().add("shop-price");
        titleLabel.getStyleClass().add("shop-title");
        goldLabel.getStyleClass().add("shop-gold-counter");
        for (int x = 0; x <5; x++){
            itemNames.get(x).getStyleClass().add("shop-item");
        }

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene
        return new Scene(root);
    }

    //In level methods
    private int IsEnd(){
        int[] shipPos = game.ship.GetPosition();
        int[] goalPos = game.goal.GetPosition();
        if (shipPos[0] == goalPos[0] && shipPos[1] == goalPos[1]) {return 1;} //True, current player lost
        if ((Math.abs(goalPos[0] - shipPos[0]) + Math.abs(goalPos[1] - shipPos[1]) == 1)) {return 2;} //True, next player lost
        return 0; //false
    }
    private String WhoWonString(int playerNumber, boolean IsAI, int levelNumber){
        switch (playerNumber){
            case 1 -> {
                if (IsAI){
                    game.saver.EndLevelSave(levelNumber);
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

    private void MakeTurnPlayer(int finalX, int finalY, String orientation, Label label, int[] changeTurn, AtomicInteger currentPlayer,
                                AtomicBoolean isNotEnd, AtomicBoolean canMove, boolean isAI, int AIDifficulty, int levelNumber){
        int[] tempcord = game.pathfinder.GetVortexPosition(game.ship.GetPosition()[0], game.ship.GetPosition()[1], finalX, finalY, orientation);
        if (tempcord[0] != -1){
            finalX = tempcord[0];
            finalY = tempcord[1];
        }
        game.DrawShip(finalX, finalY);
        switch (IsEnd()){
            case 1 -> {
                label.setText(WhoWonString(changeTurn[currentPlayer.get()-1], isAI, levelNumber));
                isNotEnd.set(false);
            }
            case 2 -> {
                label.setText(WhoWonString(currentPlayer.get(), isAI, levelNumber));
                isNotEnd.set(false);
            }
            default -> {
                currentPlayer.set(changeTurn[currentPlayer.get()-1]);
                label.setText(ChangeTurnString(currentPlayer.get(), isAI));
                if (isAI){
                    canMove.set(false);
                    MakeTurnAI(label,  changeTurn,  currentPlayer,  isNotEnd, canMove, AIDifficulty, levelNumber);
                }
            }
        }
    }

    private void MakeTurnAI(Label label, int[] changeTurn, AtomicInteger currentPlayer, AtomicBoolean isNotEnd,
                            AtomicBoolean canMove, int AIDifficulty, int levelNumber){
        int[] cord = game.AIMakeTurn(AIDifficulty);

        if (game.arrow.IsShown()) {
            game.arrow.Clear();
        }
        String orientation = "Left";
        if (game.pathfinder.IsPathOkRock(cord[0], cord[1], game.ship.GetPosition(), "Top") &&
                game.pathfinder.IsPathOkOrient(cord[0], cord[1], game.ship.GetPosition(), "Top")) {
            orientation = "Top";
        }
        if (game.pathfinder.IsPathOkRock(cord[0], cord[1], game.ship.GetPosition(), "Right") &&
                game.pathfinder.IsPathOkOrient(cord[0], cord[1], game.ship.GetPosition(), "Right")) {
            orientation = "Right";
        }
        if (game.pathfinder.IsPathOkRock(cord[0], cord[1], game.ship.GetPosition(), "Bottom") &&
                game.pathfinder.IsPathOkOrient(cord[0], cord[1], game.ship.GetPosition(), "Bottom")) {
            orientation = "Bottom";
        }

        // Delay
        PauseTransition delay1 = new PauseTransition(Duration.seconds(1));
        PauseTransition delay2 = new PauseTransition(Duration.seconds(3));

        String finalOrientation = orientation;
        delay2.setOnFinished(event -> {
            // Code to be executed after the delay
            int[] tempcord = game.pathfinder.GetVortexPosition(game.ship.GetPosition()[0], game.ship.GetPosition()[1], cord[0], cord[1], finalOrientation);
            if (tempcord[0] != -1){
                cord[0] = tempcord[0];
                cord[1] = tempcord[1];
            }

            game.DrawShip(cord[0], cord[1]);
            game.arrow.Clear();
            canMove.set(true);

            switch (IsEnd()){
                case 1 -> {
                    label.setText(WhoWonString(changeTurn[currentPlayer.get()-1], true, levelNumber));
                    isNotEnd.set(false);
                }
                case 2 -> {
                    label.setText(WhoWonString(currentPlayer.get(), true, levelNumber));
                    isNotEnd.set(false);
                }
                default -> {
                    currentPlayer.set(changeTurn[currentPlayer.get()-1]);
                    label.setText(ChangeTurnString(currentPlayer.get(), true));
                }
            }
        });

        delay1.setOnFinished(event -> {
            // Code to be executed after the delay
            game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], cord[0], cord[1], finalOrientation);
            delay2.play();
        });

        delay1.play();

    }

    private void AddShopImages(GridPane grid, Image[] images, int tileSize, int x){
        int i = 0;
        for (int y = 1; y<=5; y = y+ 2){
            ImageView imageView = new ImageView(images[i]);
            imageView.setFitWidth(tileSize);
            imageView.setFitHeight(tileSize);
            imageView.setMouseTransparent(false);
            grid.add(imageView, x, y);
            i++;
        }

    }


    // Static method to create instance of Initializer class
    public static Creator Initialize()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Creator();

        return instance;
    }
}