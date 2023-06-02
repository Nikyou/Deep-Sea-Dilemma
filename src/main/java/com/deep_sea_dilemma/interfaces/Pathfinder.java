package com.deep_sea_dilemma.interfaces;


import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class Pathfinder {
    private static Pathfinder instance = null;
    private char[][] mapLayout;
    public List<int[]> winPositions;
    private int levelMapSizeX, levelMapSizeY;
    int[] goalPos = new int[2];
    private int shipSpeed;

    private Pathfinder(){
    }
    public void Initialize(char[][] mapLayout, int shipSpeed, int[] levelMapSize, int[] goalPos){
        this.mapLayout = mapLayout;
        this.shipSpeed = shipSpeed;
        this.levelMapSizeX = levelMapSize[0];
        this.levelMapSizeY = levelMapSize[1];
        this.goalPos[0] = goalPos[0];
        this.goalPos[1] = goalPos[1];
    }

    private boolean IsPathOkSpeed(int x, int y, int[] shipPos){
        if (shipPos[0] == x && shipPos[1] == y){return false;}
        return (Math.abs(x - shipPos[0]) + Math.abs(y - shipPos[1])) <= shipSpeed;
    }
    private boolean IsPathOkCloser(int x, int y, int[] shipPos){
        return (Math.abs(goalPos[0] - x) + Math.abs(goalPos[1] - y)) < (Math.abs(goalPos[0] - shipPos[0]) + Math.abs(goalPos[1] - shipPos[1]));
    }
    public boolean IsPathOkOrient(int x, int y, int[] shipPos, String orientation){
        switch (orientation){
            case "Top" -> {
                if (y == shipPos[1]){
                    return false;
                }
                if (shipPos[1] > y){
                    return false;
                }
            }
            case "Bottom" -> {
                if (y == shipPos[1]){
                    return false;
                }
                if (shipPos[1] < y){
                    return false;
                }
            }
            case "Right" -> {
                if (x == shipPos[0]){
                    return false;
                }
                if (shipPos[0] < x){
                    return false;
                }
            }
            case "Left" -> {
                if (x == shipPos[0]){
                    return false;
                }
                if (shipPos[0] > x){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean IsPathOkRock(int x, int y, int[] shipPos, String orientation){
        int lineType = 0;
        if (shipPos[1] == y) {
            lineType = 1; //only horizontal
        }
        if (shipPos[0] == x) {
            lineType = 2; //only vertical
        }

        switch (orientation){
            case "Top", "Bottom" -> {
                switch (lineType){
                    case 0, 2 -> {
                        if (RockOnPath(x, y, shipPos[0], shipPos[1], true)){
                            return false;
                        }
                    }
                    default -> {
                        return false;
                    }
                }

            }
            case "Right", "Left" -> {
                switch (lineType){
                    case 0, 1 -> {
                        if (RockOnPath(x, y, shipPos[0], shipPos[1], false)){
                            return false;
                        }
                    }
                    default -> {
                        return false;
                    }
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    public boolean IsPathOk(int x, int y, int[] shipPos, String orientation){
        return (
                IsPathOkSpeed(x, y, shipPos)
                && IsPathOkCloser(x, y, shipPos)
                && IsPathOkOrient(x, y, shipPos, orientation)
                && IsPathOkRock(x, y, shipPos, orientation)
        );
    }



    public boolean IsTurnLegal(boolean isNotEnd, int x, int y, int[] shipPos, String orientation, int currentPlayer, boolean isAI) {
        if (currentPlayer == 2 && isAI) {
            return false;
        }
        return isNotEnd && this.IsPathOk(x, y, shipPos,orientation);
    }

    public List<int[]> GetAllPossibleTurns(int[] shipPos){
        List<int[]> result = new ArrayList<>();

        int startX, startY, finalX, finalY;
        startX = Math.max(shipPos[0] - shipSpeed, 0);
        startY = Math.max(shipPos[1] - shipSpeed, 0);
        finalX = Math.min(shipPos[0] + shipSpeed, levelMapSizeX);
        finalY = Math.min(shipPos[1] + shipSpeed, levelMapSizeY);
        for (int x = startX; x < finalX; x++){
            for (int y = startY; y < finalY; y++){
                if (IsPathOk(x, y, shipPos, "Top")
                        || IsPathOk(x, y, shipPos, "Right")
                        || IsPathOk(x, y, shipPos, "Bottom")
                        || IsPathOk(x, y, shipPos, "Left")
                ) {
                    result.add(new int[]{x, y});
                }
            }
        }

        return result;
    }

    public void CalculateWinPositions(){
        winPositions = new ArrayList<>();
        for (int y = 0; y < levelMapSizeY; y++) {
            for (int x = 0; x < levelMapSizeX; x++) {
                int dx = Math.abs(goalPos[0] - x);
                int dy = Math.abs(goalPos[1] - y);

                int totalDistance = dx + dy;

                if (((totalDistance - 1) % (shipSpeed+1) == 0) || ((totalDistance == 1))) {
                    int[] coordinate = {x, y};
                    winPositions.add(coordinate);
                }
            }
        }
    }

    private boolean RockOnPath (int startX, int startY, int endX, int endY, boolean isVertical){
        int tempSX, tempSY, tempEX, tempEY;

        if (startX < endX){
            tempSX = startX;
            tempEX = endX;
        }else {
            tempSX = endX;
            tempEX = startX;
        }

        if (startY < endY){
            tempSY = startY;
            tempEY = endY;
        }else {
            tempSY = endY;
            tempEY = startY;
        }

        for (int i = tempSY; i <= tempEY; i++){
            if (isVertical){
                if (mapLayout[i][startX] == 'R'){
                    return true;
                }
            } else {
                if (mapLayout[i][endX] == 'R'){
                    return true;
                }
            }

        }

        for (int i = tempSX; i <= tempEX; i++){
            if (isVertical) {
                if (mapLayout[endY][i] == 'R') {
                    return true;
                }
            } else {
                if (mapLayout[startY][i] == 'R') {
                    return true;
                }
            }
        }
        return false;
    }

    public static Pathfinder Initialize()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Pathfinder();

        return instance;
    }
}
