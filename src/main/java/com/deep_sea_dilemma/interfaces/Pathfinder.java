package com.deep_sea_dilemma.interfaces;

import com.deep_sea_dilemma.objects.Ship;

public class Pathfinder {
    private char[][] mapLayout;
    private int shipSpeed;

    public Pathfinder(){
    }
    public void Initialize(char[][] mapLayout, int shipSpeed){
        this.mapLayout = mapLayout;
        this.shipSpeed = shipSpeed;
    }

    public boolean IsPathOkSpeed(int x, int y, int[] shipPos){
        if (shipPos[0] == x && shipPos[1] == y){return false;}
        return (Math.abs(x - shipPos[0]) + Math.abs(y - shipPos[1])) <= shipSpeed;
    }
    public boolean IsPathOkCloser(int x, int y, int[] shipPos, int[] goalPos){
        return (Math.abs(goalPos[0] - x) + Math.abs(goalPos[1] - y)) < (Math.abs(goalPos[0] - shipPos[0]) + Math.abs(goalPos[1] - shipPos[1]));
    }

    public boolean IsPathOk(int x, int y, int[] shipPos, int[] goalPos, String orientation){
        if(IsPathOkSpeed(x,y, shipPos) && IsPathOkCloser(x,y, shipPos, goalPos)){
            return true;
        }
        return false;
    }
}
