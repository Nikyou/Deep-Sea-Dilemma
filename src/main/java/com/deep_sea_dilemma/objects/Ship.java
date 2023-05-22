package com.deep_sea_dilemma.objects;

import com.deep_sea_dilemma.interfaces.ShipAI;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class Ship extends Entity implements ShipAI {

    int[] position = new int[2];;
    GridPane grid;

    Random generator;

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

    public void SetRandom(long seed){
        generator = new Random(seed);
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
    public void AIMakeTurnEasy() {

    }

    @Override
    public void AIMakeTurnNormal() {

    }

    @Override
    public void AIMakeTurnHard() {

    }
}
