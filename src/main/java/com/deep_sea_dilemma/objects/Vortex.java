package com.deep_sea_dilemma.objects;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Vortex extends Entity{

    @Override
    public void Draw(int x, int y, int width, int height, GridPane grid) {
        ImageView tempImageView = new ImageView(imageView.getImage());
        tempImageView.setFitWidth(width);
        tempImageView.setFitHeight(height);
        tempImageView.setMouseTransparent(true);
        grid.add(tempImageView, x, y);
    }

}
