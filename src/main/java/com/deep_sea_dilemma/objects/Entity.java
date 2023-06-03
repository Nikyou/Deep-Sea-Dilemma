package com.deep_sea_dilemma.objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public abstract class Entity {
    private Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("default.jpg")));
    ImageView imageView = new ImageView(image);


    public abstract void Draw(int x, int y, int width, int height, GridPane grid);
    public void SetImage(Image image){
        this.image = image;
        this.imageView = new ImageView(image);
        imageView.setMouseTransparent(true);
    };
    public void SetImage(String path){
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        this.imageView = new ImageView(this.image);
        imageView.setMouseTransparent(true);
    };

    public static Image GetImageByPath(String path){
        return new Image(Objects.requireNonNull(Entity.class.getResourceAsStream(path)));
    };
}
