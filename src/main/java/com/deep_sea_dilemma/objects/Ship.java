package com.deep_sea_dilemma.objects;

import com.deep_sea_dilemma.interfaces.Pathfinder;
import com.deep_sea_dilemma.interfaces.ShipAI;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Ship extends Entity implements ShipAI {

    Pathfinder pathfinder = Pathfinder.Initialize();
    int[] position = new int[2];
    GridPane grid;

    Random generator;
    long seed;

    @Override
    public void Draw(int x, int y, int width, int height, GridPane grid) {
        this.grid = grid;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setMouseTransparent(false);
        this.grid.add(imageView, x, y);
        position[0] = x;
        position[1] = y;
    }
    public void Draw(int x, int y) {
        imageView.setX(x);
        imageView.setY(y);
        this.grid.getChildren().remove(imageView);
        this.grid.add(imageView, x, y);
        position = new int[]{x, y};
    }

    public void Draw(int[] cord) {
        imageView.setX(cord[0]);
        imageView.setY(cord[1]);
        this.grid.getChildren().remove(imageView);
        this.grid.add(imageView, cord[0], cord[1]);
        position = cord;
    }

    public void SetRandom(long seed){
        this.seed = seed;
    }

    public void SetPosition(int x, int y){
        position[0] = x;
        position[1] = y;
    }
    public void SetPosition(int[] pos){
        position = pos;
    }

    public int[] GetPosition(){
        return position;
    }

    public void Move(){

    }

    @Override
    public void AIMakeTurn(int difficulty) {
        int tempX;
        if (position[0] == 0){
            tempX = 1;
        } else {
            tempX = position[0];
        }

        int tempY;
        if (position[1] == 0){
            tempY = 1;
        } else {
            tempY = position[1];
        }

        long tempSeed = seed / tempX * tempY;
        generator = new Random(tempSeed);

        List<int[]> allTurns = pathfinder.GetAllPossibleTurns(position);

        switch (difficulty) {
            case 1 -> AIMakeTurnEasy(allTurns);
            case 2 -> AIMakeTurnNormal(allTurns);
            case 3 -> AIMakeTurnHard(allTurns);
        }
    }

    @Override
    public void AIMakeTurnEasy(List<int[]> allTurns) {
        int i = generator.nextInt(allTurns.size());
        Draw(allTurns.get(i));
    }

    @Override
    public void AIMakeTurnNormal(List<int[]> allTurns) {
        double percent = 0.5;
        List<int[]> winningPos = new ArrayList<>();
        for (int[] i : allTurns){
            if (ArrayContains(pathfinder.winPositions, i)) {
                winningPos.add(i);
            }
        }

        allTurns.removeAll(winningPos);
        Collections.shuffle(allTurns, generator);
        int nToRemove = (int) (allTurns.size()*percent);

        if (nToRemove > 0) {
            allTurns.subList(0, nToRemove).clear();
        }

        int tempX;
        if (position[0] == 0){
            tempX = 1;
        } else {
            tempX = position[0];
        }

        int tempY;
        if (position[1] == 0){
            tempY = 1;
        } else {
            tempY = position[1];
        }
        long tempSeed = seed / tempX * tempY;
        generator = new Random(tempSeed);
        System.out.println(tempX + " " + tempY + ": removed " + nToRemove + " left " + allTurns.size() + " win "+ winningPos.size());
        allTurns.addAll(winningPos);
        allTurns.addAll(winningPos);
        int i = generator.nextInt(allTurns.size());
        Draw(allTurns.get(i));
    }

    @Override
    public void AIMakeTurnHard(List<int[]> allTurns) {
        double percent = 0.8;

        List<int[]> winningPos = new ArrayList<>();
        for (int[] i : allTurns){
            if (ArrayContains(pathfinder.winPositions, i)) {
                winningPos.add(i);
            }
        }

        allTurns.removeAll(winningPos);
        Collections.shuffle(allTurns, generator);
        int nToRemove = (int) (allTurns.size()*percent);

        if (nToRemove > 0) {
            allTurns.subList(0, nToRemove).clear();
        }

        int tempX;
        if (position[0] == 0){
            tempX = 1;
        } else {
            tempX = position[0];
        }

        int tempY;
        if (position[1] == 0){
            tempY = 1;
        } else {
            tempY = position[1];
        }
        long tempSeed = seed / tempX * tempY;
        generator = new Random(tempSeed);
        System.out.println(tempX + " " + tempY + ": removed " + nToRemove + " left " + allTurns.size() + " win "+ winningPos.size());
        allTurns.addAll(winningPos);
        int i = generator.nextInt(allTurns.size());
        Draw(allTurns.get(i));
    }

    //Internal check
    private boolean ArrayContains (List<int[]> list, int[] item){
        for (int [] i: list){
            if (i[0] == item[0] && i[1] == item[1]){
                return true;
            }
        }
        return false;
    }
}
