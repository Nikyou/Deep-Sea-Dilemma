package com.deep_sea_dilemma.objects;

import com.deep_sea_dilemma.interfaces.Pathfinder;
import com.deep_sea_dilemma.interfaces.ShipAI;
import javafx.scene.layout.GridPane;

import java.util.*;

public class Ship extends Entity implements ShipAI {

    private final Pathfinder pathfinder = Pathfinder.Initialize();
    private int[] position = new int[2];
    private GridPane grid;

    private Random generator;
    private long seed;

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

    @Override
    public void SetRandom(long seed){
        this.seed = seed;
    }

    @Override
    public void SetGenerator() {
        int tempX = ReturnNotZero(position[0]);
        int tempY = ReturnNotZero(position[1]);

        long tempSeed = seed / tempX * tempY;
        generator = new Random(tempSeed);
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

    @Override
    public int[] AIMakeTurn(int difficulty) {
        SetGenerator();

        List<int[]> allTurns = pathfinder.GetAllPossibleTurns(position);

        switch (difficulty) {
            case 1 -> {
                return AIMakeTurnEasy(allTurns);
            }
            case 2 -> {
                return AIMakeTurnNormal(allTurns);
            }
            case 3 -> {
                return AIMakeTurnHard(allTurns);
            }
        }
        return new int[]{0, 0};
    }

    @Override
    public int[] AIMakeTurnEasy(List<int[]> allTurns) {
        int i = generator.nextInt(allTurns.size());
        return allTurns.get(i);
    }

    @Override
    public int[] AIMakeTurnNormal(List<int[]> allTurns) {
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

        SetGenerator();

        allTurns.addAll(winningPos);
        int i = generator.nextInt(allTurns.size());
        return allTurns.get(i);
    }

    @Override
    public int[] AIMakeTurnHard(List<int[]> allTurns) {
        double percent = 0.9;

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

        SetGenerator();

        allTurns.addAll(winningPos);
        int i = generator.nextInt(allTurns.size());
        return allTurns.get(i);
    }

    //Internal check if in elements is in array
    private boolean ArrayContains (List<int[]> list, int[] item){
        for (int [] i: list){
            if (i[0] == item[0] && i[1] == item[1]){
                return true;
            }
        }
        return false;
    }
    //Internal check for not zero
    private int ReturnNotZero (int item){
        if (item == 0){
            return 1;
        } else {
            return item;
        }
    }
}
