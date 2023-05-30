package com.deep_sea_dilemma.interfaces;


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

    public boolean IsPathOkSpeed(int x, int y, int[] shipPos){
        if (shipPos[0] == x && shipPos[1] == y){return false;}
        return (Math.abs(x - shipPos[0]) + Math.abs(y - shipPos[1])) <= shipSpeed;
    }
    public boolean IsPathOkCloser(int x, int y, int[] shipPos){
        return (Math.abs(goalPos[0] - x) + Math.abs(goalPos[1] - y)) < (Math.abs(goalPos[0] - shipPos[0]) + Math.abs(goalPos[1] - shipPos[1]));
    }

    public boolean IsPathOk(int x, int y, int[] shipPos, String orientation){
        if(IsPathOkSpeed(x,y, shipPos) && IsPathOkCloser(x,y, shipPos)){
            return true;
        }
        return false;
    }

    public boolean IsTurnLegal(boolean isNotEnd, int x, int y, int[] shipPos, String orientation, int currentPlayer, boolean isAI) {
        if (currentPlayer == 2 && isAI) {
            return false;
        }
        return isNotEnd && this.IsPathOk(x, y, shipPos,"Top");
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
                if (IsPathOk(x, y, shipPos, "")) {
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

    public static Pathfinder Initialize()
    {
        // To ensure only one instance is created
        if (instance == null)
            instance = new Pathfinder();

        return instance;
    }
}
