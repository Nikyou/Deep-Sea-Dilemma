package com.deep_sea_dilemma.background;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Background {
    static ImageView background_level = new ImageView(new Image(Objects.requireNonNull(Background.class.getResourceAsStream("background_level.jpg"))));
    static ImageView background_main_menu = new ImageView(new Image(Objects.requireNonNull(Background.class.getResourceAsStream("background_mainmenu.jpg"))));

    public static ImageView GetBackgroundLevel(){
        return background_level;
    }
    public static ImageView GetBackgroundMainMenu(){
        return background_main_menu;
    }
}
