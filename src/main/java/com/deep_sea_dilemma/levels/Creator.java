package com.deep_sea_dilemma.levels;

import com.deep_sea_dilemma.background.Background;
import com.deep_sea_dilemma.interfaces.Cosmetics;
import com.deep_sea_dilemma.interfaces.Game;
import com.deep_sea_dilemma.objects.Arrow;
import com.deep_sea_dilemma.objects.Entity;
import com.deep_sea_dilemma.settings.Settings;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
        backButton.setPrefSize(game.getButtonWidth()/2, game.getButtonHeight()/2);
        backButton.setOnAction(e -> game.window.setScene(createLevelSelectionScene()));

        Button CPUButton = new Button("CPU");
        CPUButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());
        CPUButton.setOnAction(e -> game.window.setScene(createLevelScene(levelNumber, true)));

        Button playerButton = new Button("Player");
        playerButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());
        playerButton.setOnAction(e -> game.window.setScene(createLevelScene(levelNumber, false)));

        // Create the stack pane
        StackPane root = new StackPane();

        // Background image
        ImageView background = Background.GetBackgroundLevelSelector();
        background.fitWidthProperty().bind(game.window.widthProperty());
        background.fitHeightProperty().bind(game.window.heightProperty());
        root.getChildren().add(background);

        BorderPane rootBorder = new BorderPane();
        root.getChildren().add(rootBorder);
        background.toBack();
        VBox centerLayout = new VBox();

        BorderPane topContainer = new BorderPane();
        topContainer.setPadding(new Insets(20));
        topContainer.setTop(backButton);
        rootBorder.setTop(topContainer);


        rootBorder.setCenter(centerLayout);
        centerLayout.setAlignment(Pos.CENTER); // Center alignment for the VBox
        centerLayout.setSpacing(30); // Space between the buttons
        centerLayout.getChildren().addAll(CPUButton, playerButton);

        // Position the button
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Set styles for buttons
        backButton.getStyleClass().add("back");

        // Add css file to scene
        rootBorder.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the level selection scene and return it
        return new Scene(root);
    }

    private Scene createLevelScene(int levelNumber, boolean isAI) {
        // Create the stack pane
        StackPane root = new StackPane();

        // Background image
        ImageView background = Background.GetBackgroundLevel();
        background.fitWidthProperty().bind(game.window.widthProperty());
        background.fitHeightProperty().bind(game.window.heightProperty());
        root.getChildren().add(background);

        // Create the border pane
        BorderPane rootBorder = new BorderPane();
        root.getChildren().add(rootBorder);
        background.toBack();

        // Create the grid
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setAlignment(Pos.CENTER);
        rootBorder.setCenter(grid);

        //Game variable
        AtomicBoolean canMove = new AtomicBoolean(true);

        //Create Back button
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.getButtonWidth()/2, game.getButtonHeight()/2);
        backButton.setOnAction(e -> {
            if (canMove.get()) {
                game.window.setScene(createLevelSelectionScene());
                grid.getChildren().removeAll();
            }
        });
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        //Create labels
        Label turnOrder = new Label();
        BorderPane.setAlignment(turnOrder, Pos.TOP_CENTER);
        Label goldLabel = new Label(game.settings.gold + " Gold");
        Label speedLabel = new Label("Speed: " + LevelsStore.levelShipSpeed[levelNumber]);

        // Create top container
        BorderPane topContainer = new BorderPane();
        topContainer.setPadding(new Insets(20));

        //Create stack pane for labels on top right
        GridPane topRightContainer = new GridPane();
        topRightContainer.add(goldLabel, 0 , 0);
        topRightContainer.add(speedLabel, 0 , 1);


        rootBorder.setTop(topContainer);
        topContainer.setTop(backButton);
        topContainer.setCenter(turnOrder);
        topContainer.setRight(topRightContainer);

        //Game variables
        AtomicInteger currentPlayer = new AtomicInteger(1);
        AtomicBoolean isNotEnd = new AtomicBoolean(true);
        final int[] changeTurn = new int[] {2, 1};
        turnOrder.setText(ChangeTurnString(1, isAI));

        // Create map
        int tileSize = levelObjectSize[levelNumber];
        double halfTileSize = tileSize / 2;
        grid.setPadding(new Insets(0, tileSize + halfTileSize, 0 , 0));
        // Create arrow
        game.arrow = new Arrow(grid,tileSize);

        // Populate the grid with squares
        for (int y = 0; y < LevelsStore.levelMapSize[levelNumber][0]; y++) {
            for (int x = 0; x < LevelsStore.levelMapSize[levelNumber][1]; x++) {
                // Create the four triangles that form the rectangle
                Polygon triangle1 = new Polygon(0, 0, 0, tileSize, halfTileSize, halfTileSize); // Left
                Polygon triangle2 = new Polygon(tileSize, 0, 0, 0, halfTileSize, halfTileSize); // Top
                Polygon triangle3 = new Polygon(tileSize, tileSize, 0, tileSize, halfTileSize, halfTileSize);   // Bottom
                Polygon triangle4 = new Polygon(tileSize, tileSize, tileSize, 0, halfTileSize, halfTileSize);   // Right

                List<Polygon> buttons = new ArrayList<>();
                buttons.add(triangle1);
                buttons.add(triangle2);
                buttons.add(triangle3);
                buttons.add(triangle4);

                // Add a mouse click event to each triangle
                int finalX = x;
                int finalY = y;
                for (int i = 0; i<4; i++){
                    String orient = "Left";
                    switch (i){
                        case 0 -> orient = "Left";
                        case 1 -> orient = "Top";
                        case 2 -> orient = "Bottom";
                        case 3 -> orient = "Right";
                    }

                    String finalOrient = orient;
                    buttons.get(i).setOnMouseClicked(event -> {
                        if (canMove.get() &&
                                game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(), finalOrient, currentPlayer.get(), isAI)){
                            MakeTurnPlayer(finalX, finalY, finalOrient, turnOrder, changeTurn, currentPlayer,
                                    isNotEnd, canMove, isAI, LevelsStore.levelAIDifficulty[levelNumber], levelNumber);
                            if(game.arrow.IsShown()){
                                game.arrow.Clear();
                            }
                            goldLabel.setText(game.settings.gold + " Gold");
                        }
                    });
                    buttons.get(i).setOnMouseEntered(event -> {
                        if (canMove.get()){
                            if(game.arrow.IsShown()){
                                game.arrow.Clear();
                            }
                            if(!game.arrow.IsShown() && game.pathfinder.IsTurnLegal(isNotEnd.get(), finalX, finalY, game.ship.GetPosition(),finalOrient, currentPlayer.get(), isAI)){
                                game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], finalX, finalY, finalOrient);
                            }
                        }
                    });
                }

                // Add the four triangles to the grid
                for (int i = 0; i < 4; i++) {
                    grid.add(buttons.get(i), x, y);
                }

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
        goldLabel.getStyleClass().add("shop-gold-counter");
        speedLabel.getStyleClass().add("ship-speed");

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        System.gc();

        // Create the scene
        return new Scene(root);
    }

    public Scene createTutorialLevelScene() {

        String text0 = "Welcome to the game. Your goal is to make the other player get to the treasure first, however, you have to get closer to it each turn. You can see your speed in top right corner. Try to get to the marked square. Notice that you can move first horizontally or vertically.";
        String text1 = "As you saw, your opponent tried to move to a different square but was caught in a vortex. Vortices stop your ship if they are in your path. Now move to the marked square. Notice the rock blocking your path from one direction.";
        String text2 = "Your opponent reached the treasure and thus you won! Generally, you want to stop on squares that are adjacent to the goal to force your opponent to move into the goal. After completing each level once against CPU, you earn gold that can be spent in the shop.\nGood luck!";

        // Create the stack pane
        StackPane root = new StackPane();

        // Background image
        ImageView background = Background.GetBackgroundLevel();
        background.fitWidthProperty().bind(game.window.widthProperty());
        background.fitHeightProperty().bind(game.window.heightProperty());
        root.getChildren().add(background);

        // Create the border pane
        BorderPane rootBorder = new BorderPane();
        root.getChildren().add(rootBorder);
        background.toBack();

        // Create the grid
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setAlignment(Pos.CENTER);
        rootBorder.setCenter(grid);

        //Game variable
        AtomicBoolean canMove = new AtomicBoolean(true);

        //Create Back button
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.getButtonWidth()/2, game.getButtonHeight()/2);
        backButton.setOnAction(e -> {
            if (canMove.get()) {
                game.window.setScene(createLevelSelectionScene());
                grid.getChildren().removeAll();
            }
        });
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        //Create labels
        Label turnOrder = new Label();
        BorderPane.setAlignment(turnOrder, Pos.TOP_CENTER);

        Label goldLabel = new Label(game.settings.gold + " Gold");

        Label speedLabel = new Label("Speed: " + LevelsStore.levelShipSpeed[0]);

        Label tutorialLabel = new Label(text0);
        tutorialLabel.setWrapText(true);

        // Create top container
        BorderPane topContainer = new BorderPane();
        topContainer.setPadding(new Insets(20));

        //Create grid pane for labels on top right
        GridPane topRightContainer = new GridPane();
        topRightContainer.add(goldLabel, 0 , 0);
        topRightContainer.add(speedLabel, 0 , 1);

        //Create stack pane for label on right
        StackPane rightContainer = new StackPane();
        rightContainer.setPadding(new Insets(0,20,0,0));
        rightContainer.getChildren().add(tutorialLabel);

        rootBorder.setTop(topContainer);
        rootBorder.setRight(rightContainer);
        topContainer.setTop(backButton);
        topContainer.setCenter(turnOrder);
        topContainer.setRight(topRightContainer);

        //Game variables
        turnOrder.setText("Your turn");

        // Create map
        int tileSize = levelObjectSize[0];
        double halfTileSize = tileSize / 2;

        grid.setPadding(new Insets(0, 0, 0 , (tileSize + halfTileSize)*2));

        // Border glow effect
        DropShadow borderGlow= new DropShadow();
        borderGlow.setColor(Color.YELLOW);
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setWidth(70);
        borderGlow.setHeight(70);
        speedLabel.setEffect(borderGlow);

        // Create arrow
        game.arrow = new Arrow(grid,tileSize);

        List<Rectangle> glowContainer = new ArrayList<>();

        // Populate the grid with squares
        for (int y = LevelsStore.levelMapSize[0][0]-1; y >=0 ; y--) {
            for (int x = 0; x < LevelsStore.levelMapSize[0][1]; x++) {
                if ((x ==5 && y == 1) ||
                        (x == 3 && y == 4)
                ){
                    // Create the four triangles that form the rectangle
                    Polygon triangle1 = new Polygon(0, 0, 0, tileSize, halfTileSize, halfTileSize); // Left
                    Polygon triangle2 = new Polygon(tileSize, 0, 0, 0, halfTileSize, halfTileSize); // Top
                    Polygon triangle3 = new Polygon(tileSize, tileSize, 0, tileSize, halfTileSize, halfTileSize);   // Bottom
                    Polygon triangle4 = new Polygon(tileSize, tileSize, tileSize, 0, halfTileSize, halfTileSize);   // Right

                    List<Polygon> buttons = new ArrayList<>();
                    buttons.add(triangle1);
                    buttons.add(triangle2);
                    buttons.add(triangle3);
                    buttons.add(triangle4);

                    if (x == 5){

                        Rectangle rectangle = new Rectangle(tileSize-2, tileSize-2);
                        rectangle.setMouseTransparent(true);
                        rectangle.setFill(Color.TRANSPARENT);
                        rectangle.setStroke(Color.YELLOW);
                        rectangle.setStrokeWidth(1);
                        glowContainer.add(rectangle);

                        grid.add(rectangle, 5, 1);

                        // Add a mouse click event to each triangle
                        for (int i = 0; i<4; i++){
                            String orient = "Left";
                            switch (i){
                                case 0 -> orient = "Left";
                                case 1 -> orient = "Top";
                                case 2 -> orient = "Bottom";
                                case 3 -> orient = "Right";
                            }

                            String finalOrient = orient;
                            buttons.get(i).setOnMouseClicked(event -> {
                                if (canMove.get() && game.pathfinder.IsPathOk(5, 1, game.ship.GetPosition(), finalOrient)){
                                    canMove.set(false);
                                    speedLabel.setEffect(null);
                                    rectangle.toBack();
                                    game.DrawShip(5, 1);

                                    turnOrder.setText("CPU turn");
                                    // Delay
                                    PauseTransition delay1 = new PauseTransition(Duration.seconds(1));
                                    PauseTransition delay2 = new PauseTransition(Duration.seconds(3));

                                    delay2.setOnFinished(e -> {
                                        // Code to be executed after the delay

                                        game.DrawShip(4, 2);
                                        game.arrow.Clear();
                                        canMove.set(true);
                                        turnOrder.setText("Your turn");
                                        tutorialLabel.setText(text1);
                                        glowContainer.get(0).toFront();

                                    });

                                    delay1.setOnFinished(e -> {
                                        // Code to be executed after the delay
                                        game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], 3, 2, "Right");
                                        delay2.play();
                                    });

                                    delay1.play();

                                    if(game.arrow.IsShown()){
                                        game.arrow.Clear();
                                    }
                                }
                            });
                            buttons.get(i).setOnMouseEntered(event -> {
                                if (canMove.get()){
                                    if(game.arrow.IsShown()){
                                        game.arrow.Clear();
                                    }
                                    if(!game.arrow.IsShown() && game.pathfinder.IsPathOk(5, 1, game.ship.GetPosition(), finalOrient)){
                                        game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], 5, 1, finalOrient);
                                    }
                                }
                            });
                        }
                    }

                    if (x == 3){

                        Rectangle rectangle = new Rectangle(tileSize-2, tileSize-2);
                        rectangle.setMouseTransparent(true);
                        rectangle.setFill(Color.TRANSPARENT);
                        rectangle.setStroke(Color.YELLOW);
                        rectangle.setStrokeWidth(1);
                        glowContainer.add(rectangle);

                        grid.add(rectangle, 3, 4);

                        // Add a mouse click event to each triangle
                        for (int i = 0; i<4; i++){
                            String orient = "Left";
                            switch (i){
                                case 0 -> orient = "Left";
                                case 1 -> orient = "Top";
                                case 2 -> orient = "Bottom";
                                case 3 -> orient = "Right";
                            }

                            String finalOrient = orient;
                            buttons.get(i).setOnMouseClicked(event -> {
                                if (canMove.get() && game.pathfinder.IsPathOk(3, 4, game.ship.GetPosition(), finalOrient)){
                                    canMove.set(false);
                                    rectangle.toBack();
                                    game.DrawShip(3, 4);

                                    turnOrder.setText("CPU turn");
                                    // Delay
                                    PauseTransition delay1 = new PauseTransition(Duration.seconds(1));
                                    PauseTransition delay2 = new PauseTransition(Duration.seconds(3));

                                    delay2.setOnFinished(e -> {
                                        // Code to be executed after the delay

                                        game.DrawShip(1, 5);
                                        game.arrow.Clear();
                                        canMove.set(true);
                                        turnOrder.setText("You win!");
                                        tutorialLabel.setText(text2);
                                        goldLabel.setEffect(borderGlow);

                                        game.saver.EndLevelSave(0);
                                    });

                                    delay1.setOnFinished(e -> {
                                        // Code to be executed after the delay
                                        game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], 1, 5, "Top");
                                        delay2.play();
                                    });

                                    delay1.play();

                                    if(game.arrow.IsShown()){
                                        game.arrow.Clear();
                                    }
                                }
                            });
                            buttons.get(i).setOnMouseEntered(event -> {
                                if (canMove.get()){
                                    if(game.arrow.IsShown()){
                                        game.arrow.Clear();
                                    }
                                    if(!game.arrow.IsShown() && game.pathfinder.IsPathOk(3, 4, game.ship.GetPosition(), finalOrient)){
                                        game.arrow.Draw(game.ship.GetPosition()[0], game.ship.GetPosition()[1], 3, 4, finalOrient);
                                    }
                                }
                            });
                        }
                    }



                    // Add the four triangles to the grid
                    for (int i = 0; i < 4; i++) {
                        grid.add(buttons.get(i), x, y);
                    }

                    // Set the position of each triangle to form the rectangle
                    triangle1.setTranslateX(triangle1.getTranslateX());
                    triangle1.setTranslateY(triangle1.getTranslateY());
                    triangle2.setTranslateX(triangle2.getTranslateX());
                    triangle2.setTranslateY(triangle2.getTranslateY() - halfTileSize/2);
                    triangle3.setTranslateX(triangle3.getTranslateX());
                    triangle3.setTranslateY(triangle3.getTranslateY() + halfTileSize/2);
                    triangle4.setTranslateX(triangle4.getTranslateX() + halfTileSize);
                    triangle4.setTranslateY(triangle4.getTranslateY());
                }

                game.DrawTile(x, y, tileSize, tileSize, grid);

                switch (LevelsStore.levelMap[0][y][x]){
                    case 'S' -> game.DrawShip(x, y, tileSize, tileSize, grid);
                    case 'G' -> game.DrawGoal(x, y, tileSize, tileSize, grid);
                    case 'R' -> game.DrawRock(x, y, tileSize, tileSize, grid);
                    case 'V' -> game.DrawVortex(x, y, tileSize, tileSize, grid);
                }
            }
        }

        glowContainer.get(1).toFront();

        //Initialize pathfinding
        game.pathfinder.Initialize(LevelsStore.levelMap[0], LevelsStore.levelShipSpeed[0], LevelsStore.levelMapSize[0], game.goal.GetPosition());

        // Set styles for button
        backButton.getStyleClass().add("back");

        // Set styles for label
        turnOrder.getStyleClass().add("turn-order");
        goldLabel.getStyleClass().add("shop-gold-counter");
        speedLabel.getStyleClass().add("ship-speed");
        tutorialLabel.getStyleClass().add("tutorial");

        // Add css file to scene
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene
        return new Scene(root);
    }

    public void createMainMenuScene() {
        // Create the stack pane
        StackPane root = new StackPane();

        // Background image
        ImageView background = Background.GetBackgroundMainMenu();
        background.fitWidthProperty().bind(game.window.widthProperty());
        background.fitHeightProperty().bind(game.window.heightProperty());
        root.getChildren().add(background);

        //Create border pane
        BorderPane rootBorder = new BorderPane();
        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(20));
        rootBorder.setTop(topBar);
        root.getChildren().add(rootBorder);
        background.toBack();

        Button levelSelectButton = new Button("Select Level");
        Button shopButton = new Button("Shop");
        Button exitButton = new Button("Exit");

        levelSelectButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());
        shopButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());
        exitButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());


        // Add functionality to the buttons
        levelSelectButton.setOnAction(e -> game.window.setScene(createLevelSelectionScene()));

        shopButton.setOnAction(e ->  game.window.setScene(createShop()));

        exitButton.setOnAction(e -> {
            game.Save();
            game.window.close(); // Close the application
        });

        //Create label
        Label mainMenuLabel = new Label("Deep Sea Dilemma");
        mainMenuLabel.getStyleClass().add("menu-title");
        BorderPane.setAlignment(mainMenuLabel, Pos.CENTER);
        topBar.setBottom(mainMenuLabel);

        // Create the main menu layout
        VBox mainMenuLayout = new VBox(40); // 40 is the spacing between elements
        mainMenuLayout.setAlignment(Pos.CENTER);
        mainMenuLayout.getChildren().addAll(levelSelectButton, shopButton, exitButton);
        rootBorder.setCenter(mainMenuLayout);

        // Add css file to scene
        rootBorder.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene and set it as the primary stage's scene
        game.mainMenu = new Scene(root);

    }

    public Scene createLevelSelectionScene() {
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.getButtonWidth()/2, game.getButtonHeight()/2);
        backButton.setOnAction(e -> game.window.setScene(game.mainMenu));

        // Create the stack pane
        StackPane root = new StackPane();

        // Background image
        ImageView background = Background.GetBackgroundLevelSelector();
        background.fitWidthProperty().bind(game.window.widthProperty());
        background.fitHeightProperty().bind(game.window.heightProperty());
        root.getChildren().add(background);

        BorderPane rootBorder = new BorderPane();
        root.getChildren().add(rootBorder);
        background.toBack();

        // Create the grid
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(50);
        grid.setVgap(50);
        rootBorder.setCenter(grid);

        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                i++;
                // Create the level button
                Button levelButton = new Button("Level " + i);
                levelButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());

                // Add an action to the button
                int finalI = i;
                levelButton.setOnAction(e -> game.window.setScene(ChoseMode(finalI)));

                if (game.settings.GetCompletedLevels()[i]){
                    levelButton.getStyleClass().add("level-completed");
                } else {
                    levelButton.getStyleClass().add("level");
                }

                // Add the button to the grid
                grid.add(levelButton, x, y);
            }
        }

        // Create tutorial button
        Button tutorialButton = new Button("Tutorial");
        tutorialButton.setPrefSize(game.getButtonWidth(), game.getButtonHeight());
        tutorialButton.setOnAction(e -> game.window.setScene(createTutorialLevelScene()));
        grid.add(tutorialButton, 1, 3);

        if (!game.settings.GetCompletedLevels()[0]){
            // Border glow effect
            DropShadow borderGlow= new DropShadow();
            borderGlow.setColor(Color.YELLOW);
            borderGlow.setOffsetY(0f);
            borderGlow.setOffsetX(0f);
            borderGlow.setWidth(70);
            borderGlow.setHeight(70);
            tutorialButton.setEffect(borderGlow);

            tutorialButton.getStyleClass().add("level");
        } else {
            tutorialButton.getStyleClass().add("level-completed");
        }

        BorderPane topContainer = new BorderPane();
        topContainer.setPadding(new Insets(20));
        rootBorder.setTop(topContainer);
        topContainer.setTop(backButton);

        // Position the back button
        BorderPane.setAlignment(backButton, Pos.TOP_LEFT);

        // Set styles for buttons
        backButton.getStyleClass().add("back");

        // Add css file to scene
        rootBorder.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

        // Create the scene
        return new Scene(root);
    }

    public Scene createShop(){
        // Create the grid
        GridPane grid = new GridPane();
        grid.setMinSize(6,7);//68
        grid.setPadding(new Insets(0, 200, 0, 0));
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Create the stack pane
        StackPane root = new StackPane();

        // Background image
        ImageView background = Background.GetBackgroundShop();
        background.fitWidthProperty().bind(game.window.widthProperty());
        background.fitHeightProperty().bind(game.window.heightProperty());
        root.getChildren().add(background);

        // Create the border pane
        BorderPane rootBorder = new BorderPane();
        root.getChildren().add(rootBorder);
        background.toBack();
        rootBorder.setCenter(grid);

        //Create Back button
        Button backButton = new Button("Back");
        backButton.setPrefSize(game.getButtonWidth()/2, game.getButtonHeight()/2);
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
        topContainer.setPadding(new Insets(20));
        rootBorder.setTop(topContainer);
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

        int tileSize = 150;
        //Ship images
        AddShopImages(grid, Settings.imageShips, tileSize, 1);
        //Tile images
        AddShopImages(grid, Settings.imageTiles, tileSize, 2);
        //Goal images
        AddShopImages(grid, Settings.imageGoals, tileSize, 3);
        //Vortex images
        AddShopImages(grid, Settings.imageVortices, tileSize, 4);
        //Rock images
        AddShopImages(grid, Settings.imageRocks, tileSize, 5);

        // Create buy buttons
        for (int x = 1; x <=5; x++){
            int y = 2;
            for (int i = 0; i <3; i++){
                Button button = new Button("Buy");
                button.setPrefSize(game.getButtonWidth()/2, game.getButtonHeight()/2);
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

        //Prices
        Label price1Row = new Label(Integer.toString(game.settings.cosmeticsPrice[1]));
        grid.add(price1Row, 0, 4);
        Label price2Row = new Label(Integer.toString(game.settings.cosmeticsPrice[2]));
        grid.add(price2Row, 0, 6);

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
        rootBorder.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/deep_sea_dilemma/styles.css")).toExternalForm());

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