package com.deep_sea_dilemma.objects;

import javafx.scene.layout.GridPane;

public class Goal extends Entity {

    int[] position = new int[2];
    GridPane grid;

    @Override
    public void Draw(int x, int y, int width, int height, GridPane grid) {
        this.grid = grid;
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setMouseTransparent(true);
        this.grid.add(imageView, x, y);
        position[0] = x;
        position[1] = y;
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
}
